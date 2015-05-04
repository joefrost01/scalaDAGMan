package com.analytictech.actors

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.analytictech.workflow.{Task, WorkflowReader}
import com.typesafe.config.Config
import sext._


class WorkflowEngineActor extends Actor with akka.actor.ActorLogging {

  log.info("workflow engine started")

  //val reflections = new Reflections("com.analytictech.actors.submitters")
  //val subTypes = reflections.getSubTypesOf(Class[SubmitterActor]).toArray

  val submitter = context.actorOf(Props[SubmitterActor].withDispatcher("prio-dispatcher"), name = "SubmitterActor")
  context.watch(submitter)

  // Read all workflows
  val workflowRoot = new File(getClass.getResource("/workflows").getPath)
  val workflowReader = new WorkflowReader
  val workflows = workflowReader.readWorkflows(workflowRoot)

  // Submit root tasks of all workflows to the submitter
  for (workflow <- workflows.values.toList.sortBy(_.priority)) {
    for (root <- workflow.getRoots) {
        submitter !(root, workflow.priority, workflow.name)

    }
  }

  override def receive: PartialFunction[Any, Unit] = {

    case x => log.info(x.treeString)
      //workflows.get()
  }
}

class TaskPriorityActorMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(

  PriorityGenerator {

    // Task priority
    case x: Tuple2[Task, Int] => x._2
    case _ => 100

  })
