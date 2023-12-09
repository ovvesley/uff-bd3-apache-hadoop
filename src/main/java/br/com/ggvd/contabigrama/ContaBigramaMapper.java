/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ggvd.contabigrama;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class ContaBigramaMapper extends
        Mapper<Object, Text, Text, IntWritable> {

    private final IntWritable ONE = new IntWritable(1);

    private final Text bigrama = new Text();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {


        // get conf
        String nGramParam = context.getConfiguration().get("ngram");
        String nContagemMinima = context.getConfiguration().get("contagem_minima");

        System.out.println("------ Mapper ------");
        System.out.println("nGramParam: " + nGramParam);
        System.out.println("nContagemMinima: " + nContagemMinima);
        System.out.println("--------------------");

        int nGram = Integer.parseInt(nGramParam);
        int contagemMinima = Integer.parseInt(nContagemMinima);

        String line = value.toString();
        StringTokenizer st = new StringTokenizer(line, " ");

        if (!st.hasMoreTokens()) {
            return;
        }

        String lastPalavra = st.nextToken();

        if (st.countTokens() < nGram + 1) {
            return;
        }

        while (st.hasMoreTokens()) {
            String nextPalavra = st.nextToken();
            String myBigrama = lastPalavra + " " + nextPalavra;
            bigrama.set(myBigrama);
            context.write(bigrama, ONE);
            lastPalavra = nextPalavra;
        }

    }
}
