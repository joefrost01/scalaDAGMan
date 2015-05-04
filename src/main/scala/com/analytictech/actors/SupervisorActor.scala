package com.analytictech.actors

import akka.actor.{Actor, Props, Terminated}

class SupervisorActor extends Actor with akka.actor.ActorLogging {

  log.info("supervisor started")

  val workflowEngine = context.actorOf(Props[WorkflowEngineActor], name = "WorkflowEngineActor")
  context.watch(workflowEngine)
  val stateManager = context.actorOf(Props[StateManagerActor], name = "StateManagerActor")
  context.watch(stateManager)

  def receive = {
    case Terminated(`workflowEngine`) => println("WorkflowEngine is shutdown")
    case _ => println("Supervisor received a message")
  }
}