package com.analytictech

import java.util
import java.util.Collections
import scala.util.control.Breaks._
import scala.collection.JavaConversions._


object CycleDetector {
  private val NOT_VISITED: Integer = 0
  private val VISITING: Integer = 1
  private val VISITED: Integer = 2

  def hasCycle(graph: DAG): util.List[String] = {
    val nodes = graph.getNodes
    val nodeStateMap = new util.HashMap[Node, Integer]
    var retValue: util.List[String] = null
    for (node <- nodes.toList) {
      if (isNotVisited(node, nodeStateMap)) {
        retValue = introducesCycle(node, nodeStateMap)
        if (retValue != null) {
          break()
        }
      }
    }
    retValue
  }

  def introducesCycle(node: Node, nodeStateMap: util.Map[Node, Integer]): util.List[String] = {
    val cycleStack = new util.LinkedList[String]
    val hasCycle: Boolean = dfsVisit(node, cycleStack, nodeStateMap)
    if (hasCycle) {
      val label: String = cycleStack.getFirst
      val pos: Int = cycleStack.lastIndexOf(label)
      val cycle = cycleStack.subList(0, pos + 1)
      Collections.reverse(cycle)
      cycle
    }
    else {
      null
    }
  }

  def introducesCycle(node: Node): util.List[String] = {
    val nodeStateMap = new util.HashMap[Node, Integer]
    introducesCycle(node, nodeStateMap)
  }

  private def isNotVisited(node: Node, nodeStateMap: util.Map[Node, Integer]): Boolean = {
    val state: Integer = nodeStateMap.get(node)
    state == null || (NOT_VISITED == state)
  }

  private def isVisiting(node: Node, nodeStateMap: util.Map[Node, Integer]): Boolean = {
    val state: Integer = nodeStateMap.get(node)
    VISITING == state
  }

  private def dfsVisit(node: Node, cycle: util.LinkedList[String], nodeStateMap: util.Map[Node, Integer]): Boolean = {
    cycle.addFirst(node.label)
    nodeStateMap.put(node, VISITING)
    val i$ = node.getChildren.iterator
    while (i$.hasNext) {
      val v: Node = i$.next
      if (isNotVisited(v, nodeStateMap)) {
        val hasCycle: Boolean = dfsVisit(v, cycle, nodeStateMap)
        if (hasCycle) {
          return true
        }
      }
      else if (isVisiting(v, nodeStateMap)) {
        cycle.addFirst(v.label)
        return true
      }
    }
    nodeStateMap.put(node, VISITED)
    cycle.removeFirst()
    false
  }
}