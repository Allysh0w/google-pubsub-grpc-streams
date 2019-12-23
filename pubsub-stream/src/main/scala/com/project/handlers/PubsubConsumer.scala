package com.project.handlers

import akka.Done
import akka.actor.Cancellable
import akka.stream.ActorMaterializer
import akka.stream.alpakka.googlecloud.pubsub.grpc.scaladsl.GooglePubSub
import akka.stream.scaladsl.{Sink, Source}
import com.google.pubsub.v1.pubsub.{AcknowledgeRequest, ReceivedMessage, StreamingPullRequest}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait PubsubConsumer {

  def startStream(projectId: String, subscription: String)(implicit mat: ActorMaterializer,
                                                           ec: ExecutionContext) = {

    println("starting stream...")

    val request = StreamingPullRequest()
      .withSubscription(s"projects/$projectId/subscriptions/$subscription")
      .withStreamAckDeadlineSeconds(10)

    val subscriptionSource: Source[ReceivedMessage, Future[Cancellable]] =
      GooglePubSub.subscribe(request, pollInterval = 1.second)


    val ackSink: Sink[AcknowledgeRequest, Future[Done]] =
      GooglePubSub.acknowledge(parallelism = 1)

    subscriptionSource
      .map { message =>
        // do something fun
        println("Message => " + message.ackId)
        message.ackId
      }
      .runWith(Sink.head)
//      .groupedWithin(10, 1.second)
//      .map(ids => AcknowledgeRequest(ackIds = ids))
//      .to(ackSink).run()
      .recover{
      case _@error => println("error caused by: " + error.getMessage)
    }
  }
}
