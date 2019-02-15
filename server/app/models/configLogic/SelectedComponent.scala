package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.{CurrentConfigContainerBO, CurrentConfigStepBO}
import models.bo.step.StepContainerBO
import models.bo.warning.WarningBO
import models.persistence.Persistence
import org.shared.error.StepNotExist

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */
object SelectedComponent  {

  def verifySelectedComponent(selectedComponentBO: SelectedComponentContainerBO, currentConfigContainerBO: CurrentConfigContainerBO): SelectedComponentContainerBO = {
    new SelectedComponent(selectedComponentBO,
      currentConfigContainerBO
    ).selectedComponent
  }
}

class SelectedComponent (
                         selectedComponentBO: SelectedComponentContainerBO,
                         currentConfigContainerBO: CurrentConfigContainerBO
                       )
  extends Dependency with SelectionCriterion with CurrentConfig {

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
              getCurrentStepFromCurrentConfig(sCExtendedOfCurrentAndNextStep, currentConfigContainerBO
              )

            // Is selected component final component
            val sCExtendedOfStatusComponentTyp = isSelectedComponentFinalComponent(sCExtendedOfCurrentConfigStep)

            //Warning Exclude Dependency for internal Components
            val sCExtendedOfStatusExcludeDependencyInternal =
              verifyExcludeDependencyInForInternal(sCExtendedOfStatusComponentTyp)

            //Warning Exclude Dependency for external Components
            val sCExtendedOfStatusExcludeDependencyInExternal =
              verifyExcludeDependencyInForExternal(sCExtendedOfStatusExcludeDependencyInternal, currentConfigContainerBO)

            val sCExtendedOfStatusExcludeDependencyOutExternal =
              verifyExcludeDependencyOutForExternal(sCExtendedOfStatusExcludeDependencyInExternal, currentConfigContainerBO)

            val sCExtendedOfPossibleComponentIdsToSelect =
              getPossibleComponentToSelect(sCExtendedOfStatusExcludeDependencyOutExternal)

            //Status Selection Criterion
            val sCExtendedOfInfoSelectionCriterion = verifySelectionCriterion(sCExtendedOfPossibleComponentIdsToSelect)

            // Is selected componen added to CurrentConfig
            val sCExtendedOFStatusSelectedComponent =
              isSelectedComponentAddedComponent(sCExtendedOfInfoSelectionCriterion, currentConfigContainerBO)

            if(sCExtendedOFStatusSelectedComponent.selectedComponent.get.addedComponent.get) {

              addComponent(
                currentConfigContainerBO,
                sCExtendedOFStatusSelectedComponent.stepCurrentConfig.get,
                sCExtendedOFStatusSelectedComponent.selectedComponent.get)

              sCExtendedOFStatusSelectedComponent

            }else{
              removeComponentInternal(currentConfigContainerBO, sCExtendedOFStatusSelectedComponent)
              sCExtendedOFStatusSelectedComponent
            }
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
  private def getCurrentStepFromCurrentConfig(
                                               selectedComponentBO: SelectedComponentContainerBO,
                                               currentConfigContainerBO: CurrentConfigContainerBO
                                             ): SelectedComponentContainerBO = {

    val currentStepCurrentConfig: Option[CurrentConfigStepBO] =
      getCurrentConfigStep(
        currentConfigContainerBO,
        selectedComponentBO.currentStep.get.step.get.stepId.get)

    selectedComponentBO.copy(stepCurrentConfig = currentStepCurrentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentContainerBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private def isSelectedComponentFinalComponent(
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
  private[configLogic] def isSelectedComponentAddedComponent(
                                                          selectedComponentContainerBO: SelectedComponentContainerBO,
                                                        currentConfigContainerBO: CurrentConfigContainerBO
                                                        ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.warning.getOrElse(WarningBO()).excludedComponentInternal match {
        case Some(_) =>

          val selectedComponent : SelectedComponentBO = selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))
          selectedComponentContainerBO.copy(selectedComponent = Some(selectedComponent))
        case None =>
          val isRepeatedSelektionOfComponent: Boolean =
            selectedComponentContainerBO.stepCurrentConfig.get.components
              .exists(_.componentId.get == selectedComponentContainerBO.selectedComponent.get.componentId.get)

          if(isRepeatedSelektionOfComponent) {
            //Die Komponente muss aus der CurrentConfig gelöscht werden
            // Wiederholte Auswahl der Komponente
            val componentBO: SelectedComponentBO =
              selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))

            selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))

          }else {

            selectedComponentContainerBO.selectedComponent.get.addedComponent match {
              case Some(addedComponent) =>
                if(addedComponent) {
                  val componentBO: SelectedComponentBO =
                    selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(true))
                  selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
                }else{
                  val componentBO: SelectedComponentBO =
                    selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))
                  selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
                }
              case None =>
                val componentBO: SelectedComponentBO =
                  selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(true))
                selectedComponentContainerBO.copy(selectedComponent = Some(componentBO))
            }
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

    val previousSelectedComponentsInCurrentConfig: List[SelectedComponentBO] = selectedComponentContainerBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val previousSelectedComponentsInCurrentConfigIds: List[String] = previousSelectedComponentsInCurrentConfig map (_.componentId.get)

    val fromCurrentConfigExcludedComponentIds: Set[String] = (previousSelectedComponentsInCurrentConfig flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    val fromSelectedComponentExcludedComponentIds: List[String] =
      selectedComponentContainerBO.selectedComponent.get.excludeDependenciesOut.get map (_.inId)


    val unselectedComponentIds: List[String] = (selectedComponentContainerBO.currentStep.get.componentsForSelection.get.toList map(_.componentId.get)) filterNot (c => { //TODO verbessern
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