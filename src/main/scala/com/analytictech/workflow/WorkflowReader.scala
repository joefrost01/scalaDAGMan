package com.analytictech.workflow

import java.io.{File, FileInputStream}

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.collection.parallel.mutable


class WorkflowReader {

  implicit val formats = DefaultFormats

  def readWorkflows(dir: File): mutable.ParHashMap[String, Workflow] = {
    val workflowList = new mutable.ParHashMap[String, Workflow]
    for (file <- dir.listFiles()) {
      if (file.isDirectory) {
        workflowList ++= readWorkflows(file).seq
      } else {
        val stream = new FileInputStream(file.getCanonicalPath)
        val json = scala.io.Source.fromInputStream(stream).mkString
        val workflow = parse(json).extract[Workflow]
        workflowList(workflow.name) = workflow
      }
    }
    workflowList
  }
}
