package br.com.ggvd.contabigrama;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ContaBigrama {

    public static void main(String[] args) throws IOException,
            InterruptedException, ClassNotFoundException {

        System.out.println("---- STARTER CONTA BIGRAMA ----");

        String paramNgram = args[0];
        String paramContagemMinima = args[1];
        String paramInputPath = args[2];
        String paramOutputDir = args[3];

        System.out.println("ngram: " + paramNgram);
        System.out.println("contagem_minima: " + paramContagemMinima);
        System.out.println("inputPath: " + paramInputPath);
        System.out.println("outputDir: " + paramOutputDir);


        Configuration conf = new Configuration(true);
        conf.set("ngram", paramNgram);
        conf.set("contagem_minima", paramContagemMinima);

        Job job = new Job(conf, "ContaBigrama");
        job.setJarByClass(ContaBigrama.class);

        job.setMapperClass(ContaBigramaMapper.class);
        job.setReducerClass(ContaBigramaReducer.class);
        job.setNumReduceTasks(1);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        Path inputPath = new Path(paramInputPath);
        Path outputDir = new Path(paramOutputDir);
        FileInputFormat.addInputPath(job, inputPath);
        job.setInputFormatClass(TextInputFormat.class);


        FileOutputFormat.setOutputPath(job, outputDir);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileSystem hdfs = FileSystem.get(conf);
        if (hdfs.exists(outputDir))
            hdfs.delete(outputDir, true);


        int code = job.waitForCompletion(true) ? 0 : 1;
        System.exit(code);

    }
}