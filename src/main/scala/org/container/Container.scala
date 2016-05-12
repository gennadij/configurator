package org.container

import org.configTree.st

import scala.collection.mutable.ListBuffer

/**
  * Created by gennadi on 29.04.16.
  */

case class Container (
                       configSettings: Seq[Step],
                       currentConfig: ListBuffer[Step] = ListBuffer.empty
                     ) {
  var configSettingsForStatic: Seq[AbstarctStep] =  Seq.empty
}