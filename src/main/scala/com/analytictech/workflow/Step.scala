package com.analytictech.workflow

import java.io.Serializable
import java.util
import scala.collection.JavaConversions._


case class Step(label: String, task: Task) extends Cloneable with Serializable {

  //TODO convert to Scala List - mutable
  private val children: util.List[Step] = new util.ArrayList[Step]
  //TODO convert to Scala List - mutable
  private val parents: util.List[Step] = new util.ArrayList[Step]


  def addConnectorTo(step: Step) {
    if (!children.contains(step)) {
      children.add(step)
    }
  }

  def removeConnectorTo(step: Step) {
    this.children.remove(step)
  }

  def addConnectorFrom(step: Step) {
    if(!parents.contains(step)) {
      this.parents.add(step)
    }
  }

  def removeConnectorFrom(step: Step) {
    this.parents.remove(step)
  }

  //TODO convert to Scala List
  def getChildren: util.List[Step] = {
    this.children
  }

  //TODO convert to Scala List
  def getChildLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](children.size)
    for (child: Step <- children.toList) retValue.add(child.label)
    retValue
  }

  //TODO convert to Scala List
  def getParents: util.List[Step] = {
    this.parents
  }

  //TODO convert to Scala List
  def getParentLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](parents.size)
    for (parent: Step <- parents.toList) retValue.add(parent.label)
    retValue
  }

  def isLeaf: Boolean = {
    this.children.size == 0
  }

  def isRoot: Boolean = {
    this.parents.size == 0
  }

  def isConnected: Boolean = {
    this.isRoot || this.isLeaf
  }

  @throws(classOf[CloneNotSupportedException])
  override def clone: AnyRef = {
    val retValue: AnyRef = super.clone
    retValue
  }

}