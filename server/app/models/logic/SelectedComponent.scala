package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.step._
import org.shared.common.status.{Status, Success}
import org.shared.component.status._

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

    val selectedComponentId: String = selectedComponentBO.selectedComponent.get.componentId.get

    val sCBO: SelectedComponentBO = Persistence.getSelectedComponent(selectedComponentId)

    sCBO.status.get.common.get match {
      case Success() =>

        val sCExtendedOfCurrentAndNextStep = getCurrentAndNextStepFromPersistence(sCBO)

        val (statusCurrentStep, statusNextStep): (Status ,Status) =
          (sCExtendedOfCurrentAndNextStep.currentStep.get.status.get.common.get,
            sCExtendedOfCurrentAndNextStep.nextStep.get.status.get.common.get)

        (statusCurrentStep, statusNextStep) match {
          case (Success(), Success()) =>

            val sCExtendedOfCurrentStep = getCurrentStepFromCurrentConfig(sCExtendedOfCurrentAndNextStep)

            val sCExtendedOfPossibleComponentIdsToSelect =
              new SelectedComponentUtil().getPossibleComponentToSelect_Test(sCExtendedOfCurrentStep)

            // Status Component Typ
            val sCExtendedOfStatusComponentTyp = verifyStatusComponentType(sCExtendedOfPossibleComponentIdsToSelect)

            //Test -->

            //Status Exclude Dependency
            val sCExtendedOfStatusExcludeDependency =
              verifyStatusExcludeDependencyInStepsInternalComponents(sCExtendedOfStatusComponentTyp)

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
        val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
          case Some(step) => step.components
          case None => List()
        }
//
//        val currentConfigWithTempSelectedComponent: List[ComponentBO] =
//          previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get
//
//        val possibleComponentToSelect: List[String] =
//          new SelectedComponentUtil().getPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
//          selectedComponentBO, previousSelectedComponentsInCurrentConfig)

        selectedComponentBO.possibleComponentIdsToSelect.get match {
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
          selectedComponentBO.stepCurrentConfig.get.components.exists(_.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get)

        if (componentIdExist) {

          val countOfComponentsInCurrentConfig: Int = selectedComponentBO.stepCurrentConfig match {
            case Some(step) if step.components.size <= 1 =>  step.components.size - 1
            case Some(step) if step.components.isEmpty =>  0
            case None => 0
          }

          val statusSC: StatusSelectionCriterium =
            new SelectedComponentUtil().
              getSelectionCriteriumStatus(countOfComponentsInCurrentConfig, selectedComponentBO.currentStep.get)

          val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
          selectedComponentBO.copy(status = Some(status))

        }else{

          //TODO im decision_tree nachziehen.
          selectedComponentBO.possibleComponentIdsToSelect.get match {
            case List() =>
              val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(RequireNextStep()))

              selectedComponentBO.copy(status = Some(status))
            case _ =>
              val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
                case Some(step) => step.components
                case None => List()
              }
              //
              val currentConfigWithTempSelectedComponent: List[ComponentBO] =
                previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get
              //
              //          val possibleComponentToSelect: List[String] =
              //            new SelectedComponentUtil().getPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
              //            selectedComponentBO, previousSelectedComponentsInCurrentConfig)

              val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

              val statusSC: StatusSelectionCriterium = new SelectedComponentUtil().
                getSelectionCriteriumStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent,
                  selectedComponentBO.currentStep.get)

              val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))

              selectedComponentBO.copy(status = Some(status))

          }
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
          selectedComponentBO.stepCurrentConfig.get.components
            .exists(_.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get)

        if(componentIdExist) {
          //Die Komponente muss aus der CurrentConfig gelöscht werden
          CurrentConfig.removeComponent(selectedComponentBO)

          val status = selectedComponentBO.status.get.copy(selectedComponent = Some(RemovedComponent()))

          selectedComponentBO.copy(status = Some(status))

        }else {

          // TODO Zweimal ausgeführt wird -->
//          val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
//            case Some(step) => step.components
//            case None => List()
//          }
//
//          val currentConfigWithTempSelectedComponent: List[ComponentBO] =
//            previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get
//
//          val possibleComponentToSelect: List[String] =
//            new SelectedComponentUtil().getPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
//              selectedComponentBO, previousSelectedComponentsInCurrentConfig)
          //TODO <--

          selectedComponentBO.status.get.selectionCriterium.get match {
            case RequireComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component

//              new SelectedComponentUtil().getComponentBOByStatusRequireComponent(selectedComponentBO)

            case RequireNextStep() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component

//              new SelectedComponentUtil().getComponentBOByStatusRequireNextStep(selectedComponentBO, selectedComponentBO.stepCurrentConfig)

            case AllowNextComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component

//              new SelectedComponentUtil().getComponentBOByStatusAllowNextComponent(selectedComponentBO)
            case NotAllowNextComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(NotAllowedComponent()))

              selectedComponentBO.copy(status = Some(status))
          }
        }
    }
  }

  private def verifyStatusExcludeDependencyInStepsInternalComponents(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val previousSelectedComponentIdsFromCurrentConfig: List[String] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components map (_.componentId.get)
      case None => List()
    }

    val excludedDependencyOutIds = selectedComponentBO.selectedComponent.get.excludeDependenciesIn.get map (_.outId)

    val excludeComponentsIds: List[String] = previousSelectedComponentIdsFromCurrentConfig flatMap {
      sCId => excludedDependencyOutIds filter { inECId => sCId == inECId }
    }

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

    val currentStepBO: StepBO = Persistence.getCurrentStep(selectedComponentBO.selectedComponent.get.componentId.get)

    val nextStep: StepBO = Persistence.getNextStep(selectedComponentBO.selectedComponent.get.componentId.get)

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

    selectedComponentBO.copy(stepCurrentConfig = currentStepCurrentConfig)
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
    }

    val statusComponent: StatusComponent = selectedComponentBO.status.get.copy(componentType = Some(componentTypeStatus))

    selectedComponentBO.copy(status = Some(statusComponent))
  }


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return ComponentOut
    */
