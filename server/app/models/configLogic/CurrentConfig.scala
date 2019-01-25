package models.configLogic

import models.bo.{ComponentBO, SelectedComponentContainerBO, StepCurrentConfigBO}
import play.api.Logger

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
  def removeComponent(selectedComponentBO: SelectedComponentContainerBO): List[ComponentBO] = {

    val step: Option[StepCurrentConfigBO] = getCurrentStep(selectedComponentBO.stepCurrentConfig.get.stepId)

    Logger.info(this.getClass.getSimpleName + ": " + step.get.components + " " + selectedComponentBO.selectedComponent.get.componentId.get)
    Logger.info("Step with deleted component " + step.get.getClass.hashCode())

    step.get.components = step.get.components.filterNot(_.componentId == selectedComponentBO.selectedComponent.get.componentId)

    Logger.info("Step with deleted component " + step.get.components)

    printCurrentConfig()

    step.get.components
  }
}
