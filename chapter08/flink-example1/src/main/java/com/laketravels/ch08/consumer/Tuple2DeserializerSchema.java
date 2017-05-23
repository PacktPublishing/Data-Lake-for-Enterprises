package com.laketravels.ch08.consumer;

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


public class Tuple2DeserializerSchema implements DeserializationSchema {
    public Object deserialize(byte[] bytes) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer cust = (Customer) mapper.readValue(new String(bytes), Customer.class);
        Tuple2<IntWritable, Text> tuple = new Tuple2<IntWritable, Text>();
        tuple.setFields( new IntWritable(cust.getId()), new Text(new String(bytes)));
        return tuple;
    }

    public boolean isEndOfStream(Object o) {
        return false;
    }

    public TypeInformation<Tuple2<IntWritable, Text>> getProducedType() {
        return new TupleTypeInfo<Tuple2<IntWritable, Text>>(TypeExtractor.createTypeInfo(IntWritable.class), TypeExtractor.createTypeInfo(Text.class));
    }
}
