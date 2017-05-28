package com.laketravels.ch09.batch.mr.loader;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;


public class ContactsLoader extends Configured implements Tool{

    public static void main(String[] args) throws Exception {
        int exitCode= ToolRunner.run(new ContactsLoader(),args );
        System.out.println("Exit code "+exitCode);
    }

    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();

        //Integrate with YARN endpoints for Job Control and Monitoring
        conf.set("fs.defaultFS", "hdfs://192.168.0.117:9000");
        conf.set("mapreduce.jobtracker.address", "192.168.0.117:54311");
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resourcemanager.address", "192.168.0.117:8032");

        //Included support for file:// and hdfs:// file schemes
        conf.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
        );
        conf.set("fs.file.impl",
                org.apache.hadoop.fs.LocalFileSystem.class.getName()
        );

        Job job= Job.getInstance(conf,"Contacts Loader");
        job.setJarByClass(ContactsLoader.class);

        //Set Input and Output Paths
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //Configure Schema for avro data load
        Schema.Parser parser = new Schema.Parser();
        Schema schema=parser.parse(Thread.currentThread().getContextClassLoader()
                                    .getResourceAsStream("contacts.avsc"));

        //Overide default class loading policy to load application specific libraries first
        job.getConfiguration().setBoolean(
                Job.MAPREDUCE_JOB_USER_CLASSPATH_FIRST, true);

        //Configure Job Mapper and Reducer classes
        job.setMapperClass(LoadMapper.class);
        job.setReducerClass(LoadReducer.class);


        //Set Map Output Value Format, Input Format and final Output Format Class
        job.setMapOutputValueClass(NullWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(AvroKeyOutputFormat.class);

        //Set Avro Job schema parameters for Map Output and Reducer Output
        AvroJob.setMapOutputKeySchema(job, schema);
        AvroJob.setOutputKeySchema(job, schema);

        int status = job.waitForCompletion(true) ? 0 : 1;
        return status;
    }

    private static class LoadMapper extends Mapper<LongWritable, Text, AvroKey<GenericRecord>, NullWritable> {
        Schema schema;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            //Setup initializes the contacts schema
            Schema.Parser parser = new Schema.Parser();
            schema=parser.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("contacts.avsc"));
        }


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            try {
                //Schema and Data used for Record population
                GenericRecord record = new GenericData.Record(schema);
                String inputRecord=value.toString();
                String[] values = inputRecord.split(",");
                record.put("id", values[0]);
                record.put("cell", values[1]);
                record.put("phone", values[2]);
                record.put("email", values[3]);

                context.write(new AvroKey(record), NullWritable.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private static class LoadReducer extends Reducer<AvroKey<GenericRecord>,NullWritable,AvroKey<GenericRecord>,NullWritable> {

        @Override
        public void reduce(AvroKey<GenericRecord> key, Iterable<NullWritable> value, Context context) throws IOException, InterruptedException{
            try {
                context.write(key, NullWritable.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
