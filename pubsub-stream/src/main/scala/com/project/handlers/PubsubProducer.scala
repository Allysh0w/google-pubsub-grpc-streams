package com.project.handlers

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.googlecloud.pubsub.grpc.PubSubSettings
import akka.stream.alpakka.googlecloud.pubsub.grpc.scaladsl.{GooglePubSub, GrpcPublisher, PubSubAttributes}
import akka.stream.scaladsl._
import com.google.protobuf.ByteString
import com.google.pubsub.v1.pubsub._

import scala.concurrent.ExecutionContext

trait PubsubProducer {

  def startProducer(projectId: String, topic: String)(implicit mnat: ActorMaterializer,
                                                      ec: ExecutionContext) = {

    println("Starting producer 2")

    val publishMessage: PubsubMessage =
      PubsubMessage()
        .withData(ByteString.copyFromUtf8("Hello world!"))

    val publishRequest: PublishRequest =
      PublishRequest()
        .withTopic(s"projects/$projectId/topics/$topic")
        .addMessages(publishMessage)

    val source: Source[PublishRequest, NotUsed] =
      Source.single(publishRequest)

    val publishFlow: Flow[PublishRequest, PublishResponse, NotUsed] =
      GooglePubSub.publish(parallelism = 1)

    source.via(publishFlow).runWith(Sink.seq)
      .recover {
        case _@error => println("Error caused by => " + error.getMessage())
      }

  }


  def producer3(projectId: String, topic: String)(implicit mat: ActorMaterializer,
                  ec: ExecutionContext,
                  system: ActorSystem) = {

    val settings = PubSubSettings(system)
    val publisher = GrpcPublisher(settings)


    println("Starting producer 3")

    val publishMessage: PubsubMessage =
      PubsubMessage()
        .withData(ByteString.copyFromUtf8("Hello world!"))

    val publishRequest: PublishRequest =
      PublishRequest()
        .withTopic(s"projects/$projectId/topics/$topic")
        .addMessages(publishMessage)

    val source: Source[PublishRequest, NotUsed] =
      Source.single(publishRequest)

    val publishFlow: Flow[PublishRequest, PublishResponse, NotUsed] =
      GooglePubSub
        .publish(parallelism = 1)
        .withAttributes(PubSubAttributes.publisher(publisher))


    source.via(publishFlow).runWith(Sink.seq)
      .recover {
        case _@error => println("Error caused by => " + error.getMessage)
      }
  }
}
