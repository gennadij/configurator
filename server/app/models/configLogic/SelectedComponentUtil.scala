package models.configLogic

import models.bo.{ComponentBO, SelectedComponentBO, StepBO}
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.status.nextStep.{NextStepExist, NextStepNotExist}
import org.shared.status.selectedComponent._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.02.2018
  */

trait SelectedComponentUtil {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyStatusComponentType(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val componentTypeStatus: StatusComponentType = selectedComponentBO.nextStep.get.status.get.nextStep.get match {
      case NextStepNotExist() => FinalComponent()
      case NextStepExist() => DefaultComponent()
      case _ => UnknowComponentType()
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
  private[configLogic] def verifyStatusExcludeDependencyInStepsInternalComponents(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

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
          selectedComponentBO.status.get.copy(excludedDependencyInternal = Some(NotExcludedComponentInternal()))
        selectedComponentBO.copy(status = Some(statusNotExcludedComponent))
      case _ =>
        val statusExcludedComponent: StatusComponent =
          selectedComponentBO.status.get.copy(excludedDependencyInternal = Some(ExcludedComponentInternal()))
        selectedComponentBO.copy(status = Some(statusExcludedComponent))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyStatusSelectionCriterium(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.status.get.excludedDependencyInternal.get match {
      case ExcludedComponentInternal() =>
        val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
          case Some(step) => step.components
          case None => List()
        }
        selectedComponentBO.possibleComponentIdsToSelect.get match {
          case List() =>
            //RequireNextStep
            val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(RequireNextStep()))
            selectedComponentBO.copy(status = Some(status))
          case _ =>
            val statusSC: StatusSelectionCriterium =
              getSelectionCriterionStatus(previousSelectedComponentsInCurrentConfig.size,
                selectedComponentBO.currentStep.get)
            val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
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

          val statusSC: StatusSelectionCriterium =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfig, selectedComponentBO.currentStep.get)

          val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))
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

          val statusSC: StatusSelectionCriterium =
            getSelectionCriterionStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent,
              selectedComponentBO.currentStep.get)

          statusSC match {
            case AllowNextComponent() =>
              selectedComponentBO.possibleComponentIdsToSelect.get match {
                case List() =>
                  val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(RequireNextStep()))

                  selectedComponentBO.copy(status = Some(status))
                case _ =>
                  val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))

                  selectedComponentBO.copy(status = Some(status))
              }
            case _ =>
              val status = selectedComponentBO.status.get.copy(selectionCriterium = Some(statusSC))

              selectedComponentBO.copy(status = Some(status))
          }
        }
    }
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
        val componentIdExist: Boolean =
          selectedComponentBO.stepCurrentConfig.get.components
            .exists(_.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get)

        if(componentIdExist) {
          //Die Komponente muss aus der CurrentConfig gelöscht werden
          currentConfig.removeComponent(selectedComponentBO)

          val status = selectedComponentBO.status.get.copy(selectedComponent = Some(RemovedComponent()))

          selectedComponentBO.copy(status = Some(status))

        }else {
          selectedComponentBO.status.get.selectionCriterium.get match {
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

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param countOfComponents : Int, fatherStep: StepBO
    * @return StatusSelectionCriterium
    */
  def getSelectionCriterionStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterium = {

    val min: Int = fatherStep.selectionCriteriumMin.get
    val max: Int = fatherStep.selectionCriteriumMax.get

    countOfComponents match {
      case count if min > count && max > count => RequireComponent()
      case count if min <= count && max == count => RequireNextStep()
      case count if min <= count && max > count => AllowNextComponent()
      case count if max < count => NotAllowNextComponent()
      case _ => ErrorSelectionCriterion()
    }
  }

  def verifyStatusExcludeDependencyInExternal(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.selectedComponent.get.excludeDependenciesIn.get match {
      case List() => selectedComponentBO
      case dependencyIn =>

        val excludeComponentsId: List[String] =
          dependencyIn map (_.outId)

//        val excludedComponentsId: List[String] =
//          dependencyIn map (_.inId)

        val internComponents: List[String] = selectedComponentBO.currentStep.get.componentIds.get

        val excludeComponentsIdExternal: List[String] =
          excludeComponentsId map (eCsId => internComponents exists (iCs => iCs == eCsId)) zip (excludeComponentsId) filterNot (_._1) map (_._2)

//        val excludedComponentsIdExternal: List[String] =
//          excludedComponentsId map (eCsId => internComponents exists (iCs => iCs == eCsId)) zip (excludedComponentsId) filterNot (_._1) map (_._2)

        if (!excludeComponentsIdExternal.isEmpty) {

          //Presentation of the Exclude Status
          val excludeComponents: List[SelectedComponentBO] =
            excludeComponentsIdExternal map (Persistence.getSelectedComponent(_))

//          val excludedComponents: List[SelectedComponentBO] =
//            excludedComponentsId map (Persistence.getSelectedComponent(_))

          val nameToShowExcludeComponents: List[String] = excludeComponents map (_.selectedComponent.get.nameToShow.get)

//          val nameToShowExcludedComponents: List[String] = excludedComponents map (_.selectedComponent.get.nameToShow.get)

          val statusExcludedComponent: StatusComponent =
            selectedComponentBO.status.get.copy(excludedDependencyExternal =
              Some(ExcludedComponentExternal(
                nameToShowExcludeComponents,
                List()))
            )

          selectedComponentBO.copy(status = Some(statusExcludedComponent))
        } else {
          selectedComponentBO
        }
    }
  }
}