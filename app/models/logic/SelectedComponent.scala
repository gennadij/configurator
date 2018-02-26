package models.logic

import models.wrapper.component.ComponentOut
import models.bo.ComponentBO
import models.persistence.Persistence
import models.bo.StepBO
import models.currentConfig.CurrentConfig
import models.bo.StepCurrentConfigBO
import models.status.component.StatusSelectedComponent
import models.status.component.StatusSelectionCriterium
import models.status.component.StatusExcludeDependency
import models.status.component.AddedComponent
import models.status.component.RemovedComponent
import models.status.component.ErrorSelectedComponent
import models.status.component.RequireComponent
import models.status.component.RequireNextStep
import models.status.component.AllowNextComponent
import models.status.component.ExcludeComponent
import models.bo.DependencyBO
import models.status.component.ExcludedComponent
import models.status.component.NotExcludedComponent
import models.status.Status
import models.status.Success
import models.status.component.StatusComponent
import models.status.Error
import models.status.ODBReadError
import models.status.ODBClassCastError
import models.status.component.DefaultComponent
import models.status.component.FinalComponent
import models.status.component.StatusComponentType
import play.api.Logger
import models.status.step.NextStepNotExist
import models.status.step.NextStepExist
import models.status.step.MultipleNextSteps
import models.status.step.CommonErrorNextStep
import models.status.component.ErrorComponentType

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 08.02.2018
 */

object SelectedComponent {
  
  def verifySelectedComponent(selectedComponentId: String): ComponentOut = {
    new SelectedComponent(selectedComponentId).verifySelectedComponent
  }
  
}

class SelectedComponent(selectedComponentId: String) {
  
  private def verifySelectedComponent: ComponentOut = {
    
    val selectedComponentBO: Option[ComponentBO] = Persistence.getSelectedComponent(selectedComponentId)
    
    val commonStatusSelectedComponent: Status = selectedComponentBO match {
      case Some(selectedComponentBO) => Success()
      case None => ODBReadError()
    }
    
    val fatherStepBO: StepBO = Persistence.getFatherStep(selectedComponentBO.get.componentId)
    
    val commonStatusFatherStep: Status = fatherStepBO.status.common.get
    
    val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStepBO.stepId)
    
    val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.get.componentId)
    
    val commonStatusNextStep: Status = nextStep.status.common.get
    
    Logger.info(this.getClass.getSimpleName + ": commonStatusNextStep " + commonStatusNextStep)

    val statusSelectedComponent: StatusSelectedComponent = SelectedComponentUtil.checkSelectedComponent(currentStep.get, selectedComponentId)
      
      val previousSelectedComponents: List[ComponentBO] = currentStep match {
        case Some(step) => step.components
        case None => List()
      }
     
//      Logger.info(this.getClass.getSimpleName + ": previousSelectedComponents " + previousSelectedComponents)
      
      val stausSelectionCriterium: StatusSelectionCriterium = 
          SelectedComponentUtil.checkSelectionCriterium(
              previousSelectedComponents.size, 
              fatherStepBO, 
              statusSelectedComponent)
      
//      Logger.info(this.getClass.getSimpleName + ": " + stausSelectionCriterium)
      
      val statusExcludeDependencies: StatusExcludeDependency = SelectedComponentUtil.checkExcludeDependencies(
          currentStep.get, 
          selectedComponentBO.get.excludeDependenciesIn)
      
//      val nextStepExistence: Boolean = SelectedComponentUtil.checkNextStepExistence(nextStep)
      
      val commonStatuses: List[Status] = 
        commonStatusSelectedComponent :: commonStatusFatherStep :: commonStatusNextStep :: Nil
      
      val commonStatus: Status = SelectedComponentUtil.checkCommonStatus(commonStatuses)
      
      val componentTypeStatus: StatusComponentType = nextStep.status.nextStep.get match {
        case NextStepNotExist() => FinalComponent()
        case NextStepExist() => DefaultComponent()
        case MultipleNextSteps() => ErrorComponentType()
        case CommonErrorNextStep() => ErrorComponentType()
      }
      
      val status: StatusComponent = statusExcludeDependencies match {
        case NotExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(statusSelectedComponent), 
            Some(statusExcludeDependencies), 
            Some(commonStatus),
            Some(componentTypeStatus))
        }
        case ExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(ErrorSelectedComponent()), 
            Some(statusExcludeDependencies), 
            Some(Error()),
            Some(componentTypeStatus))
        }
      }
      
      val dependencies: List[DependencyBO] = 
        selectedComponentBO.get.requireDependenciesOut ::: selectedComponentBO.get.excludeDependenciesOut
    
    createComponentOut(
        status, 
        currentStep, 
        selectedComponentBO.get.componentId, 
        selectedComponentBO.get.nameToShow, 
        fatherStepBO.stepId, 
        dependencies)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param StatusComponent
   * 
   * @return ComponentOut
   */
  private def createComponentOut(
      status: StatusComponent, 
      currentStep: Option[StepCurrentConfigBO],
      componentId: String, 
      componentNameToShow: String,
      fatherStepId: String, 
      dependencies: List[DependencyBO]): ComponentOut = {
    
    status.excludeDependency.get match {
      case statusExcludedComponent: ExcludedComponent => {
        ComponentOut(  
            componentId,
            fatherStepId,
            status,
            List()
         )
      }
      case statusNotExcludedComponent: NotExcludedComponent => {
        SelectedComponentUtil.defineStatusForSelectionCreterium(
            status, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
      }
    }
  }
}