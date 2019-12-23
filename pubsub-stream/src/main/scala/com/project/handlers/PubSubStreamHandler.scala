package com.project.handlers

import akka.stream.Materializer
import akka.stream.alpakka.googlecloud.pubsub.grpc.scaladsl.GooglePubSub
import akka.stream.scaladsl.{Sink, Source}
import com.google.protobuf.ByteString
import com.google.pubsub.v1.pubsub.{PublishRequest, PubsubMessage, StreamingPullRequest}
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

trait PubSubStreamHandler {

  protected def subscribeStream(projectId: String, sub: String)(implicit mat: Materializer, ec: ExecutionContext) = {

    println("Startint consumer..")

    GooglePubSub
      .subscribe(subscribe(projectId, sub), 1.second)
      .map(msg => println(msg.ackId))
      .runWith(Sink.head)
//      .to(Sink.foreach(println))
//      .run()
      .recover{
        case _@error => println("Error caused by 1 => " + error.getMessage)
      }
  }

  protected def publishSingle(projectId: String, topic: String)(implicit mat: Materializer, ec: ExecutionContext) = {

    println("publishing...")
    Source
      .single(publish(projectId, topic)("Hello!"))
      .via(GooglePubSub.publish(parallelism = 1))
      .map(x => println(x.messageIds.mkString))
      .runWith(Sink.head)
      .recover{
        case _@error => println("Error caused by 2 => " + error.getMessage)
      }
  }

  private def publish(projectId: String, topic: String)(msg: String) =
    PublishRequest(topicFqrs(projectId, topic), Seq(PubsubMessage(ByteString.copyFromUtf8(msg))))

  private def subscribe(projectId: String, sub: String) =
    StreamingPullRequest(subFqrs(projectId, sub)).withStreamAckDeadlineSeconds(10)

  private def topicFqrs(projectId: String, topic: String) =
    s"projects/$projectId/topics/$topic"

  private def subFqrs(projectId: String, sub: String) =
    s"projects/$projectId/subscriptions/$sub"


}
