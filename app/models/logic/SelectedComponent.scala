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
import models.status.step.NextStepIncludeNoComponents
import models.status.step.StepCurrentConfigBOIncludeNoSelectedComponents
import models.status.step.StatusFatherStep
import models.status.component.NotAllowedComponent
import models.wrapper.dependency.Dependency

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
  
  val selectedComponentBO: Option[ComponentBO] = Persistence.getSelectedComponent(selectedComponentId)
  
  val fatherStepBO: StepBO = Persistence.getFatherStep(selectedComponentBO.get.componentId)
  
  val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStepBO.stepId)
    
  val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.get.componentId)
  
  
  private def verifySelectedComponent: ComponentOut = {
    //    Logger.info(this.getClass.getSimpleName + ": ??? " + ???)
    
//    val selectedComponentBO: Option[ComponentBO] = Persistence.getSelectedComponent(selectedComponentId)
    
    val commonStatusSelectedComponent: Status = selectedComponentBO match {
      case Some(selectedComponentBO) => Success()
      case None => ODBReadError()
    }
    
//    val fatherStepBO: StepBO = Persistence.getFatherStep(selectedComponentBO.get.componentId)
    
    val commonStatusFatherStep: Status = fatherStepBO.status.common.get
    
//    val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStepBO.stepId)
//    
//    val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.get.componentId)
    
    val commonStatusNextStep: Status = nextStep.status.common.get

    val previousSelectedComponents: List[ComponentBO] = currentStep match {
        case Some(step) => step.components
        case None => List()
      }
    
    val statusExcludeDependency: StatusExcludeDependency = SelectedComponentUtil.checkExcludeDependencies(
          previousSelectedComponents map(_.componentId), 
          selectedComponentBO.get.excludeDependenciesIn)
          
    val statusSelectedComponent: StatusSelectedComponent = 
      SelectedComponentUtil.checkSelectedComponent(statusExcludeDependency, currentStep.get, selectedComponentId)
      
    val stausSelectionCriterium: StatusSelectionCriterium = 
        SelectedComponentUtil.checkSelectionCriterium(
            previousSelectedComponents.size,
            fatherStepBO, 
            statusSelectedComponent,
            selectedComponentBO.get)
      
      val commonStatuses: List[Status] = 
        commonStatusSelectedComponent :: commonStatusFatherStep :: commonStatusNextStep :: Nil
      
      val commonStatus: Status = SelectedComponentUtil.checkCommonStatus(commonStatuses)
      
      val componentTypeStatus: StatusComponentType = nextStep.status.nextStep.get match {
        case NextStepNotExist() => FinalComponent()
        case NextStepExist() => DefaultComponent()
        case MultipleNextSteps() => ErrorComponentType()
        case CommonErrorNextStep() => ErrorComponentType()
        case NextStepIncludeNoComponents() => ErrorComponentType()
        case StepCurrentConfigBOIncludeNoSelectedComponents() => ErrorComponentType()
      }
      
      val status: StatusComponent = statusExcludeDependency match {
        case NotExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(statusSelectedComponent), 
            Some(statusExcludeDependency), 
            Some(commonStatus),
            Some(componentTypeStatus))
        }
        case ExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(statusSelectedComponent), 
            Some(statusExcludeDependency), 
            Some(commonStatus),
            Some(componentTypeStatus))
        }
      }
      
    val dependencies: List[DependencyBO] = 
      selectedComponentBO.get.requireDependenciesOut ::: selectedComponentBO.get.excludeDependenciesOut
    
    //                                   selectionCriterium           selectedComponent           excludeDependency                common          componentType
    //Scenario 6
    val statusCase1 = StatusComponent(Some(AllowNextComponent()),   Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 7
    val statusCase2 = StatusComponent(Some(RequireNextStep()),      Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 8
    val statusCase3 = StatusComponent(Some(RequireNextStep()),      Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
    //Scenario 9
    val statusCase4 = StatusComponent(Some(AllowNextComponent()),   Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 10
    val statusCase5 = StatusComponent(Some(RequireComponent()),     Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 2
    val statusCase6 = StatusComponent(Some(RequireComponent()),     Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 5
    val statusCase7 = StatusComponent(Some(RequireNextStep()),      Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))
    
    val statusCase8 = StatusComponent(Some(RequireNextStep()),      Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    
    val statusCase9 = StatusComponent(Some(AllowNextComponent()),   Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
    val statusCaseDefault = StatusComponent(_, _, _, _, _)
    
    status match {
      case `statusCase1`       => println("Case 1 " + setCase1_2(statusCase1, dependencies))
      case `statusCase2`       => println("Case 2 " + setCase1_2(statusCase2, dependencies))
      case `statusCase3`       => println("Case 3 " + setCase3(statusCase3, dependencies))
      case `statusCase4`       => println("Case 4 ")
      case `statusCase5`       => println("Case 5 ")
      case `statusCase6`       => println("Case 6 ")
      case `statusCase7`       => println("Case 7 ")
      case `statusCase8`       => println("Case 8 ")
      case `statusCase9`       => println("Case 9 ")
      case `statusCaseDefault` => println("Undefined Status")
    }
    
    createComponentOut(
        status, 
        currentStep, 
        selectedComponentBO.get.componentId, 
        selectedComponentBO.get.nameToShow, 
        fatherStepBO.stepId, 
        dependencies)
  }
  
  private def setCase1_2(status: StatusComponent, dependencies: List[DependencyBO]): ComponentOut = {
    //TODO Einstalten beim Einsatz
//    val component: ComponentBO = ComponentBO(
//            selectedComponentBO.get.componentId,
//            selectedComponentBO.get.nameToShow,
//            List.empty,
//            List.empty,
//            List.empty,
//            List.empty
//        )
//        CurrentConfig.addComponent(currentStep.get, component)
        ComponentOut(
            selectedComponentBO.get.componentId,
            fatherStepBO.stepId,
            status,
            dependencies
        )
  }
  
  private def setCase3(status: StatusComponent, dependencies: List[DependencyBO]): ComponentOut = {
    ComponentOut(  
        selectedComponentBO.get.componentId,
        fatherStepBO.stepId,
        status,
        dependencies
    )
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
            dependencies
         )
      }
      case statusNotExcludedComponent: NotExcludedComponent => {
        SelectedComponentUtil.defineStatusForSelectionCreterium(
            status, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
      }
    }
  }
}