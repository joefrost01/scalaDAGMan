package com.analytictech.workflow

import java.util
import java.util.Collections

import scala.util.control.Breaks._


object CycleDetector {
  private val NOT_VISITED: Integer = 0
  private val VISITING: Integer = 1
  private val VISITED: Integer = 2

  def hasCycle(graph: Workflow): util.List[String] = {
    val nodes = graph.getSteps
    val nodeStateMap = new util.HashMap[Step, Integer]
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

  def introducesCycle(node: Step, nodeStateMap: util.Map[Step, Integer]): util.List[String] = {
    val cycleStack = new util.LinkedList[String]
    val hasCycle = dfsVisit(node, cycleStack, nodeStateMap)
    if (hasCycle) {
      val label = cycleStack.getFirst
      val pos = cycleStack.lastIndexOf(label)
      val cycle = cycleStack.subList(0, pos + 1)
      Collections.reverse(cycle)
      cycle
    }
    else {
      null
    }
  }

  def introducesCycle(node: Step): util.List[String] = {
    val nodeStateMap = new util.HashMap[Step, Integer]
    introducesCycle(node, nodeStateMap)
  }

  private def isNotVisited(node: Step, nodeStateMap: util.Map[Step, Integer]): Boolean = {
    val state = nodeStateMap.get(node)
    state == null || (NOT_VISITED == state)
  }

  private def isVisiting(node: Step, nodeStateMap: util.Map[Step, Integer]): Boolean = {
    val state = nodeStateMap.get(node)
    VISITING == state
  }

  private def dfsVisit(node: Step, cycle: util.LinkedList[String], nodeStateMap: util.Map[Step, Integer]): Boolean = {
    cycle.addFirst(node.label)
    nodeStateMap.put(node, VISITING)
    for (child <- node.getChildren) {
      if (isNotVisited(child, nodeStateMap)) {
        if (dfsVisit(child, cycle, nodeStateMap)) {
          return true
        }
      }
      else if (isVisiting(child, nodeStateMap)) {
        cycle.addFirst(child.label)
        return true
      }
    }
    nodeStateMap.put(node, VISITED)
    cycle.removeFirst()
    false
  }
}