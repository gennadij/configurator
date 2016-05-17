package org.main

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings
import org.test._

/**
  * TODO for whole project
  * - definition von dem letztem Step
  * - multichoose bei der Auswahl der Componenten
  * - pruefe ob mehrer Stepps als firstStep definiert sind
  * - definition von dem variablen Components (Slide, variable Massen)
  * - in currentCinfigutration unter Step nur die ausgewählte Komponente speichern
  * - Logger einrichten
  * - letzte Step wird nicht in der CurrentConfig hinzugefügt
  * - Prüfung einbauen, wenn Komponent, der nicht in Step exestiert, ausgewählt
  * - SimpleComponent (ImmutableComponent) nur einen Component in einem Step ausgewählt werden kann
  * - MutableComponent nur einen Component mit einer Veränderbarer Wert ausgewählt werden kann
  * - MultiComponent kombeniert Mutable und ImmutableComponent in einem in einem Steps
  */
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")
   
    //TODO create Factory object
    val configMgr = new ConfigMgr

    val container = ConfigSettings.configSettings
    

    // first step
    println("##################################################################")

    val firstStep = configMgr.startConfig

    println("Components for step: " + firstStep.id)
    for(components <- firstStep.components){
      println(components.nameToShow)
    }

    val selected001001 = "001001"

    println("Selected Component: " + selected001001)

//    configMgr.addStepToCurrentConfig(selected001001)

//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // twice step
    println("##################################################################")

    val twiceStep = configMgr.getNextStep(selected001001)

    println("Components for step: " + twiceStep.id)
    for(components <- twiceStep.components){
      println(components.nameToShow)
    }

    val selected002001 = "002001"
    println("Selected Component: " + selected002001)

//    configMgr.addStepToCurrentConfig(selected002001)

//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // third Step
    println("##################################################################")

    val thirdStep = configMgr.getNextStep(selected002001)

    println("Components for step: " + thirdStep.nameToShow)
    for(components <- thirdStep.components){
      println(components.nameToShow)
    }

    val selected003001 = "003001"
    println("Selected Component: " + selected003001)

//    configMgr.addStepToCurrentConfig(selected003001)
//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // fourth Step
    println("##################################################################")

    val fourthStep = configMgr.getNextStep(selected003001)

    println("Components for step: " + fourthStep.nameToShow)
    for(components <- fourthStep.components){
      println(components.nameToShow)
    }

    val selected004001 = "004001"
    println("Selected Component: " + selected004001)

//    configMgr.addStepToCurrentConfig(selected004001)
//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // fifth Step
    println("##################################################################")

    val fifthStep = configMgr.getNextStep(selected004001)

    println("Components for step: " + fifthStep.nameToShow)
    for(components <- fifthStep.components){
      println(components.nameToShow)
    }

    val selected005001 = "005001"
    println("Selected Component: " + selected005001)

//    configMgr.addStepToCurrentConfig(selected005001)
//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // seventh Step
    println("##################################################################")

    val seventhStep = configMgr.getNextStep(selected005001)

    println("Components for step: " + seventhStep.nameToShow)
    for(components <- seventhStep.components){
      println(components.nameToShow)
    }

    val selected007001 = "007001"
    println("Selected Component: " + selected007001)

//    configMgr.addStepToCurrentConfig(container, selected007001)
//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }

    // eighth Step
    println("##################################################################")

    val selected002002 = "002002"

    val eighthStep = configMgr.getNextStep(selected002002)

    println("Selected Component: " + selected002002)

//    configMgr.addStepToCurrentConfig(selected002002)

//    println("Aktuelle Step: " + configMgr.getCurrentStep(container))
//
    println("Components for step: " + eighthStep.nameToShow)
    for(components <- eighthStep.components){
      println(components.nameToShow)
    }

    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println(comp.nameToShow)
      }
    }



    def nextStep(enter: String): Unit = {
//      val  step = configMgr.getNextStep(container, enter)

//      if(step._1 == null){
//        container.currentConfig += step._2
//        println("Aktuelle Konfiguration")
//        for(step <- container.currentConfig){
//          println(step.nameToShow)
//          for(comp <- step.components){
//            println("Components: " + comp.nameToShow)
//          }
//        }
//      }
//      else {
//        container.currentConfig += step._2
//        println("Next Step: " + step._1.nameToShow)
//        for(components <- step._1.components){
//          println("Components: " + components.nameToShow)
//        }
//      }
    }

//    while(true){
//      println("enter a id for component: ")
//
//      val enter = scala.io.StdIn.readLine()
//
//      enter match {
//        case "q" => System.exit(0)
//        case a if a.length == 6 => nextStep(enter)
//        case _ => println("no enter")
//      }
//    }
  }
}