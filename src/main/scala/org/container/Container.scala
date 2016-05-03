package org.container

import org.configTree.Step

import scala.collection.mutable.ListBuffer

/**
  * Created by gennadi on 29.04.16.
  */
//object Container {
//  def apply(
//             configSettings: Seq[Step],
//             currentConfig: ListBuffer[Step] = ListBuffer.empty
//           ): Container = new Container(configSettings, currentConfig)
//}

case class Container (
                       configSettings: Seq[Step],
                       currentConfig: ListBuffer[Step] = ListBuffer.empty
                     )