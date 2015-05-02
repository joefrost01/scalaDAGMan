package com.analytictech.workflow

import java.io.Serializable
import java.util

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


case class Workflow(name: String,
                    resumable: Boolean = false,
                    priority: Int = 1,
                    steps: List[Step],
                    connectors: List[Connector]
                     ) extends Cloneable with Serializable {


  val stepMap = new util.HashMap[String, Step]
  val stepListInner = new ListBuffer[Step]


  steps.foreach(addStep)
  connectors.foreach(addConnector)


  def getLabels: util.Set[String] = {
    stepMap.keySet
  }

  def getSteps: ListBuffer[Step] = {
    stepListInner
  }

  def addStep(step: Step): Step = {
    var retValue: Step = null
    if (this.stepMap.containsKey(step.label)) {
      retValue = stepMap.get(step.label)
    }
    else {
      retValue = step
      this.stepMap.put(step.label, retValue)
      this.stepListInner += step
    }
    retValue
  }

  @throws(classOf[CycleDetectedException])
  def addConnector(from: String, to: String) {
    val v1 = addStep(stepMap.get(from))
    val v2 = addStep(stepMap.get(to))
    addConnector(v1, v2)
  }

  @throws(classOf[CycleDetectedException])
  def addConnector(connector: Connector): Unit = {
    addConnector(connector.from, connector.to)
  }

  @throws(classOf[CycleDetectedException])
  def addConnector(from: Step, to: Step) {
    from.addConnectorTo(to)
    to.addConnectorFrom(from)
    val cycle = CycleDetector.introducesCycle(to)
    if (cycle != null) {
      removeConnector(from, to)
      val msg: String = "Connector between \'" + from + "\' and \'" + to + "\' introduces to cycle in the workflow"
      throw new CycleDetectedException(msg, cycle)
    }
  }

  def removeConnector(from: String, to: String) {
    val v1: Step = addStep(stepMap.get(from))
    val v2: Step = addStep(stepMap.get(from))
    removeConnector(v1, v2)
  }

  def removeConnector(from: Step, to: Step) {
    from.removeConnectorTo(to)
    to.removeConnectorFrom(from)
  }

  def getStep(label: String): Step = {
    stepMap.get(label)
  }

  def hasConnector(label1: String, label2: String): Boolean = {
    val v1 = getStep(label1)
    val v2 = getStep(label2)
    v1.getChildren.contains(v2)
  }

  def getChildLabels(label: String): ArrayBuffer[String] = {
    val step = getStep(label)
    step.getChildLabels
  }

  def getParentLabels(label: String): ArrayBuffer[String] = {
    val step = getStep(label)
    step.getParentLabels
  }

  @throws(classOf[CloneNotSupportedException])
  override def clone: AnyRef = {
    super.clone

  }

  def isConnected(label: String): Boolean = {
    val step = this.getStep(label)
    step.isConnected
  }

  def getSuccessorLabels(label: String): util.List[String] = {
    val step = getStep(label)
    var retValue: util.List[String] = new util.ArrayList[String]
    if (step.isLeaf) {
      retValue = new util.ArrayList[String](1)
      retValue.add(label)
    }
    else {
      retValue = TopologicalSorter.sort(step)
    }
    retValue
  }
}