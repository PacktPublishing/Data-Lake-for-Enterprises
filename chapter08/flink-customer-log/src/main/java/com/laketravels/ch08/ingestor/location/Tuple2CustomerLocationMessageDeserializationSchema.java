package com.laketravels.ch08.ingestor.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class Tuple2CustomerLocationMessageDeserializationSchema implements DeserializationSchema {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Object deserialize(byte[] bytes) throws IOException {
        //Spooled messages have 2 bytes of leading unicode chars
        String message = new String(bytes, 2, bytes.length-2);
        System.out.println(message);
        if (message.trim().length()>0) {
            String[] locationAttributes = message.split(",");
            ObjectNode locationObject = MAPPER.createObjectNode();
            for (String attribute : locationAttributes) {
                String[] attributeElments = attribute.split(":");
                String attributeName = attributeElments[0].replaceAll("\"", "");
                String attributeValue = attributeElments[1].replaceAll("\"", "");
                locationObject.put(attributeName, attributeValue);
            }
            Tuple2<IntWritable, Text> tuple = new Tuple2<IntWritable, Text>();
            tuple.setFields(new IntWritable(locationObject.get("id").asInt()),
                    new Text(locationObject.toString()));
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
