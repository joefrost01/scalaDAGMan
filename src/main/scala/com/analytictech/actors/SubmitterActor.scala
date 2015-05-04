package com.analytictech.actors

import akka.actor.Actor
import akka.actor.Actor.Receive


class SubmitterActor extends Actor with akka.actor.ActorLogging {

  log.info("submitter started")

  override def receive: Receive = {
    case _ => Unit
  }
}
