package com.laketravels.ch11.service.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.MalformedURLException;

@Configuration
@EnableWebMvc
@ComponentScan
public class ServerConfiguration extends WebMvcAutoConfiguration{


    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws MalformedURLException {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources( new UrlResource(System.getProperty("spring.config.location")));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}