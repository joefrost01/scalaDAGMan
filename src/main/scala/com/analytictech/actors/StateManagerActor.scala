package com.analytictech.actors

import akka.actor.Actor


class   StateManagerActor extends Actor with akka.actor.ActorLogging {

  log.info("state manager started")

  override def receive: Receive = {
    case _ => Unit
  }
}
