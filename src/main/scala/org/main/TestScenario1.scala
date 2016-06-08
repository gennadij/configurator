package org.main

import org.configMgr._
import org.configTree.component._
import org.configTree.step._

class TestScenario1 {
  def scenario1 = {

    // first step
    println("############################## SCENARIO 1 #######################")

    ConfigMgr.currentConfig.clear()
    val firstStep = ConfigMgr.startConfig

//    println("Components for step: " + firstStep.id)
//    for(component <- firstStep.components){
//      println(component.nameToShow)
//    }
    
    require(firstStep.isInstanceOf[FirstStep] == true)
    require(firstStep.id == "001", "id must be 001")
    require(firstStep.selectionCriterium.max == "1")
    require(firstStep.selectionCriterium.min == "1")
    require(firstStep.nextStep(0).step == "002")
    require(firstStep.nextStep(0).byComponent == "001001")
    require(firstStep.nextStep(1).step == "002")
    require(firstStep.nextStep(1).byComponent == "001002")
    require(firstStep.nextStep(2).step == "003")
    require(firstStep.nextStep(2).byComponent == "001003")
    
    require(firstStep.components(0).id == "001001")
    require(firstStep.components(0).nameToShow == "component 001001")
    require(firstStep.components(0).isInstanceOf[ImmutableComponent] == true)
    
    require(firstStep.components(1).id == "001002")
    require(firstStep.components(1).nameToShow == "component 001002")
    require(firstStep.components(1).isInstanceOf[ImmutableComponent] == true)
    
    require(firstStep.components(2).id == "001003")
    require(firstStep.components(2).nameToShow == "component 001003")
    require(firstStep.components(2).isInstanceOf[ImmutableComponent] == true)
    
    
    
    
    require(ConfigMgr.currentConfig.size == 0, "CurrentConfig size is 0")
    
//    println("Aktuelle Konfiguration")
//    for(step <- ConfigMgr.currentConfig){
//      println(step.nameToShow)
//      for(comp <- step.components){
//        if(comp.isInstanceOf[MutableComponent]){
//          println(comp.nameToShow)
//          println(comp.value)
//        }else{
//        	println(comp.nameToShow)
//        }
//      }
//    }

    // twice step
    println("##################################################################")

    val selected001001 = Set(new SelectedComponent("001001"))

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
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }

    // third Step
    println("##################################################################")

    val selected002001 = new SelectedComponent("002001")
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
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }

    // fourth Step
    println("##################################################################")

    val selected003001 = new SelectedComponent("003003", 1)
    val selected003002 = new SelectedComponent("003002", 1)
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
        if(comp.isInstanceOf[CurrentConfigMutableComponent]){
          println(comp.nameToShow)
          println("value: " + comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }

    // fifth Step
    println("##################################################################")

    val selected004001 = new SelectedComponent("004001")
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
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }

    // seventh Step
    println("##################################################################")

    val selected005001 = new SelectedComponent("005001")
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
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }
    
        // eighth Step
    println("##################################################################")

    val selected007001 = new SelectedComponent("007001")
    println("Selected Component: " + selected007001)
    
    val eighthStep = ConfigMgr.getNextStep(Set(selected007001))

    if(eighthStep.isInstanceOf[ErrorStep]) println("Error -> " + eighthStep.errorMessage)
    if(eighthStep.isInstanceOf[FinalStep]) println("Final -> " + eighthStep.nameToShow)
    
    require(ConfigMgr.currentConfig.size == 6, "CurrentConfig size is 6")
    
    println("Components for step: " + eighthStep.nameToShow)
    
//    for(components <- eighthStep.components){
//      println(components.nameToShow)
//    }

    println("Aktuelle Konfiguration")
    for(step <- ConfigMgr.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }

    // ninth Step
    println("##################################################################")

    val selected002002 = new SelectedComponent("002002")

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
        if(comp.isInstanceOf[MutableComponent]){
          println(comp.nameToShow)
          println(comp.value)
        }else{
        	println(comp.nameToShow)
        }
      }
    }
  }
}