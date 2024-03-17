package ru.spbu.apcyb.svp.tasks.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet.I;

public class LongestWord {

    public static class TokenizerMapper extends Mapper<Object, Text, IntWritable, Text> {

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer(
                value.toString().strip().replaceAll("[,.]", ""));

            while (itr.hasMoreTokens()) {
                String token = itr.nextToken().replaceAll(" ", "");

                IntWritable length = new IntWritable(token.length());
                Text word = new Text(token);

                context.write(length, word);
            }
        }
    }

    public static class WordReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

            StringBuilder words = new StringBuilder();
            for (Text value : values) {
                words.append(value.toString()).append(",");
            }
            words.deleteCharAt(words.length() - 1);

            context.write(key, new Text(words.toString()));
        }
    }

    public static class LongestWordMapper extends Mapper<Object, Text, IntWritable, Text> {

        private String longestWord = "";
        private int maxLength = 0;

        @Override
        public void map(Object key, Text value, Context context) {

            String[] line = value.toString().split("\t");

            int length = Integer.parseInt(line[0]);

            if (length > maxLength) {
                maxLength = length;
                longestWord = line[1];
            }
        }

        @Override
        protected void cleanup(Mapper<Object, Text, IntWritable, Text>.Context context)
            throws IOException, InterruptedException {

            context.write(new IntWritable(maxLength), new Text(longestWord));
        }
    }

    public static class LongestWordReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

        private int maxLength = 0;
        private Text longestWord = new Text();

        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context) {

            Text words = values.iterator().next();

            if (key.get() > maxLength) {
                maxLength = key.get();
                longestWord.set(words);
            }
        }

        @Override
        protected void cleanup(Reducer<IntWritable, Text, IntWritable, Text>.Context context)
            throws IOException, InterruptedException {

            context.write(new IntWritable(maxLength), longestWord);
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job lengthCount = Job.getInstance(conf, "Length count");
        lengthCount.setJarByClass(LongestWord.class);
        lengthCount.setMapperClass(TokenizerMapper.class);
        lengthCount.setReducerClass(WordReducer.class);
        lengthCount.setOutputKeyClass(IntWritable.class);
        lengthCount.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(lengthCount, new Path(args[0]));
        FileOutputFormat.setOutputPath(lengthCount, new Path("/tmp/lengthcount"));

        boolean isLengthCountComplete = lengthCount.waitForCompletion(false);
        if (!isLengthCountComplete) {
            System.exit(1);
        }

        Job getMaximum = Job.getInstance(conf, "Get longest word");
        getMaximum.setJarByClass(LongestWord.class);
        getMaximum.setMapperClass(LongestWordMapper.class);
        getMaximum.setReducerClass(LongestWordReducer.class);
        getMaximum.setNumReduceTasks(1);
        getMaximum.setOutputKeyClass(IntWritable.class);
        getMaximum.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(getMaximum, new Path("/tmp/lengthcount"));
        FileOutputFormat.setOutputPath(getMaximum, new Path(args[1]));

        boolean isJobComplete = getMaximum.waitForCompletion(false);

        try (FileSystem fs = FileSystem.newInstance(conf)) {

            fs.delete(new Path("/tmp/lengthcount"), true);
        }

        System.exit(isJobComplete ? 0 : 1);
    }
}