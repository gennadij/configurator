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

    sCBO.status.get.common.get match {
      case Success() =>

        val sCExtendedOfCurrentAndNextStep = getCurrentAndNextStepFromPersistence(sCBO)

        val (statusCurrentStep, statusNextStep): (StatusCurrentStep ,StatusNextStep) =
          (sCExtendedOfCurrentAndNextStep.currentStep.get.status.get.currentStep.get,
            sCExtendedOfCurrentAndNextStep.nextStep.get.status.get.nextStep.get)

        (statusCurrentStep, statusNextStep) match {
          case (CurrentStepExist(), NextStepExist()) =>

            val sCExtendedOfCurrentStep = getCurrentStepFromCurrentConfig(sCExtendedOfCurrentAndNextStep)

            // Status Component Typ
            val sCExtendedOfStatusComponentTyp = verifyStatusComponentType(sCExtendedOfCurrentStep)

            //Test -->

            //Status Exclude Dependency
            val sCExtendedOfStatusExcludeDependency = verifyStatusExcludeDependency(sCExtendedOfStatusComponentTyp)

            //Status Selection Criterium
            val sCExtendedOfStatusSelectionCriterium = verifyStatusSelectionCriterium(sCExtendedOfStatusExcludeDependency)

            // Status Selected Component
            val sCExtendedOFStatusSelectedComponent_Test = verifyStatusSelectedComponent_Test(sCExtendedOfStatusSelectionCriterium)

            verifyStatusSelectedComponent(sCExtendedOFStatusSelectedComponent_Test)

          //Test <--

          //    val sCExtendedOfStatusSelectedComponent: SelectedComponentBO =
          //      SelectedComponentUtil.checkStatusSelectedComponent(selectedComponentBO)
          //
          //    verifyStatusSelectedComponent(sCExtendedOfStatusComponentTyp)
          case _ => sCExtendedOfCurrentAndNextStep
        }
      case _ => sCBO
    }
  }

  private def verifyStatusSelectionCriterium(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.excludeDependency.get match {
      case ExcludedComponent() =>
        val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.currentStepCurrentConfig match {
          case Some(step) => step.components
          case None => List()
        }

        val currentConfigWithTempSelectedComponent: List[ComponentBO] =
          previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.component.get

        val possibleComponentToSelect: List[String] =
          new SelectedComponentUtil().getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
          selectedComponentBO, previousSelectedComponentsInCurrentConfig)

        possibleComponentToSelect match {
          case List() =>
            //RequireNextStep
            val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(RequireNextStep()))
            selectedComponentBO.copy(status = Some(status))
          case _ =>
            val statusSC: StatusSelectionCriterium =
              new SelectedComponentUtil().getSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size,
                selectedComponentBO.currentStep.get)
            val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
            selectedComponentBO.copy(status = Some(status))
        }

      case NotExcludedComponent() =>
        val componentIdExist: Boolean =
          selectedComponentBO.currentStepCurrentConfig.get.components.exists(_.componentId.get == selectedComponentBO.component.get.componentId.get)

        if (componentIdExist) {
          val countOfComponentsInCurrentConfig: Int = selectedComponentBO.currentStepCurrentConfig match {
            case Some(step) => step.components.size
            case None => 0
          }

          val statusSC: StatusSelectionCriterium =
            new SelectedComponentUtil().
              getSelectionCriteriumStatus(countOfComponentsInCurrentConfig, selectedComponentBO.currentStep.get)

          val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
          selectedComponentBO.copy(status = Some(status))

        }else{

          val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.currentStepCurrentConfig match {
            case Some(step) => step.components
            case None => List()
          }

          val currentConfigWithTempSelectedComponent: List[ComponentBO] =
            previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.component.get

          val possibleComponentToSelect: List[String] =
            new SelectedComponentUtil().getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
            selectedComponentBO, previousSelectedComponentsInCurrentConfig)

          val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

          val statusSC: StatusSelectionCriterium = new SelectedComponentUtil().
            getSelectionCriteriumStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent,
              selectedComponentBO.currentStep.get)

          val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
          selectedComponentBO.copy(status = Some(status))
        }
    }
  }


  private def verifyStatusSelectedComponent_Test(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.excludeDependency.get match {
      case ExcludedComponent() =>
        val status = selectedComponentBO.status.get.copy(selectedComponent = Some(NotAllowedComponent()))

        selectedComponentBO.copy(status = Some(status))

      case NotExcludedComponent() =>
        val componentIdExist: Boolean =
          selectedComponentBO.currentStepCurrentConfig.get.components
            .exists(_.componentId.get == selectedComponentBO.component.get.componentId.get)

        if(componentIdExist) {
          //Die Komponente muss aus der CurrentConfig gelöscht werden
          CurrentConfig.removeComponent(selectedComponentBO.currentStepCurrentConfig.get.stepId,
            selectedComponentBO.component.get.componentId.get)

          val status = selectedComponentBO.status.get.copy(selectedComponent = Some(RemovedComponent()))

          selectedComponentBO.copy(status = Some(status))

        }else {

          // TODO Zweimal ausgeführt wird -->
          val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.currentStepCurrentConfig match {
            case Some(step) => step.components
            case None => List()
          }

          val currentConfigWithTempSelectedComponent: List[ComponentBO] =
            previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.component.get

          val possibleComponentToSelect: List[String] =
            new SelectedComponentUtil().getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
              selectedComponentBO, previousSelectedComponentsInCurrentConfig)
          //TODO <--

          selectedComponentBO.status.get.selectionCriterium.get match {
            case RequireComponent() => new SelectedComponentUtil().getComponentBOByStatusRequireComponent(possibleComponentToSelect,
              selectedComponentBO)
            case RequireNextStep() => new SelectedComponentUtil().getComponentBOByStatusRequireNextStep(selectedComponentBO, selectedComponentBO.currentStepCurrentConfig)
            case AllowNextComponent() => new SelectedComponentUtil().getComponentBOByStatusAllowNextComponent(possibleComponentToSelect,
              selectedComponentBO)
            case NotAllowNextComponent() =>
              selectedComponentBO.copy(status = Some(StatusComponent(
                Some(RequireNextStep()),
                Some(NotAllowedComponent()),
                Some(NotExcludedComponent()),
                Some(Success()),
                Some(DefaultComponent())
              )))
            case ErrorSelectionCriterium() =>
              selectedComponentBO.copy(status = Some(StatusComponent(
                Some(ErrorSelectionCriterium()),
                Some(NotAllowedComponent()),
                Some(NotExcludedComponent()),
                Some(Error()),
                Some(DefaultComponent())
              )))
          }
        }
    }
  }

  private def verifyStatusExcludeDependency(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val selectedComponentIdsFromCurrentConfig: List[String] = selectedComponentBO.currentStepCurrentConfig match {
      case Some(step) => step.components map (_.componentId.get)
      case None => List()
    }

    val excludeComponentsIds: List[String] =
      selectedComponentIdsFromCurrentConfig flatMap {
        sCId => (selectedComponentBO.component.get.excludeDependenciesIn.get map (_.outId)).filter { inECId => sCId == inECId }
      }
    val status = selectedComponentBO.status.get
    excludeComponentsIds match {
      case List() =>
        val statusNotExcludedComponent: StatusComponent =
          selectedComponentBO.status.get.copy(excludeDependency = Some(NotExcludedComponent()))
        selectedComponentBO.copy(status = Some(statusNotExcludedComponent))
      case _ =>
        val statusExcludedComponent: StatusComponent =
          selectedComponentBO.status.get.copy(excludeDependency = Some(ExcludedComponent()))
        selectedComponentBO.copy(status = Some(statusExcludedComponent))
    }
  }



  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return selectedComponentBO
    */
  private def getCurrentAndNextStepFromPersistence(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val currentStepBO: StepBO = Persistence.getCurrentStep(selectedComponentBO.component.get.componentId.get)

    val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.component.get.componentId.get)

    selectedComponentBO.copy(currentStep = Some(currentStepBO), nextStep = Some(nextStep))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return StepBO
    */
  private def getCurrentStepFromCurrentConfig(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val currentStepCurrentConfig: Option[StepCurrentConfigBO] =
      CurrentConfig.getCurrentStep(selectedComponentBO.currentStep.get.stepId.get)

    selectedComponentBO.copy(currentStepCurrentConfig = currentStepCurrentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return StepBO
    */
  private def verifyStatusComponentType(selectedComponentBO: SelectedComponentBO):SelectedComponentBO = {

    //TODO NextStepNotExist -> commonStatus = SUCCESS
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

  private def verifyStatusSelectedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {


    //Test -->
//    val statusSelectedComponent: SelectedComponentBO = SelectedComponentUtil.checkStatusSelectedComponent(selectedComponentBO)

//    val selectedComponentStatusComponentType: SelectedComponentBO = statusSelectedComponent copy (
//      status = Some(StatusComponent(
//        statusSelectedComponent.status.get.selectionCriterium,
//        statusSelectedComponent.status.get.selectedComponent,
//        statusSelectedComponent.status.get.excludeDependency,
//        statusSelectedComponent.status.get.common,
//        statusSelectedComponent.status.get.componentType
//      ))
//      )
    //Test <--

    val selectedComponentStatusComponentType: SelectedComponentBO = selectedComponentBO

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