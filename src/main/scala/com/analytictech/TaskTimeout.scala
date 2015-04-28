package com.analytictech

import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

class TaskTimeout(data: String, delay: Long) extends Delayed {

  val startTime: Long = System.currentTimeMillis + delay

  def getDelay(unit: TimeUnit): Long = {
    val diff = startTime - System.currentTimeMillis
    unit.convert(diff, TimeUnit.MILLISECONDS)
  }

  def compareTo(o: Delayed): Int = {
    if (this.startTime < o.asInstanceOf[TaskTimeout].startTime) {
      return -1
    }
    if (this.startTime > o.asInstanceOf[TaskTimeout].startTime) {
      return 1
    }
    0
  }

  override def toString: String = {
    "{" + "data='" + data + '\'' + ", startTime=" + startTime + '}'
  }
}