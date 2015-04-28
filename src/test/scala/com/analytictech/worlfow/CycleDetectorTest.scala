package com.analytictech.worlfow

import com.analytictech.workflow._
import org.scalatest.FlatSpec
import org.scalatest._

class CycleDetectorTest extends FlatSpec with Matchers {

  val task1=new Task("task1")
  val step1=new Step("step1",task1)
  val task2=new Task("task2")
  val step2=new Step("step2",task2)
  val connector1=new Connector(step1.label,step2.label)
  val connector2=new Connector(step2.label,step1.label)
  val steps=step1 :: step2 :: Nil
  val connectors = connector1 :: connector2 :: Nil

  "A Workflow " should "throw a CycleDetectedException when steps are linked in a cycle" in {
    a [CycleDetectedException] should be thrownBy { new Workflow("test",false,1,steps,connectors) }
  }

//  it should "produce NoSuchElementException when head is invoked" in {
//    intercept[NoSuchElementException] {
//      Set.empty.head
//    }
//  }
}
