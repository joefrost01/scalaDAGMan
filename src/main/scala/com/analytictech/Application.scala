package com.analytictech

import java.io.InputStream
import java.util.concurrent.{BlockingQueue, DelayQueue}
import com.analytictech.workflow.{Workflow, TaskTimeout, Connector}
import org.json4s._
import org.json4s.jackson.JsonMethods._


object Application {

  def main(args: Array[String]) {

    implicit val formats = DefaultFormats

    val expiryQueue: BlockingQueue[TaskTimeout] = new DelayQueue()
    val taskTimeout = new TaskTimeout("test", 1000)
    expiryQueue.put(taskTimeout)

    val stream : InputStream = getClass.getResourceAsStream("/workflow1.json")
    val json = scala.io.Source.fromInputStream( stream ).mkString
    val dag = parse(json).extract[Workflow]

    println(dag)
    println("done")
  }

  def JSONtoEdge(jsPath: String): Connector = {
    new Connector("from","to")
  }
}