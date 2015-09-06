package com.analytictech.actors

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.analytictech.workflow.{Step, Task, WorkflowReader}
import com.typesafe.config.Config
import sext._


class WorkflowEngineActor extends Actor with akka.actor.ActorLogging {

  log.info("workflow engine started")

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

    case x: (String, String) =>
      val workflow=workflows.get(x._1).get
      val step = workflow.getStep(x._2)
      step.completed=true
      for (child <- step.getChildren) {
        if (!child.getParents.exists(p => !p.completed)) {
          submitter !(child, workflow.priority, workflow.name)
        }
      }
  }
}

class TaskPriorityActorMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(

  PriorityGenerator {

    // Task priority
    case x: Tuple2[Task, Int] => x._2
    case _ => 100

  })
