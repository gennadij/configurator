package models.configLogic

import models.bo._
import org.shared.info._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 04.01.2019
  */
trait SelectionCriterion {

  private[configLogic] def verifySelectionCriterion(
                                                     selectedComponentContainerBO: SelectedComponentContainerBO
                                                   ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.warning.getOrElse(WarningBO()).excludedComponentInternal match {
      case Some(_) =>
//    selectedComponentBO.status.get.excludedDependencyInternal.get match {
//      case ExcludedComponentInternal() =>
        val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentContainerBO.stepCurrentConfig match {
          case Some(step) => step.components
          case None => List()
        }
        selectedComponentContainerBO.possibleComponentIdsToSelect.get match {
          case List() =>
            //RequireNextStep
            //TODO Info is None
            val infoBO = selectedComponentContainerBO.info match {
              case Some(info) =>
                selectedComponentContainerBO.info.get.copy(selectionCriterion = Some(RequireNextStep()))
              case None =>
                InfoBO(
                  selectionCriterion = Some(RequireNextStep())
                )
            }

            selectedComponentContainerBO.copy(info = Some(infoBO))

//            val status = selectedComponentBO.status.get.copy(selectionCriterion = Some(RequireNextStep()))
//            selectedComponentBO.copy(status = Some(status))
          case _ =>
            val info: Info =
              getSelectionCriterionStatus(previousSelectedComponentsInCurrentConfig.size,
                selectedComponentContainerBO.currentStep.get.step.get)
//            val status = selectedComponentContainerBO.status.get.copy(selectionCriterion = Some(statusSC))
//            selectedComponentContainerBO.copy(status = Some(status))
            selectedComponentContainerBO.copy(info = Some(InfoBO(selectionCriterion = Some(info))))
        }
      case None =>
//      case NotExcludedComponentInternal() =>
        val componentIdExist: Boolean =
          selectedComponentContainerBO.stepCurrentConfig.get.components.exists(
            _.componentId.get == selectedComponentContainerBO.selectedComponent.get.componentId.get
          )

        if (componentIdExist) {

          val countOfComponentsInCurrentConfig: Int = selectedComponentContainerBO.stepCurrentConfig match {
            case Some(step) if step.components.nonEmpty =>  step.components.size - 1
            case Some(step) if step.components.isEmpty =>  0
            case None => 0
          }

          val info : Info =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfig, selectedComponentContainerBO.currentStep.get.step.get)

//          val status = selectedComponentContainerBO.status.get.copy(selectionCriterion = Some(statusSC))
//          selectedComponentContainerBO.copy(status = Some(status))
            selectedComponentContainerBO.copy(info = Some(InfoBO(selectionCriterion = Some(info))))
        }else{

          //TODO im decision_tree nachziehen.
          val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentContainerBO.stepCurrentConfig match {
            case Some(step) => step.components
            case None => List()
          }
          val currentConfigWithTempSelectedComponent: List[ComponentBO] =
            previousSelectedComponentsInCurrentConfig :+ selectedComponentContainerBO.selectedComponent.get

          val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

          val info: Info =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent,
              selectedComponentContainerBO.currentStep.get.step.get)

          info match {
            case AllowNextComponent() =>
              selectedComponentContainerBO.possibleComponentIdsToSelect.get match {
                case List() =>
//                  val status = selectedComponentContainerBO.status.get.copy(selectionCriterion = Some(RequireNextStep()))

//                  selectedComponentContainerBO.copy(status = Some(status))
                  selectedComponentContainerBO.copy(info = Some(InfoBO(selectionCriterion = Some(RequireNextStep()))))
                case _ =>
//                  val status = selectedComponentContainerBO.status.get.copy(selectionCriterion = Some(info))

//                  selectedComponentContainerBO.copy(status = Some(status))
                  selectedComponentContainerBO.copy(info = Some(InfoBO(selectionCriterion = Some(info))))
              }
            case _ =>
//              val status = selectedComponentContainerBO.status.get.copy(selectionCriterion = Some(info))

//              selectedComponentContainerBO.copy(status = Some(status))
              selectedComponentContainerBO.copy(info = Some(InfoBO(selectionCriterion = Some(info))))
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
  private def getSelectionCriterionStatus(countOfComponents: Int, fatherStep: StepBO): Info = {

    val min: Int = fatherStep.selectionCriterionMin.get
    val max: Int = fatherStep.selectionCriterionMax.get

    countOfComponents match {
      case count if min > count && max > count => RequireComponent()
      case count if min <= count && max == count => RequireNextStep()
      case count if min <= count && max > count => AllowNextComponent()
      case count if max < count => NotAllowedComponent()
    }
  }
}
