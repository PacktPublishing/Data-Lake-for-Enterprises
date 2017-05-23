package com.laketravels.ch07.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by pankajmisra on 3/3/17.
 */
public class SimpleProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();

        /*
        Set the list of broker addresses separated by commas. This needs to
        be updated with IP of your VM running Kafka broker
        */
        props.setProperty("bootstrap.servers", "192.168.0.117:9092");

        //Set the serializer for key of the message(value)
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        //Set the serializer for the message (value)
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        //Create a producer
        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        //Create a message to be sent to a topic
        ProducerRecord message = new ProducerRecord("customer", "001", "A Sample Message...");

        //send the message
        producer.send(message);

        System.out.println("Message Published");

        //close the producer connection
        producer.close();
    }

}
