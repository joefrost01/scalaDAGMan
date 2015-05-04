package com.analytictech

import akka.actor._
import com.analytictech.actors.SupervisorActor
import com.typesafe.config.ConfigFactory


object Application extends App {

  val system = ActorSystem("Workflow4S", ConfigFactory.load)
  val supervisor = system.actorOf(Props[SupervisorActor], name = "Supervisor")

}


