package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.{CurrentConfigContainerBO, CurrentConfigStepBO}
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Crated by Gennadi Heimann 28.02.2017
 */
trait CurrentConfig {


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
                    step: CurrentConfigStepBO,
                    component: SelectedComponentBO): Unit = {
    
    val currentStep: Option[CurrentConfigStepBO] =
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
  def addFirstStep(currentConfigContainerBO: CurrentConfigContainerBO, firstStep: CurrentConfigStepBO): Unit = {
    currentConfigContainerBO.currentConfig = Some(firstStep)
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
               currentStep: Option[CurrentConfigStepBO],
               fatherStep: Option[CurrentConfigStepBO]): Unit = {
    fatherStep match {
      case Some(fS) => fS.nextStep = currentStep
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
  def getCurrentConfigStep(
                      currentConfigContainerBO: CurrentConfigContainerBO, stepId: String
                    ): Option[CurrentConfigStepBO] = {
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
  def getLastStep(currentConfigContainerBO: CurrentConfigContainerBO): CurrentConfigStepBO = {
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
  private def getLastStepRecursive(step: CurrentConfigStepBO): CurrentConfigStepBO = {

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
                       currentConfigContainerBO: CurrentConfigContainerBO, stepId: String): Option[CurrentConfigStepBO] = {
    getStepRecursive(currentConfigContainerBO.currentConfig, stepId)
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
  private def getStepRecursive(stepA: Option[CurrentConfigStepBO], stepId: String): Option[CurrentConfigStepBO] = {
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

  /**
   * @author Gennadi Heimann
   *
   * @version 0.0.1
   *
   * @param step: Option[StepCurrentConfigBO]
   *
   * @return Unit
   */
  private def getNextStep(step: Option[CurrentConfigStepBO]): Unit = {
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
   * @param componentBOToRemove: SelectedComponentBO
   *
   * @return Unit
   */
  def removeComponentInternal(
                       currentConfigContainerBO: CurrentConfigContainerBO,
                       componentBOToRemove: SelectedComponentContainerBO
                     ): List[SelectedComponentBO] = {

    val step: Option[CurrentConfigStepBO] =
      getCurrentConfigStep(currentConfigContainerBO, componentBOToRemove.stepCurrentConfig.get.stepId)

    Logger.info(this.getClass.getSimpleName + ": " + step.get.components + " " + componentBOToRemove.selectedComponent.get.componentId.get)
    Logger.info("Step with deleted component " + step.get.getClass.hashCode())

    step.get.components = step.get.components.filterNot(_.componentId == componentBOToRemove.selectedComponent.get.componentId)

    Logger.info("Step with deleted component " + step.get.components)

    printCurrentConfig(currentConfigContainerBO)

    step.get.components
  }

  def removeComponentExternal(
                             currentConfigStepBO: CurrentConfigStepBO,
                             componentToRemove: SelectedComponentBO): List[SelectedComponentBO] = {

    currentConfigStepBO.components = currentConfigStepBO.components.filterNot(_.componentId == componentToRemove.componentId)

    currentConfigStepBO.components
  }

  def getAllComponents(currentConfigContainerBO: CurrentConfigContainerBO): List[String] = {
    getAllComponentsRecursive(currentConfigContainerBO.currentConfig.get, List())
  }

  private def getAllComponentsRecursive(stepCurrentConfigBO: CurrentConfigStepBO, allComponentsId: List[String]): List[String] = {
    stepCurrentConfigBO.nextStep match {
      case Some(nextStep) =>
        getAllComponentsRecursive(nextStep, allComponentsId ::: stepCurrentConfigBO.components.map(_.componentId.get))
      case None => allComponentsId ::: stepCurrentConfigBO.components.map(_.componentId.get)
    }
  }
}
