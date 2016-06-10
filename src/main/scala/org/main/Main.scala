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
////    
//    currentConfig.mutableCurrentConfig
    
    val scenario1 = new TestScenario1
    
    scenario1.scenario1
    
    new TestScenario2().scenario2
//    val selCrit = new TestSelectionCriterium
    
//    selCrit.selectCriterium
    
//    selCrit.getStepOfComponents
    
//    selCrit.getNextStep

    
    println("END")
  }
}