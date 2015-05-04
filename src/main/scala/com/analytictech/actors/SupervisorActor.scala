package com.analytictech.actors

import akka.actor.{Actor, Props, Terminated}

class SupervisorActor extends Actor {
  // start WorkflowEngineActor as a child, then monitor
  val workflowEngine = context.actorOf(Props[WorkflowEngineActor], name = "WorkflowEngineActor")
  context.watch(workflowEngine)

  def receive = {
    case Terminated(workflowEngine) => println("WorkflowEngine is shutdown")
    case _ => println("Parent received a message")
  }
}