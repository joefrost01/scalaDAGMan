package com.analytictech.workflow

import java.io.Serializable

import scala.collection.mutable.ArrayBuffer


case class Step(label: String, task: Task) extends Cloneable with Serializable {

  private val children = new ArrayBuffer[Step]
  private val parents = new ArrayBuffer[Step]
  var completed = false

  def addConnectorTo(step: Step) {
    if (!children.contains(step)) {
      children += step
    }
  }

  def removeConnectorTo(step: Step) {
    children -= step
  }

  def addConnectorFrom(step: Step) {
    if (!parents.contains(step)) {
      parents += step
    }
  }

  def removeConnectorFrom(step: Step) {
    parents -= step
  }

  def getChildren: ArrayBuffer[Step] = {
    children
  }

  def getChildLabels: ArrayBuffer[String] = {
    val retValue = new ArrayBuffer[String](children.size)
    for (child <- children.toList) retValue += child.label
    retValue
  }

  def getParents: ArrayBuffer[Step] = {
    parents
  }

  def getParentLabels: ArrayBuffer[String] = {
    val retValue = new ArrayBuffer[String](parents.size)
    for (parent: Step <- parents.toList) retValue += parent.label
    retValue
  }

  def isLeaf: Boolean = {
    children.size == 0
  }

  def isRoot: Boolean = {
    parents.size == 0
  }

  def isConnected: Boolean = {
    isRoot || isLeaf
  }

  @throws(classOf[CloneNotSupportedException])
  override def clone: AnyRef = {
    super.clone
  }

}