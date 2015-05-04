package com.analytictech

import akka.actor._
import com.analytictech.actors.SupervisorActor


object Application extends App {

  val system = ActorSystem("Workflow4S")
  val supervisor = system.actorOf(Props[SupervisorActor], name = "Supervisor")


}


