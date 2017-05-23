package com.laketravels.ch08.ingestor.customer;

import com.laketravels.ch08.model.data.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by pankajmisra on 3/9/17.
 */
public class Tuple2CustomerProfileMessageDeserializationSchema implements DeserializationSchema {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Object deserialize(byte[] bytes) throws IOException {
        /*
        Parse message stream. When customer data is streamed from db into Kafka using flume,
        the field values are separated by commas as delimiter
         */
        String message = new String(bytes);
        message=message.replace("\",\"", ",");
        message=message.substring(message.indexOf("\"")+1, message.lastIndexOf("\""));
        String[] data = message.split(",");

        //Deserialize parsed messages into customer objects
        Customer cust = new Customer();
        cust.setId(Integer.parseInt(data[0]));
        cust.setFirstName(data[1]);
        cust.setLastName(data[2]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data[3], formatter);
        cust.setDob(java.sql.Date.valueOf(date));

        //Serialize the customer object into tuple
        String customerMessage = MAPPER.writeValueAsString(cust);
        Tuple2<IntWritable, Text> tuple = new Tuple2<IntWritable, Text>();
        tuple.setFields( new IntWritable(cust.getId()), new Text(customerMessage));
        return tuple;
    }

    public boolean isEndOfStream(Object o) {
        return false;
    }

    public TypeInformation<Tuple2<IntWritable, Text>> getProducedType() {
        return new TupleTypeInfo<Tuple2<IntWritable, Text>>(TypeExtractor.createTypeInfo(IntWritable.class), TypeExtractor.createTypeInfo(Text.class));
    }
}
