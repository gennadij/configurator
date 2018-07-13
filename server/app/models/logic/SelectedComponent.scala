package models.logic

import models.bo.{ContainerComponentBO, DependencyBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import models.wrapper.component.ComponentOut
import org.shared.common.status.Success
import org.shared.component.status._
import play.api.Logger

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
  
  private def selectedComponent: ComponentOut = {
    //    Logger.info(this.getClass.getSimpleName + ": ??? " + ???)
    
    val selectedComponentBO: ContainerComponentBO = Persistence.getSelectedComponent(selectedComponentId)
    
    def getSelectedComponentFromDB: ComponentOut = {
      
      def verifySelectedComponent: ComponentOut = {
        
        val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.components.head.componentId.get)
        
        val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.components.head.componentId.get)
        
        val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStep.stepId.get)
        
        verifyStatusFromSelectedComponent(currentStep, selectedComponentBO, fatherStep, nextStep)
        
      }
      
      val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.components.head.componentId.get)
      
//      (fatherStep.status: @unchecked) match {
//        case status @ StatusStep(None,None,Some(FatherStepExist()),Some(Success())) => verifySelectedComponent
//        case status @ StatusStep(None, None, Some(FatherStepNotExist()), Some(Error())) =>
//          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
//        case status @ StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBClassCastError())) =>
//          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
//        case status @ StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBReadError())) =>
//          ComponentOut(status = StatusComponent(None, None, None, status.common, None))
//      }
      ???
    }
//
//    (selectedComponentBO.status.common: @unchecked) match {
//      case status @ Some(Success()) => getSelectedComponentFromDB
//      case status @ Some(Error()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
//      case status @ Some(ODBReadError()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
//      case status @ Some(ODBClassCastError()) => ComponentOut(status = StatusComponent(None, None, None, status, None))
//    }
      ???
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
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
   * @return ComponentOut
   */
  private def setCase1_2_3_4_5_6_7_8_9_10(selectedComponent: ContainerComponentBO, fatherStepId: String): ComponentOut = {
//    ComponentOut(
//        selectedComponent.componentId,
//        fatherStepId,
//        selectedComponent.status,
//        selectedComponent.requireDependenciesOut ::: selectedComponent.excludeDependenciesOut
//    )
    ???
  }
  
  private def verifyStatusFromSelectedComponent(
                                                 currentStep: Option[StepCurrentConfigBO], selectedComponent: ContainerComponentBO,
                                                 fatherStep: StepBO, nextStep: StepBO): ComponentOut = {

//      val componentTypeStatus: StatusComponentType = nextStep.status.nextStep.get match {
//        case NextStepNotExist() => FinalComponent()
//        case NextStepExist() => DefaultComponent()
//        case MultipleNextSteps() => ErrorComponentType()
//        case CommonErrorNextStep() => ErrorComponentType()
//        case NextStepIncludeNoComponents() => ErrorComponentType()
//        case StepCurrentConfigBOIncludeNoSelectedComponents() => ErrorComponentType()
//      }
//
//      val statusSelectedComponent: ContainerComponentBO =
//        SelectedComponentUtil.checkStatusSelectedComponent(currentStep, selectedComponent, fatherStep)
//
//      val selectedComponentStatusComponentType = statusSelectedComponent copy (
//          status = StatusComponent(
//              statusSelectedComponent.status.selectionCriterium,
//              statusSelectedComponent.status.selectedComponent,
//              statusSelectedComponent.status.excludeDependency,
//              statusSelectedComponent.status.common,
//              Some(componentTypeStatus)
//          )
//      )
//
//    val dependencies: List[DependencyBO] =
//      selectedComponentStatusComponentType.requireDependenciesOut ::: selectedComponentStatusComponentType.excludeDependenciesOut
//
//    //                                   selectionCriterium           selectedComponent           excludeDependency                common          componentType
//    //Scenario 6
//    val statusCase1 =  StatusComponent(Some(AllowNextComponent()),    Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 7
//    val statusCase2 =  StatusComponent(Some(RequireNextStep()),       Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 8
//    val statusCase3 =  StatusComponent(Some(RequireNextStep()),       Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
//    //Scenario 9
//    val statusCase4 =  StatusComponent(Some(AllowNextComponent()),    Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 10
//    val statusCase5 =  StatusComponent(Some(RequireComponent()),      Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 16
//    val statusCase6 =  StatusComponent(Some(RequireNextStep()),       Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 5
//    val statusCase7 =  StatusComponent(Some(RequireNextStep()),       Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))
//
//    val statusCase8 =  StatusComponent(Some(RequireNextStep()),       Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//
//    val statusCase9 =  StatusComponent(Some(AllowNextComponent()),    Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
//
//    val statusCase10 = StatusComponent(Some(NotAllowNextComponent()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//
//    val statusCaseDefault = StatusComponent(_, _, _, _, _)
//
//    val status: StatusComponent = selectedComponentStatusComponentType.status
//
//    (status: @unchecked) match {
//      case `statusCase1`       => Logger.info(this.getClass.getSimpleName + " : Case 1"); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase2`       => Logger.info(this.getClass.getSimpleName + " : Case 2 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase3`       => Logger.info(this.getClass.getSimpleName + " : Case 3 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase4`       => Logger.info(this.getClass.getSimpleName + " : Case 4 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase5`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase6`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase7`       => Logger.info(this.getClass.getSimpleName + " : Case 7 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase8`       => Logger.info(this.getClass.getSimpleName + " : Case 8 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase9`       => Logger.info(this.getClass.getSimpleName + " : Case 9 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCase10`      => Logger.info(this.getClass.getSimpleName + " : Case 10 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId)
//      case `statusCaseDefault` => println("Undefined Status"); setCaseDefault
//    }
    ???
  }
}