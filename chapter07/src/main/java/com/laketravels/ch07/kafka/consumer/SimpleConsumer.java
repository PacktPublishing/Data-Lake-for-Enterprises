package com.laketravels.ch07.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;


public class SimpleConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();

        /*
        Set the list of broker addresses separated by commas. This needs to
        be updated with IP of your VM running Kafka broker
        */
        props.setProperty("bootstrap.servers", "192.168.0.117:9092");

        //Set the deserializer for the key of the message
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        //Set the deserializer for the message (value)
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        //Set the groupId
        props.put("group.id", "1234");

        //Create a consumer from Kafka Consumer
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        //Subscribe the consumer to the topic
        consumer.subscribe(Arrays.asList("customer"));


        try {
            while (true) {
                //Get All records from latest offset
                ConsumerRecords<String, String> records = consumer.poll(100);

                //Display all records
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("key:" + record.key() + "\nvalue:" + record.value());
                }
            }
        } finally {
            consumer.close();
        }

    }
}
