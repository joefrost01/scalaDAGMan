package com.analytictech

import java.util

object TopologicalSorter {
  private val NOT_VISTITED: Integer = 0
  private val VISITING: Integer = 1
  private val VISITED: Integer = 2

  def sort(graph: DAG): util.List[String] = {
    dfs(graph)
  }

  def sort(node: Node): util.List[String] = {
    val retValue: util.LinkedList[String] = new util.LinkedList[String]
    dfsVisit(node, new util.HashMap[Node, Integer], retValue)
    retValue
  }

  private def dfs(graph: DAG): util.List[String] = {
    val retValue: util.LinkedList[String] = new util.LinkedList[String]
    val nodeStateMap: util.HashMap[Node, Integer] = new util.HashMap[Node, Integer]
    import scala.collection.JavaConversions._
    for (o <- graph.getNodes) {
      val node: Node = o.asInstanceOf[Node]
      if (isNotVisited(node, nodeStateMap)) {
        dfsVisit(node, nodeStateMap, retValue)
      }
    }
    retValue
  }

  private def isNotVisited(node: Node, nodeStateMap: util.Map[Node, Integer]): Boolean = {
    val state: Integer = nodeStateMap.get(node)
    state == null || (NOT_VISTITED == state)
  }

  private def dfsVisit(node: Node, nodeStateMap: util.Map[Node, Integer], list: util.List[String]) {
    nodeStateMap.put(node, VISITING)
    import scala.collection.JavaConversions._
    for (v <- node.getChildren) {
      if (isNotVisited(v, nodeStateMap)) {
        dfsVisit(v, nodeStateMap, list)
      }
    }
    nodeStateMap.put(node, VISITED)
    list.add(node.label)
  }
}
