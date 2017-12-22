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
  
  
  val currentConfig: CurrentConfig = new CurrentConfig
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * Neu ausgewaelte Komponente wird zu seinem Schritt eingefuegt
   * 
   * @param 
   * 
   * @return
   */
  def addComponent(step: StepCurrentConfig, component: Component): Unit = {
    // es wird geprueft ob der Step schon angelegt war
    // Die erste Komponente in dem Schritt wird ohne Pruefung des SelectionCriterium hinzugefuegt
    // Die Abhaengigkeiten werden weiterhin jedes mal geprueft
    currentConfig.addComponent(step, component)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * Dieser neuer Schritt wird in die CurrentConfig hinzugefuegt
   * 
   * @param 
   * 
   * @return
   */
  def addStep(nextStep: Option[StepCurrentConfig], fatherStep: Option[StepCurrentConfig]): Unit = {
    currentConfig.addStep(nextStep, fatherStep)
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
  def getCurrentConfig = currentConfig.getCurrentConfig
  
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
    currentConfig.printCurrentConfig
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
  def getCurrentStep(stepId: String): Option[StepCurrentConfig] = {
    currentConfig.getCurrentStep(stepId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return StepCurrentConfig
   */
  def getLastStep: StepCurrentConfig = {
    currentConfig.getLastStep
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
    
    val currentStep: Option[StepCurrentConfig] = getStep(step.stepId)
    
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
  private def addStep(currentStep: Option[StepCurrentConfig], fatherStep: Option[StepCurrentConfig]): Unit = {
    fatherStep match {
      case Some(fatherStep) => fatherStep.nextStep = currentStep
      case None => firstStep = currentStep
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
  private def getCurrentStep(stepId: String): Option[StepCurrentConfig] = {
    getStep(stepId)
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
   * @param 
   * 
   * @return StepCurrentConfig
   */
  private def getLastStep: StepCurrentConfig = {
    val firstStep = this.firstStep.get
    getLastStepRecursive(firstStep)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig
   * 
   * @return StepCurrentConfig
   */
  private def getLastStepRecursive(step: StepCurrentConfig): StepCurrentConfig = {
    
    step.nextStep match {
      case Some(step) => getLastStepRecursive(step)
      case None => step
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig
   * 
   * @return Option[StepCurrentConfig]
   */
  private def getStep(stepId: String): Option[StepCurrentConfig] = {
    
    val stepA = this.firstStep
    getStepRecursive(stepA, stepId)
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
  private def getStepRecursive(stepA: Option[StepCurrentConfig], stepId: String): Option[StepCurrentConfig] = {
    if(stepA.get.stepId == stepId){
      stepA
    }else{
      getStepRecursive(stepA.get.nextStep, stepId)
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
  private def printCurrentConfig: Unit = {
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
        Logger.info(step.get.stepId + " -> " + step.get.nameToShow)
        step.get.components.reverse foreach {component => Logger.info("====" + component.componentId + " -> " + component.nameToShow)}
        getNextStep(step.get.nextStep)
      }
      case None => {
        Logger.info(step.get.stepId + " -> " + step.get.nameToShow)
        step.get.components.reverse foreach {component => Logger.info("====" + component.componentId + " -> " + component.nameToShow)}
      }
    }
  }
}
