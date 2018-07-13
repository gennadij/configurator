package models.currentConfig

import models.bo.{ContainerComponentBO, StepCurrentConfigBO}
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
  def addComponent(step: StepCurrentConfigBO, component: ContainerComponentBO): Unit = {
    // es wird geprueft ob der Step schon angelegt war
    // Die erste Komponente in dem Schritt wird ohne Pruefung des SelectionCriterium hinzugefuegt
    // Die Abhaengigkeiten werden weiterhin jedes mal geprueft
    currentConfig.addComponent(step, component)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param StepCurrentConfigBO
   * 
   * @return Unit
   */
  def addFirstStep(firstStep: StepCurrentConfigBO): Unit = {
    currentConfig.addFirstStep(firstStep)
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
  def addStep(nextStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]): Unit = {
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
  def getCurrentStep(stepId: String): Option[StepCurrentConfigBO] = {
    currentConfig.getCurrentStep(stepId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return StepCurrentConfig
   */
  def getLastStep: StepCurrentConfigBO = {
    currentConfig.getLastStep
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig, String
   * 
   * @return Unit
   */
  def removeComponent(stepId: String, componentId: String): List[ContainerComponentBO] = {
    currentConfig.removeComponent(stepId, componentId)
  }
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 */
class CurrentConfig {
  
  var firstStep: Option[StepCurrentConfigBO] = None
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StepCurrentConfig, Component
   * 
   * @return Unit
   */
  private def addComponent(step: StepCurrentConfigBO, component: ContainerComponentBO): Unit = {
    
    val currentStep: Option[StepCurrentConfigBO] = getStep(step.stepId)
    
//    val currentComponents: List[ContainerComponentBO] = currentStep.get.components.get
//
//    currentStep.get.components = currentComponents.::(component)
    ???
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param Option[StepCurrentConfig]
   * 
   * @return Unit
   */
  private def addFirstStep(firstStep: StepCurrentConfigBO): Unit = {
    this.firstStep = Some(firstStep)
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
  private def addStep(currentStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]): Unit = {
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
  private def getCurrentStep(stepId: String): Option[StepCurrentConfigBO] = {
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
  private def getCurrentConfig: Option[StepCurrentConfigBO] = this.firstStep
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return StepCurrentConfig
   */
  private def getLastStep: StepCurrentConfigBO = {
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
  private def getLastStepRecursive(step: StepCurrentConfigBO): StepCurrentConfigBO = {
    
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
  private def getStep(stepId: String): Option[StepCurrentConfigBO] = {
    
    getStepRecursive(this.firstStep, stepId)
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
  private def getStepRecursive(stepA: Option[StepCurrentConfigBO], stepId: String): Option[StepCurrentConfigBO] = {
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
//    Logger.info("Current Configuration")
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
  private def getNextStep(step: Option[StepCurrentConfigBO]): Unit = {
//    step.get.nextStep match {
//      case Some(nextStep) => {
//        Logger.info(step.get.getClass.hashCode() + " -> " + step.get.stepId + " -> " + step.get.nameToShow)
//        step.get.components.reverse foreach {component => Logger.info("====" + component.hashCode() + "-" + component.componentId + " -> " + component.nameToShow)}
//        getNextStep(step.get.nextStep)
//      }
//      case None => {
//        Logger.info(step.get.getClass.hashCode() + " -> " + step.get.stepId + " -> " + step.get.nameToShow)
//        step.get.components.reverse foreach {component => Logger.info("====" + component.hashCode() + "-" + component.componentId + " -> " + component.nameToShow)}
//      }
//    }
    ???
  }
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param StepCurrentConfig
   * 
   * @return Unit
   */
  private def removeComponent(stepId: String, componentId: String): List[ContainerComponentBO] = {
    val step: Option[StepCurrentConfigBO] = getCurrentStep(stepId)
//    Logger.info(this.getClass.getSimpleName + ": " + step.get.components + " " + componentId)
//    Logger.info("Step with deleted component " + step.get.getClass.hashCode())
//    step.get.components = step.get.components.filterNot(_.componentId == componentId)
//    Logger.info("Step with deleted component " + step.get.components)
//    printCurrentConfig
//    step.get.components
    ???
  }
}
