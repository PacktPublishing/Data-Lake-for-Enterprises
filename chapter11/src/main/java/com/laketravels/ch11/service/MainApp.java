package com.laketravels.ch11.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class})

public class MainApp {
    public static void main(String[] args) throws Exception {
        if (args.length>0) {
            String springConfigLocationParam = args[0].trim();
            if (springConfigLocationParam != null && springConfigLocationParam.length()>0) {
                String[] configLocation = springConfigLocationParam.split("=");
                if (configLocation.length>1){
                    String configLocationValue=configLocation[1];
                    System.setProperty("spring.config.location", new File(configLocationValue).toURI().toURL().toString());
                }
            }
        }
        ConfigurableApplicationContext ctx = SpringApplication.run(new Class[]{MainApp.class, MainApplicationInitializer.class}, args);
    }
}