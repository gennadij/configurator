package org.configMgr

import org.configTree.Step

import scala.collection.mutable.ListBuffer
case class CurrentConfig(steps: ListBuffer[Step])