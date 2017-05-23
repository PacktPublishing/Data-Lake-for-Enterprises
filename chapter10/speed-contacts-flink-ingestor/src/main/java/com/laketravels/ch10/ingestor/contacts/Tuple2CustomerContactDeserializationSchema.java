package com.laketravels.ch10.ingestor.contacts;

import com.laketravels.ch08.model.data.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class Tuple2CustomerContactDeserializationSchema implements DeserializationSchema {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Object deserialize(byte[] bytes) throws IOException {
        //Spooled messages have 2 bytes of leading unicode chars
        String message = new String(bytes, 2, bytes.length-2);
        System.out.println(message);
        if (message.trim().length()>0) {
            String[] contactFields = message.split(",");
            Contact contact = new Contact();
            contact.setId(Integer.parseInt(contactFields[0].trim()));
            contact.setCell(contactFields[1]);
            contact.setWork(contactFields[2]);
            contact.setEmail(contactFields[3]);
            Tuple2<IntWritable, Text> tuple = new Tuple2<IntWritable, Text>();
            tuple.setFields(new IntWritable(contact.getId()),
                    new Text(MAPPER.writeValueAsString(contact)));
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
