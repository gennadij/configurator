package models.configLogic

import models.bo.component.{ComponentBO, SelectedComponentContainerBO}
import models.bo.step.{StepContainerBO, StepCurrentConfigBO}
import models.bo.warning.WarningBO
import models.persistence.Persistence
import org.shared.error.StepNotExist

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */
object SelectedComponent  {

  def verifySelectedComponent(selectedComponentBO: SelectedComponentContainerBO, currentConfig: CurrentConfig): SelectedComponentContainerBO = {
    new SelectedComponent(selectedComponentBO, currentConfig).selectedComponent
  }
}

class SelectedComponent(selectedComponentBO: SelectedComponentContainerBO, currentConfig: CurrentConfig)
  extends Dependency with SelectionCriterion {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @return StepBO
    */
  private def selectedComponent: SelectedComponentContainerBO = {

    val selectedComponentId: String = selectedComponentBO.selectedComponent.get.componentId.get

    val sCBO: SelectedComponentContainerBO = Persistence.getSelectedComponent(selectedComponentId)

    sCBO.errors match {
      case None =>

        val sCExtendedOfCurrentAndNextStep: SelectedComponentContainerBO =
          getCurrentAndNextStepFromPersistence(sCBO)

        val (errorCurrentStep, errorNextStep) =
          (sCExtendedOfCurrentAndNextStep.currentStep.get.error,
            sCExtendedOfCurrentAndNextStep.nextStep.get.error)

        (errorCurrentStep, errorNextStep) match {
          case (None, None) | (None, Some(List(_: StepNotExist))) =>

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
            val sCExtendedOfStatusSelectionCriterion = verifySelectionCriterion(sCExtendedOfPossibleComponentIdsToSelect)

            // Status Selected Component
            val sCExtendedOFStatusSelectedComponent =
              verifyStatusSelectedComponent(sCExtendedOfStatusSelectionCriterion, currentConfig)

            sCExtendedOFStatusSelectedComponent

          case _ => sCExtendedOfCurrentAndNextStep
        }
      case Some(_) => sCBO
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return selectedComponentBO
    */
  private def getCurrentAndNextStepFromPersistence(
                                                    selectedComponentBO: SelectedComponentContainerBO
                                                  ): SelectedComponentContainerBO = {

    val currentStepBO: StepContainerBO = Persistence.getCurrentStep(selectedComponentBO.selectedComponent.get.componentId.get)

    val nextStep: StepContainerBO = Persistence.getStep(componentId = selectedComponentBO.selectedComponent.get.componentId)

    selectedComponentBO.copy(currentStep = Some(currentStepBO), nextStep = Some(nextStep))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return StepBO
    */
  private def getCurrentStepFromCurrentConfig(selectedComponentBO: SelectedComponentContainerBO,
                                              currentConfig: CurrentConfig): SelectedComponentContainerBO = {

    val currentStepCurrentConfig: Option[StepCurrentConfigBO] =
      currentConfig.getCurrentStep(selectedComponentBO.currentStep.get.step.get.stepId.get)

    selectedComponentBO.copy(stepCurrentConfig = currentStepCurrentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentContainerBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private def verifyStatusComponentType(
                                         selectedComponentContainerBO: SelectedComponentContainerBO
                                       ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.nextStep.get.step match {
      case Some(_) =>
        val componentBO = selectedComponentContainerBO.selectedComponent.get.copy(lastComponent = Some(false))
        selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
      case None =>
        val componentBO = selectedComponentContainerBO.selectedComponent.get.copy(lastComponent = Some(true))
        selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentContainerBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyStatusSelectedComponent(
                                                          selectedComponentContainerBO: SelectedComponentContainerBO, currentConfig: CurrentConfig
                                                        ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.warning.getOrElse(WarningBO()).excludedComponentInternal match {
        case Some(_) =>
          val selectedComponent : ComponentBO = selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))
          selectedComponentContainerBO.copy(selectedComponent = Some(selectedComponent))
        case None =>
          val isRepeatedSelektionOfComponent: Boolean =
            selectedComponentContainerBO.stepCurrentConfig.get.components
              .exists(_.componentId.get == selectedComponentContainerBO.selectedComponent.get.componentId.get)

          if(isRepeatedSelektionOfComponent) {
            //Die Komponente muss aus der CurrentConfig gelöscht werden
            // Wiederholte Auswahl der Komponente
            currentConfig.removeComponent(selectedComponentContainerBO)

            val componentBO: ComponentBO =
              selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))
            selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))

          }else {
            val componentBO: ComponentBO =
              selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(true))

            //TODO Die Komponente soll nach allen Pruefungen hinzugefügt werden.
            currentConfig.addComponent(
              selectedComponentContainerBO.stepCurrentConfig.get, selectedComponentContainerBO.selectedComponent.get)

            selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
          }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentContainerBO: SelectedComponentBO
    * @return List[String]
    */
  private[configLogic] def getPossibleComponentToSelect(
                                                         selectedComponentContainerBO: SelectedComponentContainerBO
                                                       ): SelectedComponentContainerBO = {

    val selectedComponentId = selectedComponentContainerBO.selectedComponent.get.componentId.get

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentContainerBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val previousSelectedComponentsInCurrentConfigIds: List[String] = previousSelectedComponentsInCurrentConfig map (_.componentId.get)

    val fromCurrentConfigExcludedComponentIds: Set[String] = (previousSelectedComponentsInCurrentConfig flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    val fromSelectedComponentExcludedComponentIds: List[String] =
      selectedComponentContainerBO.selectedComponent.get.excludeDependenciesOut.get map (_.inId)


    val unselectedComponentIds: List[String] = (
      selectedComponentContainerBO.currentStep.get.componentsForSelection.get.toList map(_.componentId.get)
      ) filterNot (c => {
      (previousSelectedComponentsInCurrentConfigIds :::
        fromCurrentConfigExcludedComponentIds.toList.+:(selectedComponentId)).contains(c)
    })

    val unselectedExcludedComponentsFromCurrentConfigComponentsIds: List[String] = unselectedComponentIds.filter(uC => {
      fromCurrentConfigExcludedComponentIds.contains(uC)
    })

    val unselectedExcludedComponentsFromSelectedComponent: List[String] =
      selectedComponentContainerBO.warning match {
        case Some(warning) => warning.excludedComponentInternal match {
          case Some(_) => List()
          case None => unselectedComponentIds.filter(uC => {fromSelectedComponentExcludedComponentIds.contains(uC)})

        }
        case None => unselectedComponentIds.filter(uC => {fromSelectedComponentExcludedComponentIds.contains(uC)})
      }

    val unselectedExcludedComponents: List[String] =
      unselectedExcludedComponentsFromCurrentConfigComponentsIds ::: unselectedExcludedComponentsFromSelectedComponent


    val possibleComponentToSelect: List[String] =
      unselectedComponentIds filterNot(uSIds => {unselectedExcludedComponents.contains(uSIds)})

    selectedComponentContainerBO.copy(possibleComponentIdsToSelect = Some(possibleComponentToSelect))
  }
}