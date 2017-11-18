package models.currentConfig

import scala.collection.mutable.Map 
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.json.common.JsonStep
import models.json.currentConfig.JsonCurrentConfigIn
import models.json.currentConfig.JsonCurrentConfigOut
import models.wrapper.currentConfig.CurrentConfigIn
import models.wrapper.currentConfig.CurrentConfigOut
import models.wrapper.common.Step
import models.wrapper.common.Component
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 */
object CurrentConfig {
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * Initialisierung des CurentConfig
   * Es wird nur erster Schritt ohne seine Komponente hinzugefuegt
   * 
   * @param FirstStep
   * 
   * @return
   */
  def init(firstStep: Step) = {
    
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * Neu ausgewaelte Komponente wird zu seinem Schritt eingefuegt
   * 
   * @param Step
   * 
   * @return
   */
  def addComponent = {
    // es wird geprueft ob der Step schon angelegt war
    // Die erste Komponente in dem Schritt wird ohne Pruefung des SelectionCriterium hinzugefuegt
    // Die Abhaengigkeiten werden weiterhin jedes mal geprueft
    ???
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * Wenn alle Komponente des Schrittes ausgewaelt und bearbeitet waren,
   * wird ein neuen Schritt geladen.
   * 
   * Dieser neuer Schritt wird in die CurrentConfig hinzugefuegt
   * 
   * @param Step
   * 
   * @return
   */
  def addNextStep = {
    ???
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return CurrentConfig
   */
  def getCurrentConfig = ???
  
  
  private val currentConfigs: Map[String, List[Step]] = Map.empty
  
  val currentConfig: CurrentConfig = new CurrentConfig
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param String, Step
   * 
   * @return Unit
   */
  def setCurrentConfig(step: Step): Unit = {
    
    
    
    
    
    
    
    
//    val currentConfigSteps: List[Step] = currentConfigs.get(clientId) match {
//      case Some(step) => currentConfigs.get(clientId).get
//      case None => currentConfig.setCurrentConfig(step, List[Step]())
////      case _ => println("Fehler bei CurrentConfig")
//    }
//    
//    val indexOfSelectedStep = currentConfigSteps.indexWhere(s => s.stepId == step.stepId)
//    
//    if(indexOfSelectedStep == -1) {
//      currentConfigs += (clientId -> (currentConfigSteps :+ step))
//    }else {
//       val countOfStepToDelete = currentConfigSteps.size - indexOfSelectedStep
//       val steps = currentConfigSteps.dropRight(countOfStepToDelete)
//       currentConfigs. += (clientId -> (steps :+ step))
//    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param CurrentConfigIn
   * 
   * @return CurrentConfigOut
   */
  def getCurrentConfig(currentConfigIn: CurrentConfigIn): CurrentConfigOut = {
    
    val currentConfigSteps: List[Step] = currentConfigs.get(currentConfigIn.clientId) match {
      case Some(step) => step
      case None => List[Step]()
    }
    
    CurrentConfigOut(
        currentConfigSteps
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return Map[String, List[Step]]
   */
  def getCuttentConfig: Map[String, List[Step]] = {
    currentConfigs
  }
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 */
class CurrentConfig {
  
  var firstStep: Option[StepCurrentConfig] = None
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig, Component
   * 
   * @return Unit
   */
  private def addComponent(step: StepCurrentConfig, component: Component): Unit = {
    
    val currentStep: Option[StepCurrentConfig] = getStep(step)
    
    val currentComponents: List[Component] = currentStep.get.components
    
    currentStep.get.components = currentComponents.::(component)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param Option[StepCurrentConfig], Option[StepCurrentConfig]
   * 
   * @return Unit
   */
  private def addStep(nextStep: Option[StepCurrentConfig], fatherStep: Option[StepCurrentConfig]): Unit = {
    fatherStep match {
      case Some(fatherStep) => fatherStep.nextStep = nextStep
      case None => firstStep = nextStep
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return Option[StepCurrentConfig]
   */
  private def getCurrentConfig: Option[StepCurrentConfig] = this.firstStep
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig
   * 
   * @return Option[StepCurrentConfig]
   */
  private def getStep(stepB: StepCurrentConfig): Option[StepCurrentConfig] = {
    
    val stepA = this.firstStep
    getStepRecursive(stepA, stepB)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param Option[StepCurrentConfig], Option[StepCurrentConfig]
   * 
   * @return Option[StepCurrentConfig]
   */
  private def getStepRecursive(stepA: Option[StepCurrentConfig], stepB: StepCurrentConfig): Option[StepCurrentConfig] = {
    if(stepA.get.stepId == stepB.stepId){
      stepA
    }else{
      getStepRecursive(stepA.get.nextStep, stepB)
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return Unit
   */
  def printCurrentConfig: Unit = {
    Logger.info("Current Configuration")
    getNextStep(this.firstStep)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param Option[StepCurrentConfig]
   * 
   * @return Unit
   */
  private def getNextStep(step: Option[StepCurrentConfig]): Unit = {
     
    step.get.nextStep match {
      case Some(nextStep) => {
        Logger.info(step.get.stepId)
        step.get.components.reverse foreach {component => Logger.info("====" + component.componentId)}
        getNextStep(step.get.nextStep)
      }
      case None => {
        Logger.info(step.get.stepId)
        step.get.components.reverse foreach {component => Logger.info("====" + component.componentId)}
      }
    }
  }
}
