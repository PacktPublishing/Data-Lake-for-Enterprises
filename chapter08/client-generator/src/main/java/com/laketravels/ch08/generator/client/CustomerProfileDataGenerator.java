package com.laketravels.ch08.generator.client;

import com.laketravels.ch08.model.data.*;
import com.github.javafaker.*;
import org.fluttercode.datafactory.impl.DataFactory;

import java.io.*;
import java.io.File;
import java.sql.*;
import java.util.Properties;


public class CustomerProfileDataGenerator {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        if (args.length != 2) {
            System.out.println(
                    "Usage: java -jar laketravels-client-generator <configuration-file-path> <number-of-Records>"
            );
            return;
        }

        File configFile = new File(args[0]);
        int numRows = Integer.parseInt(args[1]);

        Connection conn = getConnection(configFile);

        Faker faker = new Faker();

        String custQuery = "INSERT INTO CUSTOMER VALUES(?,?,?,?)";
        String custAddressQuery = "INSERT INTO ADDRESS VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatementCustomer = conn.prepareStatement(custQuery);
        PreparedStatement preparedStatementAddress = conn.prepareStatement(custAddressQuery);
        conn.setAutoCommit(false);
        for(int ctr=1;ctr<=numRows;ctr++) {
            Customer cust = generateCustomerBasicProfile(ctr, faker);
            preparedStatementCustomer.setInt(1, cust.getId());
            preparedStatementCustomer.setString(2, cust.getFirstName());
            preparedStatementCustomer.setString(3, cust.getLastName());
            preparedStatementCustomer.setDate(4,  new Date(cust.getDob().getTime()));
            preparedStatementCustomer.addBatch();

            com.laketravels.ch08.model.data.Address address = cust.getAddress();
            preparedStatementAddress.setInt(1, address.getId());
            preparedStatementAddress.setInt(2, cust.getId());
            preparedStatementAddress.setString(3, address.getStreet1());
            preparedStatementAddress.setString(4, address.getStreet2());
            preparedStatementAddress.setString(5, address.getCity());
            preparedStatementAddress.setString(6, address.getState());
            preparedStatementAddress.setString(7, address.getCountry());
            preparedStatementAddress.setString(8, address.getZipCode());
            preparedStatementAddress.addBatch();



            if (ctr % 1000000 == 0 || ctr == numRows) {
                preparedStatementCustomer.executeBatch();
                preparedStatementAddress.executeBatch();
                conn.commit();
                System.out.println("Inserted " + ctr+ " rows");
            }
        }
    }

    public static Connection getConnection(File configFile) throws ClassNotFoundException, IOException, SQLException {
        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.load(new FileInputStream(configFile));
        return DriverManager.getConnection(props.getProperty("jdbc.url"), props);
    }

    public static Customer generateCustomerBasicProfile(int id, Faker faker) {
        DataFactory df = new DataFactory();
        Customer cust = new Customer();
        cust.setId(100+id);
        cust.setFirstName(df.getFirstName());
        cust.setLastName(df.getLastName());
        cust.setDob(df.getBirthDate());


        com.laketravels.ch08.model.data.Address address = new com.laketravels.ch08.model.data.Address();
        cust.setAddress(address);
        address.setId(100+id);
        com.github.javafaker.Address fakeAddress = faker.address();
        address.setStreet1(fakeAddress.streetAddressNumber() + " - " + fakeAddress.streetName());
        address.setStreet2(fakeAddress.streetAddress());
        address.setCity(fakeAddress.cityName());
        address.setState(fakeAddress.state());
        address.setCountry(fakeAddress.country());
        address.setZipCode(fakeAddress.zipCode());
        return cust;
    }
}
