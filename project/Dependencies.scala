
import sbt._

object Dependencies {


  val generalDependencies = Seq(

    "com.lightbend.akka" %% "akka-stream-alpakka-google-cloud-pub-sub-grpc" % "2.0.0-M2"
  )

  val grpcNetty = Seq(
    "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime"
  )
}
