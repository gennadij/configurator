package org.container

import org.configTree.step._

import scala.collection.mutable.ListBuffer

/**
  * Created by gennadi on 29.04.16.
  */

case class Container (
                       configSettings: Seq[AbstractStep],
                       currentConfig: ListBuffer[CurrentConfigStep] = ListBuffer.empty
                     ){
  val immutableCurrentConfig: Seq[AbstractStep] = Seq.empty
}