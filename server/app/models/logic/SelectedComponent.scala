package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.step._
import org.shared.common.status.{Error, Success}
import org.shared.component.status._
import play.api.Logger

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */

object SelectedComponent {

  def verifySelectedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    new SelectedComponent(selectedComponentBO).selectedComponent
  }
}

class SelectedComponent(selectedComponentBO: SelectedComponentBO) {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @return StepBO
    */
  private def selectedComponent: SelectedComponentBO = {

    val selectedComponentId: String = selectedComponentBO.component.get.componentId.get

    val sCBO: SelectedComponentBO = Persistence.getSelectedComponent(selectedComponentId)

    val sCExtendedOfCurrentAndNextStep = getCurrentAndNextStepFromPersistence(sCBO)

    val sCExtendedOfCurrentStep = getCurrentStepFromCurrentConfig(sCExtendedOfCurrentAndNextStep)

    val sCExtendedOfVerifiedStatsComponentTyp = verifyStatusComponentType(sCExtendedOfCurrentStep)

    verifyStatusFromSelectedComponent(sCExtendedOfVerifiedStatsComponentTyp)
  }



  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return selectedComponentBO
    */
  private def getCurrentAndNextStepFromPersistence(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.common match {
      case Some(Success()) =>

        val currentStepBO: StepBO = Persistence.getCurrentStep(selectedComponentBO.component.get.componentId.get)

        val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.component.get.componentId.get)

        selectedComponentBO.copy(currentStep = Some(currentStepBO), nextStep = Some(nextStep))

      case status => selectedComponentBO.copy(status = Some(StatusComponent(common = status)))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return StepBO
    */
  private def getCurrentStepFromCurrentConfig(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.currentStep.get.status.get.currentStep match {
      case Some(CurrentStepExist()) =>

        val currentStepCurrentConfig: Option[StepCurrentConfigBO] =
          CurrentConfig.getCurrentStep(selectedComponentBO.currentStep.get.stepId.get)

        selectedComponentBO.copy(currentStepCurrentConfig = currentStepCurrentConfig)

      case Some(_) =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(selectedComponentBO.currentStep.get.status.get.currentStep.get))))
      case None =>
        selectedComponentBO.copy(status = Some(StatusComponent(common = Some(Error()))))

    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return StepBO
    */
  private def verifyStatusComponentType(selectedComponentBO: SelectedComponentBO):SelectedComponentBO = {


    val componentTypeStatus: StatusComponentType = selectedComponentBO.nextStep.get.status.get.nextStep.get match {
      case NextStepNotExist() => FinalComponent()
      case NextStepExist() => DefaultComponent()
      case errorStatus => ErrorComponentType()
    }

    val statusComponent: StatusComponent = selectedComponentBO.status.get.copy(componentType = Some(componentTypeStatus))

    selectedComponentBO.copy(status = Some(statusComponent))
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
      component = Some(ComponentBO(
        componentId = selectedComponent.component.get.componentId,
        excludeDependenciesOut = selectedComponent.component.get.excludeDependenciesOut,
        excludeDependenciesIn = selectedComponent.component.get.excludeDependenciesIn,
        requireDependenciesOut = selectedComponent.component.get.requireDependenciesOut,
        requireDependenciesIn = selectedComponent.component.get.requireDependenciesIn)
      ),
      currentStep = Some(StepBO(stepId = Some(fatherStepId)))
    )
  }

  private def verifyStatusFromSelectedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val statusSelectedComponent: SelectedComponentBO = SelectedComponentUtil.checkStatusSelectedComponent(selectedComponentBO)

    val selectedComponentStatusComponentType: SelectedComponentBO = statusSelectedComponent copy (
      status = Some(StatusComponent(
        statusSelectedComponent.status.get.selectionCriterium,
        statusSelectedComponent.status.get.selectedComponent,
        statusSelectedComponent.status.get.excludeDependency,
        statusSelectedComponent.status.get.common,
        statusSelectedComponent.status.get.componentType
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
      case `statusCase1` => Logger.info(this.getClass.getSimpleName + " : Case 1")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase2` => Logger.info(this.getClass.getSimpleName + " : Case 2 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase3` => Logger.info(this.getClass.getSimpleName + " : Case 3 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase4` => Logger.info(this.getClass.getSimpleName + " : Case 4 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase5` => Logger.info(this.getClass.getSimpleName + " : Case 5 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase6` => Logger.info(this.getClass.getSimpleName + " : Case 5 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase7` => Logger.info(this.getClass.getSimpleName + " : Case 7 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase8` => Logger.info(this.getClass.getSimpleName + " : Case 8 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase9` => Logger.info(this.getClass.getSimpleName + " : Case 9 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCase10` => Logger.info(this.getClass.getSimpleName + " : Case 10 ")
        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
      case `statusCaseDefault` => println("Undefined Status")
        setCaseDefault()
    }

  }
}