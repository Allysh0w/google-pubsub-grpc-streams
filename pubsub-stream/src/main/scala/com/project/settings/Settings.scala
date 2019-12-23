package com.project.settings

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

trait Settings {


  lazy private val appConf = ConfigFactory.load("application.conf")

  val mainConfig: Config = appConf.getConfig("stream").resolve()

  val gPubSub: Config = mainConfig.getConfig("alpakka.google.cloud.pubsub.grpc")

  implicit val system: ActorSystem = ActorSystem("System", gPubSub)
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

}

object Settings extends Settings {

}
