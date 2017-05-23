package com.laketravels.ch08.generator.web;

import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import com.github.javafaker.PhoneNumber;

/**
 * Created by pankajmisra on 3/17/17.
 */
public class ContactGenerator implements DataGenerator{


    private Faker faker = new Faker();


    public String generateData(int id, int offset) {
        PhoneNumber phone = faker.phoneNumber();
        Internet mail = faker.internet();
        String log = (id+offset) + "," +
                phone.cellPhone()+","+
                phone.phoneNumber()+","+
                mail.emailAddress();
        return log;
    }
}
