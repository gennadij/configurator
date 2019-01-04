package models.configLogic

import models.bo._
import models.persistence.Persistence
import org.shared.status.common.{Status, Success}
import org.shared.status.nextStep.{NextStepExist, NextStepNotExist}
import org.shared.status.selectedComponent._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */
//TODO Definition von Geltungsberechen des Statuses und Prüfung auf Widerholten oder widersprechenden Definitionen
object SelectedComponent  {

  def verifySelectedComponent(selectedComponentBO: SelectedComponentBO, currentConfig: CurrentConfig): SelectedComponentBO = {
    new SelectedComponent(selectedComponentBO, currentConfig).selectedComponent
  }
}

class SelectedComponent(selectedComponentBO: SelectedComponentBO, currentConfig: CurrentConfig)
  extends Dependency with SelectionCriterion {

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

            val sCExtendedOfCurrentConfigStep =
              getCurrentStepFromCurrentConfig(sCExtendedOfCurrentAndNextStep, currentConfig)

            // Status Component Typ
            val sCExtendedOfStatusComponentTyp = verifyStatusComponentType(sCExtendedOfCurrentConfigStep)

            //Status Exclude Dependency for internal Components
            val sCExtendedOfStatusExcludeDependencyInternal =
              verifyExcludeDependencyInForInternal(sCExtendedOfStatusComponentTyp)

            //Status Exclude Dependency for external Components
            val sCExtendedOfStatusExcludeDependencyExternal =
              verifyExcludeDependencyInForExternal(sCExtendedOfStatusExcludeDependencyInternal)

            val sCExtendedOfPossibleComponentIdsToSelect =
              getPossibleComponentToSelect(sCExtendedOfStatusExcludeDependencyExternal)

            //Status Selection Criterion
            val sCExtendedOfStatusSelectionCriterion = verifySelectionCriterium(sCExtendedOfPossibleComponentIdsToSelect)

            // Status Selected Component
            val sCExtendedOFStatusSelectedComponent =
              verifyStatusSelectedComponent(sCExtendedOfStatusSelectionCriterion, currentConfig)

            sCExtendedOFStatusSelectedComponent

          case _ => sCExtendedOfCurrentAndNextStep
        }
      case _ => sCBO
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
  private def getCurrentStepFromCurrentConfig(selectedComponentBO: SelectedComponentBO,
                                              currentConfig: CurrentConfig): SelectedComponentBO = {

    val currentStepCurrentConfig: Option[StepCurrentConfigBO] =
      currentConfig.getCurrentStep(selectedComponentBO.currentStep.get.stepId.get)

    selectedComponentBO.copy(stepCurrentConfig = currentStepCurrentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private def verifyStatusComponentType(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val componentTypeStatus: StatusComponentType = selectedComponentBO.nextStep.get.status.get.nextStep.get match {
      case NextStepNotExist() => FinalComponent()
      case NextStepExist() => DefaultComponent()
      case _ => UnknownComponentType()
    }

    val statusComponent: StatusComponent = selectedComponentBO.status.get.copy(componentType = Some(componentTypeStatus))

    selectedComponentBO.copy(status = Some(statusComponent))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyStatusSelectedComponent(selectedComponentBO: SelectedComponentBO, currentConfig: CurrentConfig): SelectedComponentBO = {
    selectedComponentBO.status.get.excludedDependencyInternal.get match {
      case ExcludedComponentInternal() =>
        val status = selectedComponentBO.status.get.copy(selectedComponent = Some(NotAllowedComponent()))

        selectedComponentBO.copy(status = Some(status))

      case NotExcludedComponentInternal() =>
        val isRepeatedSelektionOfComponent: Boolean =
          selectedComponentBO.stepCurrentConfig.get.components
            .exists(_.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get)

        if(isRepeatedSelektionOfComponent) {
          //Die Komponente muss aus der CurrentConfig gelöscht werden
          // Wiederholte Auswahl der Komponente
          currentConfig.removeComponent(selectedComponentBO)

          val status = selectedComponentBO.status.get.copy(selectedComponent = Some(RemovedComponent()))

          selectedComponentBO.copy(status = Some(status))

        }else {
          selectedComponentBO.status.get.selectionCriterion.get match {
            case RequireComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              currentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component
            case RequireNextStep() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              currentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component

            case AllowNextComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(AddedComponent()))

              val component =
                selectedComponentBO.copy(status = Some(status))

              currentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

              component

            case NotAllowNextComponent() =>

              val status = selectedComponentBO.status.get.copy(selectedComponent = Some(NotAllowedComponent()))

              selectedComponentBO.copy(status = Some(status))
            case ErrorSelectionCriterion() =>
              val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(ErrorSelectionCriterion()),
                selectedComponent = Some(ErrorSelectedComponent()))

              selectedComponentBO.copy(status = Some(status))
          }
        }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO: SelectedComponentBO
    * @return List[String]
    */
  private[configLogic] def getPossibleComponentToSelect(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val selectedComponentId = selectedComponentBO.selectedComponent.get.componentId.get

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val previousSelectedComponentsInCurrentConfigIds: List[String] = previousSelectedComponentsInCurrentConfig map (_.componentId.get)

    val fromCurrentConfigExcludedComponentIds: Set[String] = (previousSelectedComponentsInCurrentConfig flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    val fromSelectedComponentExcludedComponentIds: List[String] =
      selectedComponentBO.selectedComponent.get.excludeDependenciesOut.get map (_.inId)

    val unselectedComponentIds: List[String] = selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
      (previousSelectedComponentsInCurrentConfigIds :::
        fromCurrentConfigExcludedComponentIds.toList.+:(selectedComponentId)).contains(c)
    })

    val unselectedExcludedComponentsFromCurrentConfigComponentsIds: List[String] = unselectedComponentIds.filter(uC => {
      fromCurrentConfigExcludedComponentIds.contains(uC)
    })

    val unselectedExcludedComponentsFromSelectedComponent: List[String] = selectedComponentBO.status.get.excludedDependencyInternal.get match {
      case ExcludedComponentInternal() => List()
      case NotExcludedComponentInternal() => unselectedComponentIds.filter(uC => {fromSelectedComponentExcludedComponentIds.contains(uC)})
    }


    val unselectedExcludedComponents: List[String] =
      unselectedExcludedComponentsFromCurrentConfigComponentsIds ::: unselectedExcludedComponentsFromSelectedComponent


    val possibleComponentToSelect: List[String] =
      unselectedComponentIds filterNot(uSIds => {unselectedExcludedComponents.contains(uSIds)})

    selectedComponentBO.copy(possibleComponentIdsToSelect = Some(possibleComponentToSelect))
  }
}