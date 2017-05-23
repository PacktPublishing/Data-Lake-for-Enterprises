package com.laketravels.ch11.service;

import com.laketravels.ch11.service.endpoint.CustomerEndpoint;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication extends ResourceConfig {
    public CustomerServiceApplication() {
        packages("com.laketravels.service.endpoint").
                register(ApiListingResourceJSON.class).
                register(JerseyApiDeclarationProvider.class).
                register(JerseyResourceListingProvider.class).
                register(CustomerEndpoint.class);


    }

}