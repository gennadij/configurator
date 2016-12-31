package org.main

import org.configMgr.ConfigMgr
import org.configTree.component.SelectedComponent
import org.persistence.db.orientdb.TestOrientdb

/**
  * 
  */
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")
    println("Run Test Scenarios")
    
    val list = List("test6", "test6", "test6", "test")
    
    println(checkElem(list))
    
//    new TestScenario1().scenario1
    
//    new TestScenario2().scenario2
    
//    new TestScenario3().scenario3
    
//    new TestScenario4().scenario4
    
//    new TestScenario5().scenario5
    
//    new TestScenario8().scenario8

//    new TestScenario9().scenario9_1
//    new TestScenario9().scenario9_2()
    
    println("END")
  }
  def checkElem(list: Seq[String]) = {
    list match {
      case x :: rest => {
        println(x + " " + rest)
        rest forall (_ == x)}
    }
  }
}



//    val currentConfig = new TestMutableCurrentConfig
////    
//    currentConfig.mutableCurrentConfig
//    val selCrit = new TestSelectionCriterium
    
//    selCrit.selectCriterium
    
//    selCrit.getStepOfComponents
    
//    selCrit.getNextStep