/**
 * The driver class.
 * Grouping Comparator is used for secondary sort, That means sort not only keys (general method)
 * but also sort values , or a part of values. these values or part of values are taken along with 
 * the keys , making a composite key . Saying that , I have received same result using composite 
 * keys with grouping comparator , and taking the whole as a key using normal map and reduce only, 
 * while sorting in natural order. Check the other driver class for the same.
 */
package com.edge.G_sort.customValueSort;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;


/**
 * @author New
 *
 */
public class Driver1 {
	
public static void main(String[] args) throws Exception { 
	String log4jConfPath = "log4j.properties";
	PropertyConfigurator.configure(log4jConfPath);
    	Configuration conf = new Configuration();
		if (args.length != 2) {
			System.err.println("Usage: SecondarySort <in> <out>");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "Driver1");	
		job.setJarByClass(Driver1.class);
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);
		job.setOutputKeyClass(Employee.class);
		job.setOutputValueClass(Text.class);			
		
		job.setMapOutputKeyClass(Employee.class);
		job.setMapOutputValueClass(Text.class);
		//job.setPartitionerClass(CostomPartitioner.class);
		job.setSortComparatorClass(KeyComparator.class);
		job.setGroupingComparatorClass(GroupingComparator.class);
		job.setNumReduceTasks(5);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path outputPath = new Path(args[1]);
		FileSystem fs = FileSystem.get(new URI(outputPath.toString()), conf);
		fs.delete(outputPath, true);
		FileOutputFormat.setOutputPath(job, outputPath);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
