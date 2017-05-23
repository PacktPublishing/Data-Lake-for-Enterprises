package com.laketravels.ch08.db.producer;

import com.laketravels.ch08.PropertyLoader;
import com.laketravels.ch08.model.data.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DBProducer {

    private static final String CUSTOMER_QUERY = "SELECT * FROM CUSTOMER";

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        publishCustomers();
    }

    private static void publishCustomers() throws SQLException, IOException, ClassNotFoundException {
        //Initialize the object mapper for serialization/deserialization
        ObjectMapper mapper = new ObjectMapper();

        //Load the producer properties and initialize Kafka producer
        Properties producerProps = PropertyLoader.loadProperty("producer.properties");
        Producer kafkaProducer = initializeProducer(producerProps);

        //Establish database connection & execute query to retrieve all customer records
        Connection conn = getConnection();
        System.out.println("Database Connection Established...");
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(CUSTOMER_QUERY);
        System.out.println("Query Executed...");

        //Serialize all customer records into JSON string and publish to Kafka topic
        while(result.next()) {
            Customer cust = new Customer();
            cust.setId(result.getInt("id"));
            cust.setFirstName(result.getString("first_Name"));
            cust.setLastName(result.getString("last_Name"));
            cust.setDob(result.getDate("dob"));

            //Serialize object into a JSON string
            String customerMessage = mapper.writeValueAsString(cust);

            //Publish customer record as JSON message
            ProducerRecord message = new ProducerRecord(producerProps.getProperty("topicName"),
                                            String.valueOf(cust.getId()), customerMessage);
            kafkaProducer.send(message);
        }
        System.out.println("Messages Published");

        //Close producer and exit
        conn.close();
        kafkaProducer.close();
    }

    private static Producer initializeProducer(Properties producerConfig) throws IOException {
        Producer<String, String> producer = new KafkaProducer<String, String>(producerConfig);
        return  producer;
    }

    private static void closeProducer(Producer producer) {
        if (producer != null) {
            producer.close();
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, IOException, SQLException {
        Class.forName("org.postgresql.Driver");
        Properties props = PropertyLoader.loadProperty("db.properties");
        return DriverManager.getConnection(props.getProperty("jdbc.url"), props);
    }
}
