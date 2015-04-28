package com.analytictech

import java.io.Serializable
import java.util



case class Node(label: String, payload: Option[Task]) extends Cloneable with Serializable {


  private val children: util.List[Node] = new util.ArrayList[Node]
  private val parents: util.List[Node] = new util.ArrayList[Node]


  def addEdgeTo(node: Node) {
    this.children.add(node)
  }

  def removeEdgeTo(node: Node) {
    this.children.remove(node)
  }

  def addEdgeFrom(node: Node) {
    this.parents.add(node)
  }

  def removeEdgeFrom(node: Node) {
    this.parents.remove(node)
  }

  def getChildren: util.List[Node] = {
    this.children
  }

  def getChildLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](this.children.size)
    val i$: util.Iterator[Node] = this.children.iterator
    while (i$.hasNext) {
      val node: Node = i$.next
      retValue.add(label)
    }
    retValue
  }

  def getParents: util.List[Node] = {
    this.parents
  }

  def getParentLabels: util.List[String] = {
    val retValue = new util.ArrayList[String](this.parents.size)
    val i$: util.Iterator[Node] = this.parents.iterator
    while (i$.hasNext) {
      val node: Node = i$.next
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
    "Node{label=\'" + this.label + "\'" + "}"
  }
}