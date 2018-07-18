package models.logic

import models.bo.{SelectedComponentBO, DependencyBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import models.wrapper.component.ComponentOut
import org.shared.common.status.step._
import org.shared.common.status.{Error, ODBClassCastError, ODBReadError, Success}
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

    val selectedComponentRid: String = RidToHash.getRId(selectedComponentId).get

    val selectedComponentBO: SelectedComponentBO = Persistence.getSelectedComponent(selectedComponentRid)
    
    def getSelectedComponentFromDB: ComponentOut = {
      
      def verifySelectedComponent: ComponentOut = {
        
        val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.component.get.componentId.get)
        
        val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.component.get.componentId.get)

        //convert RID in Hash by fatherStep

        val fatherStepIdHash: Option[String] = RidToHash.getHash(fatherStep.stepId.get)

        val componentIdsHash: List[String] = fatherStep.componentIds.get map (id => {RidToHash.getHash(id).get})

        val fatherStepWithHashId = fatherStep.copy(stepId = fatherStepIdHash, componentIds = Some(componentIdsHash))
        
        val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStepWithHashId.stepId.get)

        //TODO Status wenn currentStep == None
        
        verifyStatusFromSelectedComponent(currentStep, selectedComponentBO, fatherStep, nextStep)
        
      }
      
      val fatherStep: StepBO = Persistence.getFatherStep(selectedComponentBO.component.get.componentId.get)
      
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

    (selectedComponentBO.status.get.common: @unchecked) match {
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
  private def setCase1_2_3_4_5_6_7_8_9_10(selectedComponent: SelectedComponentBO, fatherStepId: String): ComponentOut = {
    ComponentOut(
        selectedComponent.component.get.componentId.get,
        fatherStepId,
        selectedComponent.status.get,
        selectedComponent.component.get.requireDependenciesOut.get ::: selectedComponent.component.get.excludeDependenciesOut.get
    )
  }
  
  private def verifyStatusFromSelectedComponent(currentStep: Option[StepCurrentConfigBO], selectedComponent: SelectedComponentBO,
                                                 fatherStep: StepBO, nextStep: StepBO): ComponentOut = {

      val componentTypeStatus: StatusComponentType = nextStep.status.nextStep.get match {
        case NextStepNotExist() => FinalComponent()
        case NextStepExist() => DefaultComponent()
        case MultipleNextSteps() => ErrorComponentType()
        case CommonErrorNextStep() => ErrorComponentType()
        case NextStepIncludeNoComponents() => ErrorComponentType()
        case StepCurrentConfigBOIncludeNoSelectedComponents() => ErrorComponentType()
      }

      val statusSelectedComponent: SelectedComponentBO =
        SelectedComponentUtil.checkStatusSelectedComponent(currentStep, selectedComponent, fatherStep)

      val selectedComponentStatusComponentType: SelectedComponentBO = statusSelectedComponent copy (
          status = Some(StatusComponent(
              statusSelectedComponent.status.get.selectionCriterium,
              statusSelectedComponent.status.get.selectedComponent,
              statusSelectedComponent.status.get.excludeDependency,
              statusSelectedComponent.status.get.common,
              Some(componentTypeStatus)
          ))
      )

    val dependencies: List[DependencyBO] =
      selectedComponentStatusComponentType.component.get.requireDependenciesOut.get ::: selectedComponentStatusComponentType.component.get.excludeDependenciesOut.get

    //                                   selectionCriterium           selectedComponent           excludeDependency                common          componentType
    //Scenario 6
    val statusCase1 =  StatusComponent(Some(AllowNextComponent()),    Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 7
    val statusCase2 =  StatusComponent(Some(RequireNextStep()),       Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 8
    val statusCase3 =  StatusComponent(Some(RequireNextStep()),       Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))
    //Scenario 9
    val statusCase4 =  StatusComponent(Some(AllowNextComponent()),    Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 10
    val statusCase5 =  StatusComponent(Some(RequireComponent()),      Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 16
    val statusCase6 =  StatusComponent(Some(RequireNextStep()),       Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 5
    val statusCase7 =  StatusComponent(Some(RequireNextStep()),       Some(AddedComponent()),      Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))

    val statusCase8 =  StatusComponent(Some(RequireNextStep()),       Some(RemovedComponent()),    Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))

    val statusCase9 =  StatusComponent(Some(AllowNextComponent()),    Some(NotAllowedComponent()), Some(ExcludedComponent()),    Some(Success()), Some(DefaultComponent()))

    val statusCase10 = StatusComponent(Some(NotAllowNextComponent()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))

    val statusCaseDefault = StatusComponent(_, _, _, _, _)

    val status: StatusComponent = selectedComponentStatusComponentType.status.get

    (status: @unchecked) match {
      case `statusCase1`       => Logger.info(this.getClass.getSimpleName + " : Case 1"); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase2`       => Logger.info(this.getClass.getSimpleName + " : Case 2 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase3`       => Logger.info(this.getClass.getSimpleName + " : Case 3 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase4`       => Logger.info(this.getClass.getSimpleName + " : Case 4 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase5`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase6`       => Logger.info(this.getClass.getSimpleName + " : Case 5 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase7`       => Logger.info(this.getClass.getSimpleName + " : Case 7 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase8`       => Logger.info(this.getClass.getSimpleName + " : Case 8 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase9`       => Logger.info(this.getClass.getSimpleName + " : Case 9 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase10`      => Logger.info(this.getClass.getSimpleName + " : Case 10 "); setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCaseDefault` => println("Undefined Status"); setCaseDefault
    }

  }
}