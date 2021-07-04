/*package com.sundogsoftware.sparkStreaming;
// Kafka setup instructions for Windows: https://dzone.com/articles/running-apache-kafka-on-windows-os

package just

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import java.util.regex.Pattern
import java.util.regex.Matcher
import Utilities._
import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder

*//** Working example of listening for log data from Kafka's testLogs topic on port 9092. *//*
object KafkaExample {
  
  def main(args: Array[String]) {
    val ssc = new StreamingContext("local[*]", "KafkaExample", Seconds(1))    
    setupLogging()
    val pattern = apacheLogPattern()
    val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")
    val topics = List("locator").toSet
    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topics).map(_._2)
    val requests = lines.map(x => {val matcher:Matcher = pattern.matcher(x); if (matcher.matches()) matcher.group(5)})
     val urls = requests.map(x => {val arr = x.toString().split(" "); if (arr.size == 3) arr(1) else "[error]"})
       val urlCounts = urls.map(x => (x, 1)).reduceByKeyAndWindow(_ + _, _ - _, Seconds(300), Seconds(1))
       val sortedResults = urlCounts.transform(rdd => rdd.sortBy(x => x._2, false))
    sortedResults.print()
     ssc.checkpoint("C:/checkpoint/")
    ssc.start()
    ssc.awaitTermination()
  }
}

*/