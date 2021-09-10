resolvers ++= Seq (
  Resolver.mavenLocal,
  DefaultMavenRepository,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

name := "spark-pulsar-wcapp-jwt"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.1" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.1.1" % "provided",
  "io.streamnative.connectors" %% "pulsar-spark-connector" % "3.1.1.1" % "provided"
)