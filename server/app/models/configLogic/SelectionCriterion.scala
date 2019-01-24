package models.configLogic

import models.bo.{ComponentBO, SelectedComponentBO, StepBO}
import org.shared.status.selectedComponent._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 04.01.2019
  */
trait SelectionCriterion {

  private[configLogic] def verifySelectionCriterium(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.excludedDependencyInternal.get match {
      case ExcludedComponentInternal() =>
        val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
          case Some(step) => step.components
          case None => List()
        }
        selectedComponentBO.possibleComponentIdsToSelect.get match {
          case List() =>
            //RequireNextStep
            val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(RequireNextStep()))
            selectedComponentBO.copy(status = Some(status))
          case _ =>
            val statusSC: StatusSelectionCriterion =
              getSelectionCriterionStatus(previousSelectedComponentsInCurrentConfig.size,
                selectedComponentBO.currentStep.get.step.get)
            val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(statusSC))
            selectedComponentBO.copy(status = Some(status))
        }

      case NotExcludedComponentInternal() =>
        val componentIdExist: Boolean =
          selectedComponentBO.stepCurrentConfig.get.components.exists(
            _.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get
          )

        if (componentIdExist) {

          val countOfComponentsInCurrentConfig: Int = selectedComponentBO.stepCurrentConfig match {
            case Some(step) if step.components.nonEmpty =>  step.components.size - 1
            case Some(step) if step.components.isEmpty =>  0
            case None => 0
          }

          val statusSC: StatusSelectionCriterion =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfig, selectedComponentBO.currentStep.get.step.get)

          val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(statusSC))
          selectedComponentBO.copy(status = Some(status))

        }else{

          //TODO im decision_tree nachziehen.
          val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
            case Some(step) => step.components
            case None => List()
          }
          val currentConfigWithTempSelectedComponent: List[ComponentBO] =
            previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get

          val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

          val statusSC: StatusSelectionCriterion =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent,
              selectedComponentBO.currentStep.get.step.get)

          statusSC match {
            case AllowNextComponent() =>
              selectedComponentBO.possibleComponentIdsToSelect.get match {
                case List() =>
                  val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(RequireNextStep()))

                  selectedComponentBO.copy(status = Some(status))
                case _ =>
                  val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(statusSC))

                  selectedComponentBO.copy(status = Some(status))
              }
            case _ =>
              val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(statusSC))

              selectedComponentBO.copy(status = Some(status))
          }
        }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param countOfComponents : Int, fatherStep: StepBO
    * @return StatusSelectionCriterium
    */
  private def getSelectionCriterionStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterion = {

    val min: Int = fatherStep.selectionCriterionMin.get
    val max: Int = fatherStep.selectionCriterionMax.get

    countOfComponents match {
      case count if min > count && max > count => RequireComponent()
      case count if min <= count && max == count => RequireNextStep()
      case count if min <= count && max > count => AllowNextComponent()
      case count if max < count => NotAllowNextComponent()
      case _ => ErrorSelectionCriterion()
    }
  }
}
