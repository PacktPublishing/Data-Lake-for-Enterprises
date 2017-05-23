package com.laketravels.ch09.batch.mr.loader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by pankajmisra on 3/18/17.
 */
public class ContactsLoader  {

    public static void main(String[] args) throws Exception {
        //Define Job Configurations
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Contacts Loader - Simple Mapper");
        job.setJarByClass(ContactsLoader.class);
        job.setMapperClass(LoadMapper.class);

        //Included support for file:// and hdfs:// file schemes
        conf.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
        );
        conf.set("fs.file.impl",
                org.apache.hadoop.fs.LocalFileSystem.class.getName()
        );
        //Set Input and Output Paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    private static class LoadMapper extends Mapper<Object, Text, LongWritable, Text> {
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            try {
                context.write(new LongWritable(Long.parseLong(line.substring(0, line.indexOf(",")))), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
