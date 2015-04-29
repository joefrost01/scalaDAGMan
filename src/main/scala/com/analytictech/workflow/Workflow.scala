package com.analytictech.workflow

import java.io.Serializable
import java.util

import scala.collection.mutable.ListBuffer


case class Workflow(name: String, resumable: Boolean=false, priority: Int=1, steps: List[Step], connectors: List[Connector]) extends Cloneable with Serializable {
  val stepMap = new util.HashMap[String, Step]
  val stepListInner = new ListBuffer[Step]


  steps.foreach(addStep)
  connectors.foreach(addConnector)


  def getLabels: util.Set[String] = {
    this.stepMap.keySet
  }

  def getSteps: ListBuffer[Step] = {
    this.stepListInner
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
    val v1: Step = addStep(stepMap.get(from))
    val v2: Step = addStep(stepMap.get(to))
    this.addConnector(v1, v2)
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
      this.removeConnector(from, to)
      val msg: String = "Connector between \'" + from + "\' and \'" + to + "\' introduces to cycle in the workflow"
      throw new CycleDetectedException(msg, cycle)
    }
  }

  def removeConnector(from: String, to: String) {
    val v1: Step = addStep(stepMap.get(from))
    val v2: Step = addStep(stepMap.get(from))
    this.removeConnector(v1, v2)
  }

  def removeConnector(from: Step, to: Step) {
    from.removeConnectorTo(to)
    to.removeConnectorFrom(from)
  }

  def getStep(label: String): Step = {
    val retValue = stepMap.get(label)
    retValue
  }

  def hasConnector(label1: String, label2: String): Boolean = {
    val v1 = getStep(label1)
    val v2 = getStep(label2)
    val retValue: Boolean = v1.getChildren.contains(v2)
    retValue
  }

  def getChildLabels(label: String): util.List[String] = {
    val step = getStep(label)
    step.getChildLabels
  }

  def getParentLabels(label: String): util.List[String] = {
    val step = getStep(label)
    step.getParentLabels
  }

  @throws(classOf[CloneNotSupportedException])
  override def clone: AnyRef = {
    val retValue = super.clone
    retValue
  }

  def isConnected(label: String): Boolean = {
    val step = this.getStep(label)
    val retValue = step.isConnected
    retValue
  }

  def getSuccessorLabels(label: String): util.List[String] = {
    val step: Step = getStep(label)
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