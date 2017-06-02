package com.laketravels.ch11.service.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableConfigurationProperties
@Component
@EnableAutoConfiguration
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ESUtil {

    public ESUtil() {
    }

    private TransportClient client = null;

    @Value("${elastic.hosts}")
    private String hosts;

    @Value("${elastic.clusterName}")
    private String clusterName;


    public Client getConnection() throws UnknownHostException {
        if (client == null) {
            Settings settings = Settings.builder().put("cluster.name", clusterName)
                    .build();


            client = TransportClient.builder().settings(settings).build();
            String[] serverAddresses = null;
            if (hosts.length() > 0 && hosts.contains(":")) {
                serverAddresses = hosts.split(",");
            }
            if (serverAddresses != null && serverAddresses.length > 0) {
                for (String serverAddress : serverAddresses) {
                    String[] serverAddrressParts = serverAddress.split(":");
                    client.addTransportAddresses(new InetSocketTransportAddress(
                            InetAddress.getByName(serverAddrressParts[0]),
                            Integer.parseInt(serverAddrressParts[1])));
                }
            }
        }
        return client;

    }
}
