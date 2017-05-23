package com.laketravels.ch08.ingestor.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laketravels.ch08.PropertyLoader;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.fs.SequenceFileWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.Properties;

/**
 * Created by pankajmisra on 3/9/17.
 */
public class CustomerDBMessageProcessor {

    public static void main(String[] args) throws Exception {

        final ObjectMapper mapper = new ObjectMapper();
        Properties flinkProps = PropertyLoader.loadProperty("flink.properties");
        // create execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        // parse user parameters
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        DataStream<Tuple2<IntWritable, Text>> messageStream = env.addSource(
                                                new FlinkKafkaConsumer010(
                                                        parameterTool.getRequired("topic"),
                                                        new Tuple2CustomerProfileMessageDeserializationSchema(),
                                                        parameterTool.getProperties()));

        messageStream.rebalance().print();
        System.setProperty("HADOOP_USER_NAME", flinkProps.getProperty("hdfsUser"));
        BucketingSink<Tuple2<IntWritable, Text>> hdfsSink = new BucketingSink<Tuple2<IntWritable, Text>>(flinkProps.getProperty("hdfsPath"));
        hdfsSink.setBucketer(new DateTimeBucketer("yyyy-MM-dd--HHmm"));
        hdfsSink.setWriter(new SequenceFileWriter<IntWritable, Text>());
        hdfsSink.setBatchSize(1024 * 1024 ); //Write 1 MB at a time
        messageStream.addSink(hdfsSink);

        env.execute();
    }
}
