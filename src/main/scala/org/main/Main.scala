package org.main

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings
import org.test._
import org.admin._
import org.client.Client
import org.configTree.component.SelectedComponent

/**
  * 
  */
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")
    println("Run Test Scenarios")
    
    new TestScenario1().scenario1
    
    new TestScenario2().scenario2
    
    new TestScenario3().scenario3
    
    new TestScenario4().scenario4
    
    new TestScenario5().scenario5
    
    new TestScenario6().scenario6
    
    new TestScenario7().scenario7
    
    new TestScenario8().scenario8
    
    println("END")
  }
}


//    val currentConfig = new TestMutableCurrentConfig
////    
//    currentConfig.mutableCurrentConfig
//    val selCrit = new TestSelectionCriterium
    
//    selCrit.selectCriterium
    
//    selCrit.getStepOfComponents
    
//    selCrit.getNextStep