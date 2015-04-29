package com.analytictech.worlfow

import java.util

import com.analytictech.workflow._
import org.scalatest.{FlatSpec, _}

class WorkflowTest extends FlatSpec with Matchers {

  val task1 = new Task("task1")
  val step1 = new Step("step1", task1)
  val task2 = new Task("task2")
  val step2 = new Step("step2", task2)
  val connector1 = new Connector(step1.label, step2.label)
  val connector2 = new Connector(step2.label, step1.label)
  val steps = step1 :: step2 :: Nil
  val connectors = connector1 :: connector2 :: Nil

  "A Workflow " should "throw a CycleDetectedException when steps are linked in a cycle" in {
    a[CycleDetectedException] should be thrownBy {
      new Workflow("test", false, 1, steps, connectors)
    }
  }

  it should "correctly report the children of a step" in {
    val workflow = new Workflow("test", false, 1, steps, connector1 :: Nil)
    val list=new util.ArrayList[String](1)
      list.add(step2.label)
    workflow.getChildLabels(step1.label) should equal(list)
  }

  it should "correctly report the parent of a step" in {
    val workflow = new Workflow("test", false, 1, steps, connector1 :: Nil)
    val list=new util.ArrayList[String](1)
    list.add(step1.label)
    workflow.getParentLabels(step2.label) should equal(list)
  }
}
