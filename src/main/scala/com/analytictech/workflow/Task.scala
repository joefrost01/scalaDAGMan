package com.analytictech.workflow


case class Task(command: String, parameters: List[String] = {
  "" :: Nil
}) {

}
