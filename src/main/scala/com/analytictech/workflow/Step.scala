package com.analytictech.workflow

import java.io.Serializable
import java.util


case class Step(label: String, payload: Option[Task]) extends Cloneable with Serializable {


  private val children: util.List[Step] = new util.ArrayList[Step]
  private val parents: util.List[Step] = new util.ArrayList[Step]


  def addConnectorTo(step: Step) {
    this.children.add(step)
  }

  def removeConnectorTo(step: Step) {
    this.children.remove(step)
  }

  def addConnectorFrom(step: Step) {
    this.parents.add(step)
  }

  def removeConnectorFrom(step: Step) {
    this.parents.remove(step)
  }

  def getChildren: util.List[Step] = {
    this.children
  }

  def getChildLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](this.children.size)
    val i$: util.Iterator[Step] = this.children.iterator
    while (i$.hasNext) {
      val step: Step = i$.next
      retValue.add(label)
    }
    retValue
  }

  def getParents: util.List[Step] = {
    this.parents
  }

  def getParentLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](this.parents.size)
    val i$: util.Iterator[Step] = this.parents.iterator
    while (i$.hasNext) {
      val step: Step = i$.next
      retValue.add(label)
    }
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

  override def toString: String = {
    "Step{label=\'" + this.label + "\'" + "}"
  }
}