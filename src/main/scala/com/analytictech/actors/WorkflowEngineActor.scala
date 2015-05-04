package com.analytictech.actors

import akka.actor.Actor


class WorkflowEngineActor extends Actor {
  override def receive = {
    case _ => println("WorkflowEngineActor received a message")
  }
}
