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
import models.status.step.FatherStepExist
import models.status.step.StatusStep
import models.status.step.FatherStepNotExist
import models.status.step.CommonErrorFatherStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 08.02.2018
 */

object SelectedComponent {
  
  def verifySelectedComponent(selectedComponentId: String): ComponentOut = {
    new SelectedComponent(selectedComponentId).selectedComponent
  }
}

class SelectedComponent(selectedComponentId: String) {
  type type1 
  private def selectedComponent: ComponentOut = {
    //    Logger.info(this.getClass.getSimpleName + ": ??? " + ???)
    
    val selectedComponentBO: ComponentBO = Persistence.getSelectedComponent(selectedComponentId)
    
    def getSelectedComponentFromDB: ComponentOut = {
      
      def verifySelectedComponent: ComponentOut = {
        
        val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.componentId)
        
        val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.componentId)
        
        val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStep.stepId)
        
        verifyStatusFromSelectedComponent(currentStep, selectedComponentBO, fatherStep, nextStep)
        
      }
      
      val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.componentId)
      
      (fatherStep.status: @unchecked) match {
        case status @ StatusStep(None,None,Some(FatherStepExist()),Some(Success())) => verifySelectedComponent
        case status @ StatusStep(None, None, Some(FatherStepNotExist()), Some(Error())) => 
          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
        case status @ StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBClassCastError())) => 
          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
        case status @ StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBReadError())) => 
          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
      }
    }
    
    (selectedComponentBO.status.common: @unchecked) match {
      case status @ Some(Success()) => getSelectedComponentFromDB
      case status @ Some(Error()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
      case status @ Some(ODBReadError()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
      case status @ Some(ODBClassCastError()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
    }
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
  private def setCaseDefault: ComponentOut = {
    ComponentOut(
        "",
        "",
        StatusComponent(None, None, None, None, None),
        List()
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
  private def setCase1_2_3_4_5_6_7_8_9(selectedComponent: ComponentBO, fatherStepId: String): ComponentOut = {
    ComponentOut(
        selectedComponent.componentId,
        fatherStepId,
        selectedComponent.status,
        selectedComponent.requireDependenciesOut ::: selectedComponent.excludeDependenciesOut
    )
  }
  
  private def verifyStatusFromSelectedComponent(
      currentStep: Option[StepCurrentConfigBO], selectedComponent: ComponentBO, 
      fatherStep: StepBO, nextStep: StepBO): ComponentOut = {

//    val statusExcludeDependency: StatusExcludeDependency = SelectedComponentUtil.checkExcludeDependencies(
//      currentStep, 
//      selectedComponent.excludeDependenciesIn)
//      
//      val statusSelectedComponent: StatusSelectedComponent = 
//        SelectedComponentUtil.checkSelectedComponentInCurrentConfig(statusExcludeDependency, currentStep.get, selectedComponent)
//      
//      //update Status in ComponenteBO
//      val selectedComponentBOUpdated_1: ComponentBO = selectedComponent.copy(
//          status = StatusComponent(
//              None, Some(statusSelectedComponent), Some(statusExcludeDependency), 
//              selectedComponent.status.common, None))
//      
//      val selectedComponentStatusSelectionCriterium: ComponentBO = 
//        SelectedComponentUtil.checkSelectionCriterium(currentStep, fatherStep, selectedComponentBOUpdated_1)
      
      
      
      val componentTypeStatus: StatusComponentType = nextStep.status.nextStep.get match {
        case NextStepNotExist() => FinalComponent()
        case NextStepExist() => DefaultComponent()
        case MultipleNextSteps() => ErrorComponentType()
        case CommonErrorNextStep() => ErrorComponentType()
        case NextStepIncludeNoComponents() => ErrorComponentType()
        case StepCurrentConfigBOIncludeNoSelectedComponents() => ErrorComponentType()
      }
      
      val statusSelectedComponent: ComponentBO = 
        SelectedComponentUtil.checkStatusSelectedComponent(currentStep, selectedComponent, fatherStep)
      
      val selectedComponentStatusComponentType = statusSelectedComponent copy (
          status = StatusComponent(
              statusSelectedComponent.status.selectionCriterium,
              statusSelectedComponent.status.selectedComponent,
              statusSelectedComponent.status.excludeDependency,
              statusSelectedComponent.status.common,
              Some(componentTypeStatus)
          )
      )
      
    val dependencies: List[DependencyBO] = 
      selectedComponentStatusComponentType.requireDependenciesOut ::: selectedComponentStatusComponentType.excludeDependenciesOut
    
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
    //Scenario 16
    val statusCase6 = StatusComponent(Some(RequireNextStep()),      Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 5
    val statusCase7 = StatusComponent(Some(RequireNextStep()),      Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))
    
    val statusCase8 = StatusComponent(Some(RequireNextStep()),      Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    
    val statusCase9 = StatusComponent(Some(AllowNextComponent()),   Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
    val statusCaseDefault = StatusComponent(_, _, _, _, _)
    
    val status: StatusComponent = selectedComponentStatusComponentType.status
    
    (status: @unchecked) match {
      case `statusCase1`       => Logger.info(this.getClass.getSimpleName + " : Case 1"); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase2`       => Logger.info(this.getClass.getSimpleName + " : Case 2 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase3`       => Logger.info(this.getClass.getSimpleName + " : Case 3 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase4`       => Logger.info(this.getClass.getSimpleName + " : Case 4 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase5`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase6`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase7`       => Logger.info(this.getClass.getSimpleName + " : Case 7 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase8`       => Logger.info(this.getClass.getSimpleName + " : Case 8 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCase9`       => Logger.info(this.getClass.getSimpleName + " : Case 9 "); setCase1_2_3_4_5_6_7_8_9(selectedComponentStatusComponentType, fatherStep.stepId)
      case `statusCaseDefault` => println("Undefined Status"); setCaseDefault
    }
  }
}