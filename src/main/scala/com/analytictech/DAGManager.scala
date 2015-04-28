package com.analytictech

import java.util.concurrent.{BlockingQueue, DelayQueue}
import org.json4s._
import org.json4s.jackson.JsonMethods._


object DAGManager {

  def main(args: Array[String]) {

    implicit val formats = DefaultFormats

    val expiryQueue: BlockingQueue[TaskTimeout] = new DelayQueue()
    val taskTimeout = new TaskTimeout("test", 1000)
    expiryQueue.put(taskTimeout)
    val json =  """
                  |{
                  |  "nodes": [
                  |    {
                  |      "label": "Init",
                  |      "task": "StartFlow",
                  |      "params": "list of my parameters"
                  |    },
                  |    {
                  |      "label": "MarketDataCaching",
                  |      "task": "CacheMarketData",
                  |      "params": "curve=LIBOR"
                  |    },
                  |    {
                  |      "label": "TradeCaching",
                  |      "task": "CacheTrades",
                  |      "params": "type=BONDS"
                  |    },
                  |    {
                  |      "label": "Calculation",
                  |      "task": "calculate",
                  |      "params": ""
                  |    }
                  |  ],
                  |  "edges": [
                  |    {
                  |      "from": "Init",
                  |      "to": "MarketDataCaching"
                  |    },
                  |    {
                  |      "from": "Init",
                  |      "to": "TradeCaching"
                  |    },
                  |    {
                  |      "from": "MarketDataCaching",
                  |      "to": "Calculation"
                  |    },
                  |    {
                  |      "from": "TradeCaching",
                  |      "to": "Calculation"
                  |    }
                  |  ]
                  |}
                           """.stripMargin

    //val dag = parse(json).extract[DAG]
    val nodes = parse(json).extract[NodeList]
    val edges = parse(json).extract[EdgeList]
    println(nodes)
    println(edges)

    val dag = new DAG(nodes,edges)
    println(dag)
    println("done")
  }

  case class NodeList(nodes: List[Node]) {}
  case class EdgeList(edges: List[Edge]) {}

  def JSONtoEdge(jsPath: String): Edge = {
    new Edge("from","to")
  }
}