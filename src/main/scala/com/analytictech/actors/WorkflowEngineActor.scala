package com.analytictech.actors

import java.io.File

import akka.actor.Actor
import com.analytictech.workflow.WorkflowReader
import sext._


class WorkflowEngineActor extends Actor with akka.actor.ActorLogging {

  log.info("workflow engine started")

  val workflowRoot = new File(getClass.getResource("/workflows").getPath)
  val workflowReader = new WorkflowReader
  val workflows = workflowReader.readWorkflows(workflowRoot)
  for (workflow <- workflows) {
    log.info(workflow.treeString)
  }

  override def receive = {
    case _ => println("WorkflowEngineActor received a message")
  }
}
