package models.logic

import models.bo.{ComponentBO, SelectedComponentBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import org.shared.common.status.{Error, Success}
import org.shared.component.status._
import play.api.Logger

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.02.2018
  */

object SelectedComponentUtil {

  def checkStatusSelectedComponent(currentStep: Option[StepCurrentConfigBO],
                                   selectedComponent: SelectedComponentBO, fatherstep: StepBO): SelectedComponentBO = {
    new SelectedComponentUtil().checkStatusSelectedComponent(currentStep, selectedComponent, fatherstep)
  }
}

class SelectedComponentUtil {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param currentStep : Option[StepCurrentConfigBO], selectedContainerComponentBO: ContainerComponentBO, fatherStep: StepBO
    * @return ContainerComponentBO
    */
  private def checkStatusSelectedComponent(currentStep: Option[StepCurrentConfigBO],
                                           selectedComponentBO: SelectedComponentBO, fatherStep: StepBO): SelectedComponentBO = {

    val selectedcomponentIdsFromCurrentConfig: List[String] = currentStep match {
      case Some(step) => step.components map (_.componentId.get)
      case None => List()
    }

    val excludeComponentsIds: List[String] =
      selectedcomponentIdsFromCurrentConfig flatMap {
        sCId => (selectedComponentBO.component.get.excludeDependenciesIn.get map (_.outId)).filter { inECId => sCId == inECId }
      }

    Logger.info(this.getClass.getSimpleName + ": excludeComponentsIds " + excludeComponentsIds)

    excludeComponentsIds.size match {
      case count if count > 0 =>
        //STATUS EXCLUDED_COMPONENT
        caseExcludedComponent(selectedComponentBO, currentStep, fatherStep)
      case count if count == 0 =>
        //STATUS NOT_EXCLUDED_COMPONENT

        caseNotExcludedComponent(currentStep, selectedComponentBO, fatherStep)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO : SelectedComponentBO, currentStep: Option[StepCurrentConfigBO], fatherStep: StepBO
    * @return SelectedComponentBO
    */
  private def caseExcludedComponent(selectedComponentBO: SelectedComponentBO, currentStep: Option[StepCurrentConfigBO],
                                    fatherStep: StepBO): SelectedComponentBO = {


    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = currentStep match {
      case Some(step) => step.components
      case None => List()
    }

    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.component.get

    val possibleComponentToSelect: List[String] = getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
      fatherStep, selectedComponentBO, previousSelectedComponentsInCurrentConfig)

    possibleComponentToSelect match {
      case List() =>
        selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireNextStep()),
          Some(NotAllowedComponent()),
          Some(ExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))
      case _ =>
        selectedComponentBO.copy(status = Some(StatusComponent(
          Some(getSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, fatherStep)),
          Some(NotAllowedComponent()),
          Some(ExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param currentStep : Option[StepCurrentConfigBO], selectedComponent: SelectedComponentBO,
    *                    fatherStep: StepBO
    * @return SelectedComponentBO
    */
  private def caseNotExcludedComponent(currentStep: Option[StepCurrentConfigBO], selectedComponentBO: SelectedComponentBO,
                                       fatherStep: StepBO): SelectedComponentBO = {

    val componentIdExist: Boolean = currentStep.get.components.exists(_.componentId.get == selectedComponentBO.component.get.componentId.get)

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = currentStep match {
      case Some(step) => step.components
      case None => List()
    }

    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.component.get

    val possibleComponentToSelect: List[String] = getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
      fatherStep, selectedComponentBO, previousSelectedComponentsInCurrentConfig)

    println("possibleComponentToSelect  " + possibleComponentToSelect)

    val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

    if (componentIdExist) {
      CurrentConfig.removeComponent(currentStep.get.stepId, selectedComponentBO.component.get.componentId.get)

      val countOfComponentsInCurrentConfig: Int = currentStep match {
        case Some(step) => step.components.size
        case None => 0
      }

      selectedComponentBO.copy(status = Some(StatusComponent(
        Some(getSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)),
        Some(RemovedComponent()),
        Some(NotExcludedComponent()),
        Some(Success()),
        Some(DefaultComponent())
      )))
    } else {
      //STATUS ADDED_COMPONENT
      getSelectionCriteriumStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent, fatherStep) match {
        case RequireComponent() => getComponentBOByStatusRequireComponent(possibleComponentToSelect,
          selectedComponentBO, currentStep)
        case RequireNextStep() => getComponentBOByStatusRequireNextStep(selectedComponentBO, currentStep)
        case AllowNextComponent() => getComponentBOByStatusAllowNextComponent(possibleComponentToSelect,
          selectedComponentBO, currentStep)
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

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param possibleComponentToSelect: List[String],
    *        selectedComponentBO: SelectedComponentBO,
    *        currentStep: Option[StepCurrentConfigBO]
    * @return SelectedComponentBO
    */
  private def getComponentBOByStatusRequireComponent(possibleComponentToSelect: List[String],
                                                     selectedComponentBO: SelectedComponentBO,
                                                     currentStep: Option[StepCurrentConfigBO]): SelectedComponentBO = {
    possibleComponentToSelect match {
      case List() =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireNextStep()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(currentStep.get, component.component.get)

        component
      case _ =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireComponent()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(currentStep.get, component.component.get)

        component
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO : SelectedComponentBO,
    *        currentStep: Option[StepCurrentConfigBO]
    * @return SelectedComponentBO
    */
  private def getComponentBOByStatusRequireNextStep(selectedComponentBO: SelectedComponentBO,
                                                    currentStep: Option[StepCurrentConfigBO]): SelectedComponentBO = {
    val component = selectedComponentBO.copy(status = Some(StatusComponent(
      Some(RequireNextStep()),
      Some(AddedComponent()),
      Some(NotExcludedComponent()),
      Some(Success()),
      Some(DefaultComponent())
    )))

    CurrentConfig.addComponent(currentStep.get, component.component.get)

    component
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param possibleComponentToSelect : List[String],
    *        selectedComponentBO: SelectedComponentBO,
    *        currentStep: Option[StepCurrentConfigBO]
    * @return
    */
  private def getComponentBOByStatusAllowNextComponent(possibleComponentToSelect: List[String],
                                                       selectedComponentBO: SelectedComponentBO,
                                                       currentStep: Option[StepCurrentConfigBO]): SelectedComponentBO = {
    possibleComponentToSelect match {
      case List() =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireNextStep()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(currentStep.get, component.component.get)

        component
      case _ =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(AllowNextComponent()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(currentStep.get, component.component.get)

        component
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param currentConfigWithTempSelectedComponent : List[SelectedComponentBO],
    *                                               fatherStep: StepBO, selectedComponentBO: SelectedComponentBO,
    * @return List[String]
    */
  private def getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent: List[ComponentBO],
                                                  fatherStep: StepBO, selectedComponentBO: SelectedComponentBO,
                                                  previousSelectedComponentsInCurrentConfig: List[ComponentBO]): List[String] = {

    val outExcludedDependencyIds: Set[String] = (currentConfigWithTempSelectedComponent flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    val unselectedComponentsIds: List[String] = fatherStep.componentIds.get filterNot (c => {
      currentConfigWithTempSelectedComponent.map(_.componentId.get).contains(c)
    })

    val unselectedExcludedComponents: List[String] = unselectedComponentsIds.filter(uC => {
      outExcludedDependencyIds.contains(uC)
    })

    val filteredComponents: List[String] =
      fatherStep.componentIds.get filterNot (c => {
        unselectedExcludedComponents :+ selectedComponentBO.component.get.componentId.get
      }.contains(c))

    filteredComponents.filterNot(pCTS => previousSelectedComponentsInCurrentConfig.map(_.componentId.get).contains(pCTS))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param countOfComponents : Int, fatherStep: StepBO
    * @return StatusSelectionCriterium
    */
  private def getSelectionCriteriumStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterium = {

    val min: Int = fatherStep.selectionCriteriumMin.get
    val max: Int = fatherStep.selectionCriteriumMax.get

    countOfComponents match {
      case count if min > count && max > count => RequireComponent()
      case count if min <= count && max == count => RequireNextStep()
      case count if min <= count && max > count => AllowNextComponent()
      case count if max < count => NotAllowNextComponent()
      case _ => ErrorSelectionCriterium()
    }
  }
}