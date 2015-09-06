package com.analytictech.actors

import akka.actor.Actor
import com.analytictech.workflow.Step
import sext._

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global


class SubmitterActor extends Actor with akka.actor.ActorLogging {

  log.info("submitter started")

  def name = this.getClass.getSimpleName

  def classType = this.getClass

  val workflowManager = context.parent

  def receive: PartialFunction[Any, Unit] = {
    case x: (Step, Int, String) =>
      log.info("Priority: " + x._2 + ", " + x._1.treeString + " from workflow: " + x._3)
      processTask(x._1, x._2, x._3)
  }

  def processTask(step: Step, priority: Int, workflow: String) = {
    async {
      Thread.sleep(1000)
      log.info("completed step " + step.label + " from workflow "+workflow)
      workflowManager !(workflow, step.label)
    }
  }
}


