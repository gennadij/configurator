package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.{Error, Success}
import org.shared.common.status.step._
import org.shared.component.status._
import play.api.Logger

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */

object SelectedComponent {

  def verifySelectedComponent(selectedComponentId: String): SelectedComponentBO = {
    new SelectedComponent(selectedComponentId).selectedComponent
  }
}

class SelectedComponent(selectedComponentId: String) {

  private def selectedComponent: SelectedComponentBO = {

    val selectedComponentRid: String = RidToHash.getRId(selectedComponentId).get

    val selectedComponentBO: SelectedComponentBO = Persistence.getSelectedComponent(selectedComponentRid)

    val sCExtendedOfFatherStepBO: SelectedComponentBO = getFatherStep(selectedComponentBO)

    val sCExtendenOfNextStep: SelectedComponentBO = getNextStep(sCExtendedOfFatherStepBO)

    getCurrentStep(sCExtendenOfNextStep)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param fatherStep : StepBO
    * @return StepBO
    */
  private def convertRidToHashIn(fatherStep: StepBO): StepBO = {
    val fatherStepIdHash: Option[String] = RidToHash.getHash(fatherStep.stepId.get)

    val componentIdsHash: List[String] = fatherStep.componentIds.get map (id => {
      RidToHash.getHash(id).get
    })

    fatherStep.copy(stepId = fatherStepIdHash, componentIds = Some(componentIdsHash))
  }

  private def getFatherStep(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.common match {
      case Some(Success()) =>
        val fatherStepBO: StepBO = Persistence.getFatherStep(selectedComponentBO.component.get.componentId.get)
        selectedComponentBO.copy(fatherStep = Some(fatherStepBO))
      case status => selectedComponentBO.copy(status = Some(StatusComponent(common = status)))
    }
  }

  private def getNextStep(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.common match {
      case Some(Success()) =>
        val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.component.get.componentId.get)
        selectedComponentBO.copy(nextStep = Some(nextStep))
      case status => selectedComponentBO.copy(status = Some(StatusComponent(common = status)))
    }
  }

  private def getCurrentStep(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.fatherStep.get.status.get.fatherStep match {
      case Some(FatherStepExist()) =>
        val fatherStepWithHashId: StepBO = convertRidToHashIn(selectedComponentBO.fatherStep.get)

        val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStepWithHashId.stepId.get)

        selectedComponentBO.copy(fatherStep = Some(fatherStepWithHashId), currentStep = currentStep)

        verifyStatusFromSelectedComponent(currentStep, selectedComponentBO, fatherStepWithHashId, selectedComponentBO.nextStep.get)

      case Some(FatherStepNotExist()) =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(selectedComponentBO.fatherStep.get.status.get.fatherStep.get))))
      case Some(MultipleFatherSteps()) =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(selectedComponentBO.fatherStep.get.status.get.fatherStep.get))))
      case Some(CommonErrorFatherStep()) =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(selectedComponentBO.fatherStep.get.status.get.fatherStep.get))))
      case None =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(Error()))))

    }
  }


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return ComponentOut
    */
  private def setCaseDefault(): SelectedComponentBO = {

    SelectedComponentBO(status = Some(StatusComponent(None, None, None, None, None)))
//    ComponentOut(
//      "",
//      "",
//      StatusComponent(None, None, None, None, None),
//      List()
//    )
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return ComponentOut
    */
  private def setCase1_2_3_4_5_6_7_8_9_10(selectedComponent: SelectedComponentBO, fatherStepId: String): SelectedComponentBO = {
    SelectedComponentBO(
      status = selectedComponent.status,
      component = Some(ComponentBO(componentId = selectedComponent.component.get.componentId)),
      fatherStep = Some(StepBO(stepId = Some(fatherStepId)))
    )

//    ComponentOut(
//      selectedComponent.component.get.componentId.get,
//      fatherStepId,
//      selectedComponent.status.get,
//      selectedComponent.component.get.requireDependenciesOut.get ::: selectedComponent.component.get.excludeDependenciesOut.get
//    )
  }

  private def verifyStatusFromSelectedComponent(currentStep: Option[StepCurrentConfigBO], selectedComponent: SelectedComponentBO,
                                                fatherStep: StepBO, nextStep: StepBO): SelectedComponentBO = {

    val componentTypeStatus: StatusComponentType = nextStep.status.get.nextStep.get match {
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
    val statusCase1 = StatusComponent(Some(AllowNextComponent()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 7
    val statusCase2 = StatusComponent(Some(RequireNextStep()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 8
    val statusCase3 = StatusComponent(Some(RequireNextStep()), Some(NotAllowedComponent()), Some(ExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 9
    val statusCase4 = StatusComponent(Some(AllowNextComponent()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 10
    val statusCase5 = StatusComponent(Some(RequireComponent()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 16
    val statusCase6 = StatusComponent(Some(RequireNextStep()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
    //Scenario 5
    val statusCase7 = StatusComponent(Some(RequireNextStep()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))

    val statusCase8 = StatusComponent(Some(RequireNextStep()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))

    val statusCase9 = StatusComponent(Some(AllowNextComponent()), Some(NotAllowedComponent()), Some(ExcludedComponent()), Some(Success()), Some(DefaultComponent()))

    val statusCase10 = StatusComponent(Some(NotAllowNextComponent()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))

    val statusCaseDefault = StatusComponent(_, _, _, _, _)

    val status: StatusComponent = selectedComponentStatusComponentType.status.get

    (status: @unchecked) match {
      case `statusCase1` => Logger.info(this.getClass.getSimpleName + " : Case 1");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase2` => Logger.info(this.getClass.getSimpleName + " : Case 2 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase3` => Logger.info(this.getClass.getSimpleName + " : Case 3 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase4` => Logger.info(this.getClass.getSimpleName + " : Case 4 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase5` => Logger.info(this.getClass.getSimpleName + " : Case 5 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase6` => Logger.info(this.getClass.getSimpleName + " : Case 5 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase7` => Logger.info(this.getClass.getSimpleName + " : Case 7 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase8` => Logger.info(this.getClass.getSimpleName + " : Case 8 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase9` => Logger.info(this.getClass.getSimpleName + " : Case 9 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCase10` => Logger.info(this.getClass.getSimpleName + " : Case 10 ");
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, fatherStep.stepId.get)
      case `statusCaseDefault` => println("Undefined Status");
        setCaseDefault
    }

  }
}