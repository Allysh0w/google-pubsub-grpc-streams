import sbt.Resolver
import com.typesafe.sbt.packager.docker._


resolvers in ThisBuild ++= Seq(
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  Classpaths.typesafeReleases,
  Classpaths.sbtPluginReleases,
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Java.net Maven2 Repository" at "https://download.java.net/maven/2/",
  "Eclipse repositories" at "https://repo.eclipse.org/service/local/repositories/egit-releases/content/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

name := "google-pubsub-grpc-stream"

scalaVersion in ThisBuild := "2.12.10"

mainClass in Compile := Some("com.project.MainStream")


lazy val dockerCmd = Seq(
  Cmd("USER", "root"),
  // setting the run script executable
  Cmd("RUN",
    "apt-get update"
  ),
  Cmd("RUN",
    "apt-get install -y curl"
  ),
  Cmd("RUN",
    "curl -L -o sbt-1.3.0.deb https://dl.bintray.com/sbt/debian/sbt-1.3.0.deb"
  ),
  Cmd("RUN",
    "dpkg -i sbt-1.3.0.deb"
  ),
  Cmd("RUN",
    "rm sbt-1.3.0.deb"
  ),
  Cmd("RUN",
    "apt-get update"
  ),
  Cmd("RUN",
    "apt-get install -y sbt git"
  ),
  Cmd("RUN",
    "apt-get clean"
  ),
  Cmd("RUN",
    "apt-get autoclean"
  )
)

lazy val commonSettings = Seq(
  version := "0.0.1",
  organization := "com.project",
  scalaVersion := "2.12.10",
  packageName in Docker := "javac7/pubsub-streams",
  maintainer in Docker := "javac7",
  packageSummary := "PubSub Streams",
  packageDescription := "PubSub Streams",
  dockerBaseImage := "adoptopenjdk/openjdk8:latest"
)
lazy val mappingsFiles = file("/home/javac7/Downloads/bigquery-loader.json") -> "home/bigquery-loader.json"

lazy val pubsubStream = project
  .in(file("pubsub-stream"))
  .settings(commonSettings)
  .settings(name := "pubsub-stream")
  .enablePlugins(JavaAgent, AkkaGrpcPlugin, JavaAppPackaging, DockerPlugin)
  .settings(libraryDependencies ++= Dependencies.generalDependencies)
  .settings(javaAgents ++= Dependencies.grpcNetty)
  .settings(dockerCommands ++= dockerCmd)
  .settings(mappings in Universal += mappingsFiles)


//mappings in Universal += file("bigquery-loader.json") -> "home/bigquery-loader.json"

//  .settings(akkaGrpcGeneratedSources := Seq(AkkaGrpc.Client))
//  .settings(akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala))
//.enablePlugins(JavaAgent,AkkaGrpcPlugin, PackPlugin)

// .settings(packMain := Map("Main" -> "com.project.Main"))
//libraryDependencies ++= Seq(
//
//  "com.lightbend.akka" %% "akka-stream-alpakka-google-cloud-pub-sub-grpc" % "2.0.0-M2"
//
//)

//enablePlugins(JavaAgent)
//javaAgents ++= Seq("org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9")
//javaAgents += ("org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9")

//dependencyOverrides ++= Seq(
//  "com.lightbend.akka.grpc" %% "akka-grpc-runtime" % "0.7.3"
//)
//enablePlugins(AkkaGrpcPlugin)

//akkaGrpcGeneratedSources := Seq(AkkaGrpc.Client)
//akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala)

