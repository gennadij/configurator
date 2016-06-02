package org.main

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings
import org.configTree.step.ErrorStep
import org.configTree.step.FinalStep
import org.configTree.component.SelectedComponent

class TestMutableCurrentConfig {
  
  def mutableCurrentConfig = {

    // first step
    println("##################################################################")

    val firstStep = ConfigMgr.startConfig

    require(firstStep.id == "001", "id must be 001")
    
    require(ConfigMgr.currentConfig.size == 0, "CurrentConfig size is 0")
    
    println("Components for step: " + firstStep.id)
    for(components <- firstStep.components){
      println(components.nameToShow)
    }
    
    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // twice step
    println("##################################################################")

    val selected001001 = Set(new SelectedComponent("001001", ""))

    println("Selected Component: " + selected001001.head.id)
    
    val twiceStep = ConfigMgr.getNextStep(selected001001)
    
    if(twiceStep.isInstanceOf[ErrorStep]) println("Error -> " + twiceStep.errorMessage)

    require(ConfigMgr.currentConfig.size == 1, "CurrentConfig size is 1")
    
    println("Components for step: " + twiceStep.id)
    for(components <- twiceStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // third Step
    println("##################################################################")

    val selected002001 = new SelectedComponent("002001", "")
    println("Selected Component: " + selected002001.id)
    
    val thirdStep = ConfigMgr.getNextStep(Set(selected002001))
    
    if(thirdStep.isInstanceOf[ErrorStep]) println("Error -> " + thirdStep.errorMessage)
    
    require(ConfigMgr.currentConfig.size == 2, "CurrentConfig size is 2")

    println("Components for step: " + thirdStep.nameToShow)
    
    for(components <- thirdStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // fourth Step
    println("##################################################################")

    val selected003001 = new SelectedComponent("003001","")
    val selected003002 = new SelectedComponent("003002","")
    println("Selected Component: " + selected003001.id)
    
    val fourthStep = ConfigMgr.getNextStep(Set(selected003001, selected003002))

    if(fourthStep.isInstanceOf[ErrorStep]) println("Error -> " + fourthStep.errorMessage)
    
    require(ConfigMgr.currentConfig.size == 3, "CurrentConfig size is 3")
    
    println("Components for step: " + fourthStep.nameToShow)
    for(components <- fourthStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // fifth Step
    println("##################################################################")

    val selected004001 = new SelectedComponent("004001","")
    println("Selected Component: " + selected004001.id)
    
    val fifthStep = ConfigMgr.getNextStep(Set(selected004001))

    if(fifthStep.isInstanceOf[ErrorStep]) println("Error -> " + fifthStep.errorMessage)
    
    require(ConfigMgr.currentConfig.size == 4, "CurrentConfig size is 4")
    
    println("Components for step: " + fifthStep.nameToShow)
    for(components <- fifthStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // seventh Step
    println("##################################################################")

    val selected005001 = new SelectedComponent("005001","")
    println("Selected Component: " + selected005001.id)
    
    val seventhStep = ConfigMgr.getNextStep(Set(selected005001))

    if(seventhStep.isInstanceOf[ErrorStep]) println("Error -> " + seventhStep.errorMessage)
    if(seventhStep.isInstanceOf[FinalStep]) println("Final -> " + seventhStep.nameToShow)
    
    require(ConfigMgr.currentConfig.size == 5, "CurrentConfig size is 5")
    
    println("Components for step: " + seventhStep.nameToShow)
    
    for(components <- seventhStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }
    
        // eighth Step
    println("##################################################################")

    val selected007001 = new SelectedComponent("007001","")
    println("Selected Component: " + selected007001)
    
    val eighthStep = ConfigMgr.getNextStep(Set(selected007001))

    if(eighthStep.isInstanceOf[ErrorStep]) println("Error -> " + eighthStep.errorMessage)
    if(eighthStep.isInstanceOf[FinalStep]) println("Final -> " + eighthStep.nameToShow)
    
    require(ConfigMgr.currentConfig.size == 5, "CurrentConfig size is 5")
    
    println("Components for step: " + eighthStep.nameToShow)
    
//    for(components <- eighthStep.components){
//      println(components.nameToShow)
//    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // ninth Step
    println("##################################################################")

    val selected002002 = new SelectedComponent("002002","")

    val ninthStep = ConfigMgr.getNextStep(Set(selected002002))
    
    if(ninthStep.isInstanceOf[ErrorStep]) println("Error -> " + ninthStep.errorMessage)
    
    require(ConfigMgr.currentConfig.size == 2, "CurrentConfig size is 2")

    println("Selected Component: " + selected002002)

    println("Components for step: " + ninthStep.nameToShow)
    for(components <- ninthStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }
  }
}


//  val currentConfig = ListBuffer(
//                  new DefaultStep("001","step 001",List(new NextStep("001001","002"), new NextStep("001002","002"), 
//                  new NextStep("001003","003")),"first",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                  List(
//                      new ImmutableComponent("001003","immutable","component 001003")
//                      ))
//                  ,
//                 new DefaultStep("003","step 003",List(new NextStep("003001","004"), 
//                 new NextStep("003002","004"), new NextStep("003003","004"), 
//                 new NextStep("003004","004")),"default",new SelectionCriterium("1","1"),
//                 new Source("xml","",""), 
//                   List(
//                      new ImmutableComponent("003003","immutable","component 003003")
//                      ))) 
//             new DefaultStep("004","step 004",List(new NextStep("004001","005"),
//                 new NextStep("004002","005"), new NextStep("004003","006")),
//                 "default",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                 List(
//                      new StaticComponent("004003","immutable","component 004003")
//                      )), 
//             new DefaultStep("006","step 006", List(new NextStep("006001","000"), new NextStep("006002","000"), 
//                 new NextStep("006003","000")),"last",new SelectionCriterium("1","1"),new Source("xml","",""),
//                 List(
//                      new StaticComponent("006001","immutable","component 006001")
//                      ))
//              )