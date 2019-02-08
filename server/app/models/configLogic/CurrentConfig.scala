package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.{CurrentConfigContainerBO, StepCurrentConfigBO}
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Crated by Gennadi Heimann 28.02.2017
 */
trait CurrentConfig {

//  var firstStep: Option[StepCurrentConfigBO] = None
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param step: StepCurrentConfigBO, component: ContainerComponentBO
   * 
   * @return Unit
   */
  def addComponent(
                    currentConfigContainerBO: CurrentConfigContainerBO,
                    step: StepCurrentConfigBO,
                    component: SelectedComponentBO): Unit = {
    
    val currentStep: Option[StepCurrentConfigBO] =
      getStep(currentConfigContainerBO, step.stepId)
    
    val currentComponents: List[SelectedComponentBO] = currentStep.get.components

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
  def addFirstStep(currentConfigContainerBO: CurrentConfigContainerBO, firstStep: StepCurrentConfigBO): Unit = {
    currentConfigContainerBO.currentConfig = Some(firstStep)
    //    this.firstStep = Some(firstStep)
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
  def addStep(
             currentConfigContainerBO: CurrentConfigContainerBO,
             currentStep: Option[StepCurrentConfigBO],
             fatherStep: Option[StepCurrentConfigBO]): Unit = {
    fatherStep match {
      case Some(fS) => fS.nextStep = currentStep
//      case None => firstStep = currentStep
      case None => currentConfigContainerBO.currentConfig = currentStep
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
  def getCurrentStep(
                      currentConfigContainerBO: CurrentConfigContainerBO, stepId: String
                    ): Option[StepCurrentConfigBO] = {
    getStep(currentConfigContainerBO, stepId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return Option[StepCurrentConfigBO]
   */
//  def getCurrentConfig: Option[StepCurrentConfigBO] = this.firstStep
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return StepCurrentConfig
   */
  def getLastStep(currentConfigContainerBO: CurrentConfigContainerBO): StepCurrentConfigBO = {
//    val firstStep = this.firstStep.get
    getLastStepRecursive(currentConfigContainerBO.currentConfig.get)
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
  private def getStep(
                       currentConfigContainerBO: CurrentConfigContainerBO, stepId: String): Option[StepCurrentConfigBO] = {
    getStepRecursive(currentConfigContainerBO.currentConfig, stepId)
//    getStepRecursive(this.firstStep, stepId)
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
  private def printCurrentConfig(
                                  currentConfigContainerBO: CurrentConfigContainerBO
                                ): Unit = getNextStep(currentConfigContainerBO.currentConfig)
//    getNextStep(this.firstStep)
  
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
  def removeComponent(
                       currentConfigContainerBO: CurrentConfigContainerBO,
                       selectedComponentBO: SelectedComponentContainerBO
                     ): List[SelectedComponentBO] = {

    val step: Option[StepCurrentConfigBO] =
      getCurrentStep(currentConfigContainerBO, selectedComponentBO.stepCurrentConfig.get.stepId)

    Logger.info(this.getClass.getSimpleName + ": " + step.get.components + " " + selectedComponentBO.selectedComponent.get.componentId.get)
    Logger.info("Step with deleted component " + step.get.getClass.hashCode())

    step.get.components = step.get.components.filterNot(_.componentId == selectedComponentBO.selectedComponent.get.componentId)

    Logger.info("Step with deleted component " + step.get.components)

    printCurrentConfig(currentConfigContainerBO)

    step.get.components
  }
}
