package models.currentConfig

import models.bo.{ComponentBO, SelectedComponentBO, StepCurrentConfigBO}
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 */
//object CurrentConfig {
//
//
//  val currentConfig: CurrentConfig = new CurrentConfig
//
//  println("currentConfig " + currentConfig.hashCode())
//
//  //TODO handling from multiply clients
//
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * Neu ausgewaelte Komponente wird zu seinem Schritt eingefuegt
//   *
//   * @param step: StepCurrentConfigBO, component: ContainerComponentBO
//   *
//   * @return Unit
//   */
//  def addComponent(step: StepCurrentConfigBO, component: ComponentBO): Unit = {
//    // es wird geprueft ob der Step schon angelegt war
//    // Die erste Komponente in dem Schritt wird ohne Pruefung des SelectionCriterium hinzugefuegt
//    // Die Abhaengigkeiten werden weiterhin jedes mal geprueft
//    currentConfig.addComponent(step, component)
//  }
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.2
//   *
//   * @param firstStep: StepCurrentConfigBO
//   *
//   * @return Unit
//   */
//  def addFirstStep(firstStep: StepCurrentConfigBO): Unit = currentConfig.addFirstStep(firstStep)
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * Dieser neuer Schritt wird in die CurrentConfig hinzugefuegt
//   *
//   * @param nextStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]
//   *
//   * @return Unit
//   */
//  def addStep(nextStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]): Unit = {
//    currentConfig.addStep(nextStep, fatherStep)
//  }
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * @return Option[StepCurrentConfigBO]
//   */
//  def getCurrentConfig: Option[StepCurrentConfigBO] = currentConfig.getCurrentConfig
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * @return Unit
//   */
//  def printCurrentConfig(): Unit = currentConfig.printCurrentConfig()
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * @param stepId: String
//   *
//   * @return Option[StepCurrentConfigBO]
//   */
//  def getCurrentStep(stepId: String): Option[StepCurrentConfigBO] = {
//    currentConfig.getCurrentStep(stepId)
//  }
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * @return StepCurrentConfig
//   */
//  def getLastStep: StepCurrentConfigBO = {
//    currentConfig.getLastStep
//  }
//
//  /**
//   * @author Gennadi Heimann
//   *
//   * @version 0.0.1
//   *
//   * @param selectedComponentBO: SelectedComponentBO
//   *
//   * @return List[ContainerComponentBO]
//   */
//  def removeComponent(selectedComponentBO: SelectedComponentBO): List[ComponentBO] = {
//    currentConfig.removeComponent(selectedComponentBO: SelectedComponentBO)
//  }
//}

class CurrentConfig {
  
  var firstStep: Option[StepCurrentConfigBO] = None

  println("CurrentConfig " + this.hashCode())
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param step: StepCurrentConfigBO, component: ContainerComponentBO
   * 
   * @return Unit
   */
  def addComponent(step: StepCurrentConfigBO, component: ComponentBO): Unit = {
    
    val currentStep: Option[StepCurrentConfigBO] = getStep(step.stepId)
    
    val currentComponents: List[ComponentBO] = currentStep.get.components

    currentStep.get.components = currentComponents.::(component)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param firstStep: StepCurrentConfigBO
   * 
   * @return Unit
   */
  def addFirstStep(firstStep: StepCurrentConfigBO): Unit = {
    this.firstStep = Some(firstStep)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param currentStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]
   * 
   * @return Unit
   */
  def addStep(currentStep: Option[StepCurrentConfigBO], fatherStep: Option[StepCurrentConfigBO]): Unit = {
    fatherStep match {
      case Some(fS) => fS.nextStep = currentStep
      case None => firstStep = currentStep
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param stepId: String
   * 
   * @return Option[StepCurrentConfig]
   */
  def getCurrentStep(stepId: String): Option[StepCurrentConfigBO] = {
    getStep(stepId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return Option[StepCurrentConfigBO]
   */
  def getCurrentConfig: Option[StepCurrentConfigBO] = this.firstStep
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return StepCurrentConfig
   */
  def getLastStep: StepCurrentConfigBO = {
    val firstStep = this.firstStep.get
    getLastStepRecursive(firstStep)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param step: StepCurrentConfigBO
   * 
   * @return StepCurrentConfig
   */
  private def getLastStepRecursive(step: StepCurrentConfigBO): StepCurrentConfigBO = {
    
    step.nextStep match {
      case Some(s) => getLastStepRecursive(s)
      case None => step
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param stepId: String
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
   * @param stepA: Option[StepCurrentConfigBO], stepId: String
   * 
   * @return Option[StepCurrentConfig]
   */
  private def getStepRecursive(stepA: Option[StepCurrentConfigBO], stepId: String): Option[StepCurrentConfigBO] = {
    if(stepA.get.stepId == stepId){
      stepA
    }else{
      stepA.get.nextStep match {
        case Some(_) => getStepRecursive(stepA.get.nextStep, stepId)
        case None => None
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return Unit
   */
  private def printCurrentConfig(): Unit = getNextStep(this.firstStep)
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param step: Option[StepCurrentConfigBO]
   * 
   * @return Unit
   */
  private def getNextStep(step: Option[StepCurrentConfigBO]): Unit = {
    step.get.nextStep match {
      case Some(_) =>
        Logger.info(step.get.getClass.hashCode() + " -> " + step.get.stepId + " -> " + step.get.nameToShow)
        step.get.components.reverse foreach {component => Logger.info("====" + component.hashCode() + "-" + component.componentId + " -> " + component.nameToShow)}
        getNextStep(step.get.nextStep)
      case None =>
        Logger.info(step.get.getClass.hashCode() + " -> " + step.get.stepId + " -> " + step.get.nameToShow)
        step.get.components.reverse foreach {component => Logger.info("====" + component.hashCode() + "-" + component.componentId + " -> " + component.nameToShow)}
    }
  }
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param selectedComponentBO: SelectedComponentBO
   * 
   * @return Unit
   */
  def removeComponent(selectedComponentBO: SelectedComponentBO): List[ComponentBO] = {

    val step: Option[StepCurrentConfigBO] = getCurrentStep(selectedComponentBO.stepCurrentConfig.get.stepId)

    Logger.info(this.getClass.getSimpleName + ": " + step.get.components + " " + selectedComponentBO.selectedComponent.get.componentId.get)
    Logger.info("Step with deleted component " + step.get.getClass.hashCode())

    step.get.components = step.get.components.filterNot(_.componentId == selectedComponentBO.selectedComponent.get.componentId)

    Logger.info("Step with deleted component " + step.get.components)

    printCurrentConfig()

    step.get.components
  }
}
