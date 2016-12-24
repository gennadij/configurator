package org.container

import org.configTree.step._

import scala.collection.mutable.ListBuffer

/**
  * Created by gennadi on 29.04.16.
  */

//object Container{
//  def initContainer(xml: String) = {
//    new Container(ConfigSettings.configSettings, ListBuffer.empty)
//  }
//}


case class Container (
                       configSettings: Seq[Step],
                       currentConfig: ListBuffer[CurrentConfigStep] = ListBuffer.empty
                     )