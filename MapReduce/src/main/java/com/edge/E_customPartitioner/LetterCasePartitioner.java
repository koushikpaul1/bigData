package com.edge.E_customPartitioner;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class LetterCasePartitioner extends Partitioner<Text, IntWritable> {

	
	public int getPartition(Text key, IntWritable value, int numPart) {
		if (numPart == 2) {
			if (Character.isLowerCase(key.charAt(0))) {
				return 0;
			} else {
				return 1;
			}
		} else {
			System.err
					.println("WordCountParitioner can only handle either 1 or 2 paritions");
			return 1;
		}
	}

}
