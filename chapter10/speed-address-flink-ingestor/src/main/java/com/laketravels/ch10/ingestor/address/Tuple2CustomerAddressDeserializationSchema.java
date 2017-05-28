package com.laketravels.ch10.ingestor.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laketravels.ch08.model.data.Address;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Created by pankajmisra on 3/9/17.
 */
public class Tuple2CustomerAddressDeserializationSchema implements DeserializationSchema {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final int KAFKA_TIMESTAMP_LENGTH = 28;

    public Object deserialize(byte[] bytes) throws IOException {
        String message = new String(bytes,KAFKA_TIMESTAMP_LENGTH,
                                    bytes.length-(KAFKA_TIMESTAMP_LENGTH));
        if (message.trim().length()>0) {
            Address address = new Address();
            String[] fields = message.replaceAll("\"","").split(",");
            address.setId(Integer.parseInt(fields[0].trim()));
            address.setCustomerId(Integer.parseInt(fields[1].trim()));
            address.setStreet1(fields[2]);
            address.setStreet2(fields[3]);
            address.setCity(fields[4]);
            address.setState(fields[5]);
            address.setCountry(fields[6]);
            address.setZipCode(fields[7]);
            Tuple2<IntWritable, Text> tuple = new Tuple2<IntWritable, Text>();
            tuple.setFields(new IntWritable(address.getId()),
                    new Text(MAPPER.writeValueAsString(address)));
            return tuple;
        } else {
            return null;
        }
    }

    public boolean isEndOfStream(Object o) {
        return false;
    }

    public TypeInformation<Tuple2<IntWritable, Text>> getProducedType() {
        return new TupleTypeInfo<Tuple2<IntWritable, Text>>(TypeExtractor.createTypeInfo(IntWritable.class), TypeExtractor.createTypeInfo(Text.class));
    }
}
