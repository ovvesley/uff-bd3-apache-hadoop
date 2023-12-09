/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ggvd.contabigrama;

import java.io.IOException;
 
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class ContaBigramaReducer extends
        Reducer<Text, IntWritable, Text, IntWritable> {
 
    @Override
    public void reduce(Text text, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;


        String nGramParam = context.getConfiguration().get("ngram");
        String nContagemMinima = context.getConfiguration().get("contagem_minima");

        System.out.println("------ Reduce ------");
        System.out.println("nGramParam: " + nGramParam);
        System.out.println("nContagemMinima: " + nContagemMinima);
        System.out.println("--------------------");


        for (IntWritable value : values) {
            sum += value.get();
        }

        if (sum < Integer.parseInt(nContagemMinima)) {
            return;
        }

        context.write(text, new IntWritable(sum));
    }
}