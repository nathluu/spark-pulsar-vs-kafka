package com.example

import org.apache.spark.sql.SparkSession

object Driver extends App {

  val spark = SparkSession.builder()
    .appName("WordCount")
    .master("local[2]")
    .getOrCreate()

  import spark.implicits._
  spark.sparkContext.setLogLevel("ERROR")

  val lines = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "192.168.56.2:29092")
    .option("subscribe", "mytopic")
    .load()

//  val query = lines.writeStream
//    .format("console")
//    .start()
  //  df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    val words = lines.selectExpr( "CAST(value AS STRING)")
      .as[String].flatMap(_.split("\\s+"))

    val wc = words.groupBy("value").count()
    val query = wc.selectExpr("to_json(struct(*)) AS value")
      .writeStream
      .format("kafka")
      .outputMode("complete")
      .option("checkpointLocation", "tmp/checkpoint/kafka")
      .option("kafka.bootstrap.servers", "192.168.56.2:29092")
      .option("topic", "mytopic1")
      .start()

  query.awaitTermination()
}
