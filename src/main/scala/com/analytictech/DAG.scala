package com.analytictech

import java.io.Serializable
import java.util

import com.analytictech.DAGManager.{EdgeList, NodeList}

import scala.collection.mutable.ListBuffer


case class DAG(nodeList: NodeList, edgeList: EdgeList) extends Cloneable with Serializable {
  val nodeMap = new util.HashMap[String, Node]
  val nodeListInner = new ListBuffer[Node]


  nodeList.nodes.foreach(addNode)
  edgeList.edges.foreach(addEdge)


  def getLabels: util.Set[String] = {
    this.nodeMap.keySet
  }

  def getNodes: ListBuffer[Node] = {
    this.nodeListInner
  }

  def addNode(node: Node): Node = {
    var retValue: Node = null
    if (this.nodeMap.containsKey(node.label)) {
      retValue = this.nodeMap.get(node.label)
    }
    else {
      retValue = node
      this.nodeMap.put(node.label, retValue)
      this.nodeListInner+=node
    }
    retValue
  }

  @throws(classOf[CycleDetectedException])
  def addEdge(from: String, to: String) {
    val v1: Node = addNode(nodeMap.get(from))
    val v2: Node = addNode(nodeMap.get(to))
    this.addEdge(v1, v2)
  }

  @throws(classOf[CycleDetectedException])
  def addEdge(edge: Edge): Unit = {
    addEdge(edge.from, edge.to)
  }

  @throws(classOf[CycleDetectedException])
  def addEdge(from: Node, to: Node) {
    from.addEdgeTo(to)
    to.addEdgeFrom(from)
    val cycle = CycleDetector.introducesCycle(to)
    if (cycle != null) {
      this.removeEdge(from, to)
      val msg: String = "Edge between \'" + from + "\' and \'" + to + "\' introduces to cycle in the graph"
      throw new CycleDetectedException(msg, cycle)
    }
  }

  def removeEdge(from: String, to: String) {
    val v1: Node = this.addNode(nodeMap.get(from))
    val v2: Node = this.addNode(nodeMap.get(from))
    this.removeEdge(v1, v2)
  }

  def removeEdge(from: Node, to: Node) {
    from.removeEdgeTo(to)
    to.removeEdgeFrom(from)
  }

  def getNode(label: String): Node = {
    val retValue = this.nodeMap.get(label)
    retValue
  }

  def hasEdge(label1: String, label2: String): Boolean = {
    val v1 = this.getNode(label1)
    val v2 = this.getNode(label2)
    val retValue: Boolean = v1.getChildren.contains(v2)
    retValue
  }

  def getChildLabels(label: String): util.List[String] = {
    val node = this.getNode(label)
    node.getChildLabels
  }

  def getParentLabels(label: String): util.List[String] = {
    val node = this.getNode(label)
    node.getParentLabels
  }

  @throws(classOf[CloneNotSupportedException])
  override def clone: AnyRef = {
    val retValue = super.clone
    retValue
  }

  def isConnected(label: String): Boolean = {
    val node = this.getNode(label)
    val retValue = node.isConnected
    retValue
  }

  def getSuccessorLabels(label: String): util.List[String] = {
    val node: Node = this.getNode(label)
    var retValue: util.List[String] = new util.ArrayList[String]
    if (node.isLeaf) {
      retValue = new util.ArrayList[String](1)
      retValue.add(label)
    }
    else {
      retValue = TopologicalSorter.sort(node)
    }
    retValue
  }
}