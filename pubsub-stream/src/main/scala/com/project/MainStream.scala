package com.project

import com.project.handlers.{PubSubStreamHandler, PubsubConsumer, PubsubProducer}
import com.project.settings.Settings._

object MainStream extends App with PubSubStreamHandler with PubsubConsumer with PubsubProducer{

  println("OK 2")


  //google pubsub publisher
  println("starting publisher")
  publishSingle("careful-span-260816","bigquery-loader")


  // google pubsub subscriber
  println("starting google pub sub consumer")
  subscribeStream("careful-span-260816","bigquery-loader")

  //startStream("careful-span-260816","bigquery-loader")

  //startProducer("careful-span-260816","bigquery-loader")
  //producer3("careful-span-260816","bigquery-loader")

}
