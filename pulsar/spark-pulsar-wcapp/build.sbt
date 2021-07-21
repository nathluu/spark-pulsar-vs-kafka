resolvers ++= Seq (
  Resolver.mavenLocal,
  DefaultMavenRepository,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

name := "spark-pulsar-wcapp"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided",
  "io.streamnative.connectors" %% "pulsar-spark-connector" % "2.4.5" % "provided"
)