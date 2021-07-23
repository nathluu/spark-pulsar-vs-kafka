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
    .option("service.url", "pulsar://51.143.57.135:6650")
    .option("admin.url", "http://51.143.57.135:80")
    .option("pulsar.client.authPluginClassName","org.apache.pulsar.client.impl.auth.AuthenticationToken")
    .option("pulsar.client.authParams","token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.BzjL7d2zY0fRMy6_LmGi7tMikBMYxr7J8QsBroHWJTo")
    .option("topic", "apache/pulsar/mytopic")
    .option("startingOffsets", "latest")
    .load()

  val words = lines.selectExpr( "CAST(value AS STRING)")
    .as[String].flatMap(_.split("\\s+"))

  val wc = words.groupBy("value").count()
  val query = wc.selectExpr("to_json(struct(*)) AS value")
    .writeStream
    .format("pulsar")
    .outputMode("complete")
    .option("checkpointLocation", "tmp/checkpoint")
    .option("service.url", "pulsar://51.143.57.135:6650")
    .option("admin.url", "http://51.143.57.135:80")
    .option("pulsar.client.authPluginClassName","org.apache.pulsar.client.impl.auth.AuthenticationToken")
    .option("pulsar.client.authParams","token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.BzjL7d2zY0fRMy6_LmGi7tMikBMYxr7J8QsBroHWJTo")
    .option("topic", "apache/pulsar/mytopic1")
    .start()

  query.awaitTermination()
}
