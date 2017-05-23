package com.laketravels.ch08.generator.web;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;

/**
 * Created by pankajmisra on 3/17/17.
 */
public class LocationGenerator implements DataGenerator {

    private Faker faker = new Faker();

    public String generateData(int id, int offset) {
        Address loc = faker.address();
        String log = "\"id\":" + (id+offset) + ",\"longitude\":" +
                loc.longitude()+",\"latitude\":"+
                loc.latitude();
        return log;
    }
}
