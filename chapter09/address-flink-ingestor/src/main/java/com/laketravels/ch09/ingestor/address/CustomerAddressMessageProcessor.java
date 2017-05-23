package com.laketravels.ch09.ingestor.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laketravels.ch08.model.data.Address;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.hadoop.shaded.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch2.ElasticsearchSink;
import org.apache.flink.streaming.connectors.elasticsearch2.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch2.RequestIndexer;
import org.apache.flink.streaming.connectors.fs.Writer;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pankajmisra on 3/9/17.
 */
public class CustomerAddressMessageProcessor {



    public static void main(String[] args) throws Exception {

        // create execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE, true);

        // parse user parameters
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        DataStream<Tuple2<IntWritable, Text>> messageStream = env.addSource(
                                                new FlinkKafkaConsumer010(
                                                        parameterTool.getRequired("topic"),
                                                        new Tuple2CustomerAddressDeserializationSchema(),
                                                        parameterTool.getProperties()));
        messageStream.rebalance();
        System.setProperty("HADOOP_USER_NAME", "centos");

        //HDFS Sink
        BucketingSink<Tuple2<IntWritable, Text>> hdfsSink = new BucketingSink<Tuple2<IntWritable, Text>>(parameterTool.getRequired("hdfsPath"));
        hdfsSink.setBucketer(new DateTimeBucketer("yyyy-MM-dd--HH-mm"));
        hdfsSink.setWriter(new SinkParquetWriter<Tuple2<IntWritable, Text>>("address.avsc"));
        hdfsSink.setBatchSize(1024 * 1024 * 1);
        messageStream.addSink(hdfsSink);

        //Elasticsearch Sink
        Map<String, String> config = Maps.newHashMap();
        config.put("bulk.flush.max.actions", "1");
        config.put("cluster.name", "elasticsearch");

        List<InetSocketAddress> transports = new ArrayList<InetSocketAddress>();
        transports.add(new InetSocketAddress(InetAddress.getByName(parameterTool.getRequired("esHost")),
                                                    Integer.parseInt(parameterTool.getRequired("esPort"))
                                                ));

        messageStream.addSink(new ElasticsearchSink<Tuple2<IntWritable, Text>>(config, transports,
                                                        new ElasticsearchSinkFunction<Tuple2<IntWritable, Text>>() {
            public void process(Tuple2<IntWritable, Text> intWritableTextTuple2, RuntimeContext runtimeContext,
                                                                                    RequestIndexer requestIndexer) {
                Map<String, Object> json = new java.util.HashMap<String, Object>();
                String jsonString = ((Text)intWritableTextTuple2.getField(1)).toString();
                IndexRequest request = Requests.indexRequest()
                        .index("address")
                        .type("address")
                        .source(jsonString);
                requestIndexer.add(request);
            }
        }));

        env.execute();
    }


    private static class SinkParquetWriter<T> implements Writer<T> {

        transient ParquetWriter writer = null;
        String schema = null;
        final ObjectMapper MAPPER = new ObjectMapper();

        public SinkParquetWriter(String schema) {
            this.writer = writer;
            this.schema = schema;
        }

        public void open(FileSystem fileSystem, Path path) throws IOException {
            writer = AvroParquetWriter.builder(path)
                    .withSchema( new Schema.Parser().parse(ClassLoader.getSystemClassLoader()
                                                                .getResourceAsStream(schema)))
                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                    .build();
        }

        public long flush() throws IOException {
            return writer.getDataSize();
        }

        public long getPos() throws IOException {
            return writer.getDataSize();
        }

        public void close() throws IOException {
            writer.close();
        }


        public void write(T t) throws IOException {
            final Tuple2<IntWritable, Text> tuple = (Tuple2<IntWritable, Text>) t;
            final List values = new ArrayList();
            GenericRecord record = new GenericData.Record(new Schema.Parser().parse(Thread.currentThread()
                                                                                .getContextClassLoader()
                                                                                .getResourceAsStream(schema)));
            String inputRecord=tuple.f1.toString();
            Address address = MAPPER.readValue(inputRecord,
                                                                Address.class);
            record.put("id", String.valueOf(address.getId()));
            record.put("customerId", address.getCustomerId());
            record.put("street1", address.getStreet1());
            record.put("street2", address.getStreet2());
            record.put("city", address.getCity());
            record.put("state", address.getState());
            record.put("country", address.getCountry());
            record.put("zipCode", address.getZipCode());

            writer.write(record);
        }

        public Writer<T> duplicate() {
            return new SinkParquetWriter<T>(schema);
        }
    }
}