//  private def setCaseDefault(): SelectedComponentBO = {
//
//    SelectedComponentBO(status = Some(StatusComponent(None, None, None, None, None)))
//    //    ComponentOut(
//    //      "",
//    //      "",
//    //      StatusComponent(None, None, None, None, None),
//    //      List()
//    //    )
//  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return ComponentOut
    */
//  private def setCase1_2_3_4_5_6_7_8_9_10(selectedComponent: SelectedComponentBO, fatherStepId: String): SelectedComponentBO = {
//    SelectedComponentBO(
//      status = selectedComponent.status,
//      selectedComponent = Some(ComponentBO(
//        componentId = selectedComponent.selectedComponent.get.componentId,
//        excludeDependenciesOut = selectedComponent.selectedComponent.get.excludeDependenciesOut,
//        excludeDependenciesIn = selectedComponent.selectedComponent.get.excludeDependenciesIn,
//        requireDependenciesOut = selectedComponent.selectedComponent.get.requireDependenciesOut,
//        requireDependenciesIn = selectedComponent.selectedComponent.get.requireDependenciesIn)
//      ),
//      currentStep = Some(StepBO(stepId = Some(fatherStepId)))
//    )
//  }

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

//    val selectedComponentStatusComponentType: SelectedComponentBO = selectedComponentBO

//    val dependencies: List[DependencyBO] =
//      selectedComponentStatusComponentType.selectedComponent.get.requireDependenciesOut.get ::: selectedComponentStatusComponentType.selectedComponent.get.excludeDependenciesOut.get

    //                                   selectionCriterium           selectedComponent           excludeDependency                common          componentType
    //Scenario 6
//    val statusCase1 = StatusComponent(Some(AllowNextComponent()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 7
//    val statusCase2 = StatusComponent(Some(RequireNextStep()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 8
//    val statusCase3 = StatusComponent(Some(RequireNextStep()), Some(NotAllowedComponent()), Some(ExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 9
//    val statusCase4 = StatusComponent(Some(AllowNextComponent()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 10
//    val statusCase5 = StatusComponent(Some(RequireComponent()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 16
//    val statusCase6 = StatusComponent(Some(RequireNextStep()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//    //Scenario 5
//    val statusCase7 = StatusComponent(Some(RequireNextStep()), Some(AddedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(FinalComponent()))
//
//    val statusCase8 = StatusComponent(Some(RequireNextStep()), Some(RemovedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//
//    val statusCase9 = StatusComponent(Some(AllowNextComponent()), Some(NotAllowedComponent()), Some(ExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//
//    val statusCase10 = StatusComponent(Some(NotAllowNextComponent()), Some(NotAllowedComponent()), Some(NotExcludedComponent()), Some(Success()), Some(DefaultComponent()))
//
//    val statusCaseDefault = StatusComponent(_, _, _, _, _)
//
//    val status: StatusComponent = selectedComponentStatusComponentType.status.get
//
//    (status: @unchecked) match {
//      case `statusCase1` => Logger.info(this.getClass.getSimpleName + " : Case 1")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase2` => Logger.info(this.getClass.getSimpleName + " : Case 2 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase3` => Logger.info(this.getClass.getSimpleName + " : Case 3 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase4` => Logger.info(this.getClass.getSimpleName + " : Case 4 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase5` => Logger.info(this.getClass.getSimpleName + " : Case 5 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase6` => Logger.info(this.getClass.getSimpleName + " : Case 5 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase7` => Logger.info(this.getClass.getSimpleName + " : Case 7 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase8` => Logger.info(this.getClass.getSimpleName + " : Case 8 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase9` => Logger.info(this.getClass.getSimpleName + " : Case 9 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCase10` => Logger.info(this.getClass.getSimpleName + " : Case 10 ")
//        setCase1_2_3_4_5_6_7_8_9_10(selectedComponentStatusComponentType, selectedComponentBO.currentStep.get.stepId.get)
//      case `statusCaseDefault` => println("Undefined Status")
//        setCaseDefault()
//    }
    selectedComponentBO
  }
}