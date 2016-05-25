package org.main

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings

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

    val selected001001 = "001001"

    println("Selected Component: " + selected001001)
    
    val twiceStep = ConfigMgr.getNextStep(Set(selected001001))

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

    val selected002001 = "002001"
    println("Selected Component: " + selected002001)
    
    val thirdStep = ConfigMgr.getNextStep(Set(selected002001))
    
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

    val selected003001 = "003001"
    println("Selected Component: " + selected003001)
    
    val fourthStep = ConfigMgr.getNextStep(Set(selected003001))

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

    val selected004001 = "004001"
    println("Selected Component: " + selected004001)
    
    val fifthStep = ConfigMgr.getNextStep(Set(selected004001))

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

    val selected005001 = "005001"
    println("Selected Component: " + selected005001)
    
    val seventhStep = ConfigMgr.getNextStep(Set(selected005001))

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

    val selected002002 = "002002"

    val eighthStep = ConfigMgr.getNextStep(Set(selected002002))
    
    require(ConfigMgr.currentConfig.size == 2, "CurrentConfig size is 2")

    println("Selected Component: " + selected002002)

    println("Components for step: " + eighthStep.nameToShow)
    for(components <- eighthStep.components){
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