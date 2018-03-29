package tests.bookProgramingInScala

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 28.03.2018
 */
abstract class Simulation {
  type Action = () => Unit

  case class WorkItem(time: Int, action: Action)

  private var curtime = 0
  def currentTime: Int = curtime

  private var agenda: List[WorkItem] = List()

  private def insert(ag: List[WorkItem],
      item: WorkItem): List[WorkItem] = {

    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  def afterDelay(delay: Int)(block: => Unit) {
    val item = WorkItem(currentTime + delay, () => block)
    agenda = insert(agenda, item)
  }

  private def next() {
    (agenda: @unchecked) match {
      case item :: rest => 
        agenda = rest 
        curtime = item.time
        item.action()
    }
  }

  def run() {
    afterDelay(0) {
      println("*** simulation started, time = "+
          currentTime +" ***")
    }
    while (!agenda.isEmpty) next()
  }
}