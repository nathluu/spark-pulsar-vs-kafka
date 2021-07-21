name := "spark-kafka-wcapp"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.1.2" % "provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.1.2" % "provided"
)