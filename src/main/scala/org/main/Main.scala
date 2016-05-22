package org.main

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings
import org.test._

/**
  * 
  */
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")
    
//    val currentConfig = new TestMutableCurrentConfig
//    
//    currentConfig.mutableCurrentConfig
    val selCrit = new TestSelectionCriterium
    
//    selCrit.selectCriterium
    
    selCrit.getStepOfComponents
    
    println("END")
  }
}