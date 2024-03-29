package com.edge.fileMerge.sequenceFile;
//***********To change the behavior of Record reader*********** 
//We can  create a class(myRecordReader) which extends RecordReader ,Paralally we need to implement a new class(myFileInputFormat) which  extends FileInputFormat. 
//if we want to read the whole file as a single record we can overwrite isSplitable() to false, and use myRecordReader in this class. Then finally in the driver class we have to 
//job.setInputFormatClass(myFileInputFormat.class);

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.PropertyConfigurator;
/**Run As> Run Configurations..>
 * Project-> MapReduce,
 * MainClass-> com.edge.basic.WordCountNline
 * -conf conf/hadoop-localhost.xml -D mapreduce.job.reduces=2 input/misc/MultipleFiles output/smallFiles
*/

public class SmallFilesToSequenceFileConverter extends Configured implements Tool {


	public int run(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "SmallFilesToSequenceFileConverter");
		job.setJarByClass(SmallFilesToSequenceFileConverter.class);	
		job.setInputFormatClass(WholeFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);
		job.setMapperClass(SequenceFileMapper.class);
		Path outputPath = new Path(args[1]);
		FileSystem fs = FileSystem.get(new URI(outputPath.toString()), conf);
		fs.delete(outputPath);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, outputPath);
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		int exitCode = ToolRunner.run(new SmallFilesToSequenceFileConverter(), args);
		System.exit(exitCode);
	}
}

 class SequenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {
	private Text filenameKey;

	
	protected void setup(Context context) throws IOException, InterruptedException {
		InputSplit split = context.getInputSplit();
		Path path = ((FileSplit) split).getPath();
		filenameKey = new Text(path.toString());
	}

	
	protected void map(NullWritable key, BytesWritable value, Context context)
			throws IOException, InterruptedException {
		context.write(filenameKey, value);
	}
}