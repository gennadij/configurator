package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.{Status, Success}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2018
  */

object SelectedComponent  {

  def verifySelectedComponent(selectedComponentBO: SelectedComponentBO, currentConfig: CurrentConfig): SelectedComponentBO = {
    new SelectedComponent(selectedComponentBO, currentConfig).selectedComponent
  }
}

class SelectedComponent(selectedComponentBO: SelectedComponentBO, currentConfig: CurrentConfig)
  extends SelectedComponentUtil{

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

            //Status Exclude Dependency
            val sCExtendedOfStatusExcludeDependency =
              verifyStatusExcludeDependencyInStepsInternalComponents(sCExtendedOfStatusComponentTyp)

            val sCExtendedOfPossibleComponentIdsToSelect =
              getPossibleComponentToSelect(sCExtendedOfStatusExcludeDependency)

            //Status Selection Criterium
            val sCExtendedOfStatusSelectionCriterium = verifyStatusSelectionCriterium(sCExtendedOfPossibleComponentIdsToSelect)

            // Status Selected Component
            val sCExtendedOFStatusSelectedComponent =
              verifyStatusSelectedComponent(sCExtendedOfStatusSelectionCriterium, currentConfig)

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
}