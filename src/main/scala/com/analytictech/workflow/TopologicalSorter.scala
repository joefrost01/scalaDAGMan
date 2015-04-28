package com.analytictech.workflow

import java.util


object TopologicalSorter {
  private val NOT_VISTITED: Integer = 0
  private val VISITING: Integer = 1
  private val VISITED: Integer = 2

  def sort(graph: Workflow): util.List[String] = {
    dfs(graph)
  }

  def sort(node: Step): util.List[String] = {
    val retValue: util.LinkedList[String] = new util.LinkedList[String]
    dfsVisit(node, new util.HashMap[Step, Integer], retValue)
    retValue
  }

  private def dfs(graph: Workflow): util.List[String] = {
    val retValue: util.LinkedList[String] = new util.LinkedList[String]
    val nodeStateMap: util.HashMap[Step, Integer] = new util.HashMap[Step, Integer]
    for (o <- graph.getSteps) {
      val node: Step = o.asInstanceOf[Step]
      if (isNotVisited(node, nodeStateMap)) {
        dfsVisit(node, nodeStateMap, retValue)
      }
    }
    retValue
  }

  private def isNotVisited(node: Step, nodeStateMap: util.Map[Step, Integer]): Boolean = {
    val state: Integer = nodeStateMap.get(node)
    state == null || (NOT_VISTITED == state)
  }

  private def dfsVisit(node: Step, nodeStateMap: util.Map[Step, Integer], list: util.List[String]) {
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
