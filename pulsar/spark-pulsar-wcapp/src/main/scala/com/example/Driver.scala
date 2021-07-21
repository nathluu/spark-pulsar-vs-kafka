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
    .format("pulsar")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("topic", "apache/pulsar/mytopic")
    .option("startingOffsets", "latest")
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
    .format("pulsar")
    .outputMode("complete")
    .option("checkpointLocation", "tmp/checkpoint")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("topic", "apache/pulsar/mytopic1")
    .start()

  query.awaitTermination()
}
