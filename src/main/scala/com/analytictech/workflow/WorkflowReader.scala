package com.analytictech.workflow

import java.io.{File, FileInputStream}

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable.ArrayBuffer


class WorkflowReader {

  implicit val formats = DefaultFormats

  def readWorkflows(dir: File): ArrayBuffer[Workflow] = {
    val workflowList = new ArrayBuffer[Workflow]
    for (file <- dir.listFiles()) {
      if (file.isDirectory) {
        workflowList ++= readWorkflows(file)
      } else {
        val stream = new FileInputStream(file.getCanonicalPath)
        val json = scala.io.Source.fromInputStream(stream).mkString
        workflowList += parse(json).extract[Workflow]
      }
    }
    workflowList
  }
}
