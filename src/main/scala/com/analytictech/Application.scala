package com.analytictech

import java.io.InputStream
import java.util.concurrent.{BlockingQueue, DelayQueue}
import java.io.File

import com.analytictech.actors.SupervisorActor
import com.analytictech.workflow.{WorkflowReader, TaskTimeout, Workflow}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import sext._
import akka.actor._


object Application extends App {


  implicit val formats = DefaultFormats

//  val expiryQueue: BlockingQueue[TaskTimeout] = new DelayQueue()
//  val taskTimeout = new TaskTimeout("test", 1000)
//  expiryQueue.put(taskTimeout)

  val workflowRoot = new File(getClass.getResource("/workflows").getPath)
  val workflowReader = new WorkflowReader
  val workflows = workflowReader.readWorkflows(workflowRoot)
  for (workflow <- workflows) {
    println(workflow.treeString)
  }




  // create the ActorSystem instance
  val system = ActorSystem("Workflow4S")
  val supervisor = system.actorOf(Props[SupervisorActor], name = "Supervisor")
  // lookup kenny, then kill it
  val workflowEngine = system.actorSelection("/user/Supervisor/WorkflowEngineActor")
  workflowEngine ! PoisonPill
  Thread.sleep(5000)
  println("calling system.shutdown")
  system.shutdown

}


