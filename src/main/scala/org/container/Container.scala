package org.container

import org.configTree.step._

import scala.collection.mutable.ListBuffer
import org.configSettings.ConfigSettings

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
                       currentConfig: ListBuffer[CurrentConfigStep] = ListBuffer.empty,
                       currentConfig_1: List[List[CurrentConfigStep]]
                     )