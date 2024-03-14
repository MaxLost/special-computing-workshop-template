package ru.spbu.apcyb.svp.tasks.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LongestWord {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer(
                value.toString().strip().replaceAll("[,.]", ""));

            while (itr.hasMoreTokens()) {
                String token = itr.nextToken();
                IntWritable length = new IntWritable(token.length());
                word.set(token);
                context.write(word, length);
            }
        }
    }

    public static class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        private int maxLength = 0;
        private Text longestWord = new Text();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) {

            IntWritable length = values.iterator().next();
            if (length.get() >= maxLength) {
                maxLength = length.get();
                longestWord.set(key);
            }
        }

        @Override
        protected void cleanup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {

            context.write(longestWord, new IntWritable(maxLength));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Longest word");
        job.setJarByClass(LongestWord.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(WordReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}