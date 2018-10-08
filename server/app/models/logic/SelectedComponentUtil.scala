package models.logic

import models.bo.{ComponentBO, SelectedComponentBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import org.shared.common.status.Success
import org.shared.component.status._
import play.api.Logger

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.02.2018
  */

object SelectedComponentUtil {

  def checkStatusSelectedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    new SelectedComponentUtil().checkStatusSelectedComponent(selectedComponentBO)
  }
}

class SelectedComponentUtil {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO: SelectedComponentBO
    * @return ContainerComponentBO
    */
  private def checkStatusSelectedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val selectedComponentIdsFromCurrentConfig: List[String] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components map (_.componentId.get)
      case None => List()
    }

    val excludeComponentsIds: List[String] =
      selectedComponentIdsFromCurrentConfig flatMap {
        sCId => (selectedComponentBO.selectedComponent.get.excludeDependenciesIn.get map (_.outId)).filter { inECId => sCId == inECId }
      }

    excludeComponentsIds match {
      case List() =>
        //STATUS NOT_EXCLUDED_COMPONENT
        caseNotExcludedComponent(selectedComponentBO)
      case _ =>
        //STATUS EXCLUDED_COMPONENT
        caseExcludedComponent(selectedComponentBO)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO : SelectedComponentBO, currentStep: Option[StepCurrentConfigBO], fatherStep: StepBO
    * @return SelectedComponentBO
    */
  private def caseExcludedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {


    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get

    val possibleComponentToSelect: List[String] = getPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
      selectedComponentBO, previousSelectedComponentsInCurrentConfig)

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
          Some(getSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, selectedComponentBO.currentStep.get)),
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
    * @param selectedComponentBO: SelectedComponentBO
    * @return SelectedComponentBO
    */
  private def caseNotExcludedComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val componentIdExist: Boolean =
      selectedComponentBO.stepCurrentConfig.get.components.exists(_.componentId.get == selectedComponentBO.selectedComponent.get.componentId.get)

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get

    val possibleComponentToSelect: List[String] = getPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
      selectedComponentBO, previousSelectedComponentsInCurrentConfig)

    val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size

    if (componentIdExist) {
      CurrentConfig.removeComponent(selectedComponentBO)

      val countOfComponentsInCurrentConfig: Int = selectedComponentBO.stepCurrentConfig match {
        case Some(step) => step.components.size
        case None => 0
      }

      selectedComponentBO.copy(status = Some(StatusComponent(
        Some(getSelectionCriteriumStatus(countOfComponentsInCurrentConfig, selectedComponentBO.currentStep.get)),
        Some(RemovedComponent()),
        Some(NotExcludedComponent()),
        Some(Success()),
        Some(DefaultComponent())
      )))
    } else {
      //STATUS ADDED_COMPONENT
      getSelectionCriteriumStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent, selectedComponentBO.currentStep.get) match {
        case RequireComponent() => getComponentBOByStatusRequireComponent(selectedComponentBO)
        case RequireNextStep() => getComponentBOByStatusRequireNextStep(selectedComponentBO, selectedComponentBO.stepCurrentConfig)
        case AllowNextComponent() => getComponentBOByStatusAllowNextComponent(selectedComponentBO)
        case NotAllowNextComponent() =>
          selectedComponentBO.copy(status = Some(StatusComponent(
            Some(RequireNextStep()),
            Some(NotAllowedComponent()),
            Some(NotExcludedComponent()),
            Some(Success()),
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
  def getComponentBOByStatusRequireComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.possibleComponentIdsToSelect.get match {
      case List() =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireNextStep()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

        component
      case _ =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireComponent()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

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
  def getComponentBOByStatusRequireNextStep(selectedComponentBO: SelectedComponentBO,
                                                    currentStep: Option[StepCurrentConfigBO]): SelectedComponentBO = {
    val component = selectedComponentBO.copy(status = Some(StatusComponent(
      Some(RequireNextStep()),
      Some(AddedComponent()),
      Some(NotExcludedComponent()),
      Some(Success()),
      Some(DefaultComponent())
    )))

    CurrentConfig.addComponent(currentStep.get, component.selectedComponent.get)

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
  def getComponentBOByStatusAllowNextComponent(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.possibleComponentIdsToSelect.get match {
      case List() =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(RequireNextStep()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

        component
      case _ =>
        val component = selectedComponentBO.copy(status = Some(StatusComponent(
          Some(AllowNextComponent()),
          Some(AddedComponent()),
          Some(NotExcludedComponent()),
          Some(Success()),
          Some(DefaultComponent())
        )))

        CurrentConfig.addComponent(selectedComponentBO.stepCurrentConfig.get, component.selectedComponent.get)

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
  def getPossibleComponentToSelect(currentConfigWithTempSelectedComponent: List[ComponentBO],
                                   selectedComponentBO: SelectedComponentBO,
                                   previousSelectedComponentsInCurrentConfig: List[ComponentBO]): List[String] = {
    //TODO nach dem Test Logger.info löschen
    val excludedComponentIds: Set[String] = (currentConfigWithTempSelectedComponent flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet
//    Logger.info("excludedComponentIds: " + excludedComponentIds)
    val unselectedComponentsIds: List[String] = selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
      currentConfigWithTempSelectedComponent.map(_.componentId.get).contains(c)
    })
//    Logger.info("unselectedComponentsIds: " + unselectedComponentsIds)
    val unselectedExcludedComponentIds: List[String] = unselectedComponentsIds.filter(uC => {
      excludedComponentIds.contains(uC)
    })
//    Logger.info("unselectedExcludedComponents: " + unselectedExcludedComponentIds)
//    val filteredComponents: List[String] =
//      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
//        unselectedExcludedComponents :+ selectedComponentBO.component.get.componentId.get
//      }.contains(c))

    val currentConfigWithTempSelectedComponentIds: List[String] =
      currentConfigWithTempSelectedComponent map (_.componentId.get)

    val furtherPosibleComponentIdsToSelect: List[String] =
      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
        unselectedExcludedComponentIds ::: currentConfigWithTempSelectedComponentIds
      }.contains(c))


    Logger.info("filteredComponents: " +furtherPosibleComponentIdsToSelect)
    furtherPosibleComponentIdsToSelect
    // TODO betrachten ob notwendig der Par löschen
//    val debugval = filteredComponents.filterNot(pCTS => previousSelectedComponentsInCurrentConfig.map(_.componentId.get).contains(pCTS))
//    Logger.info("debugval: " + debugval)
//    debugval
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO: SelectedComponentBO
    * @return List[String]
    */
  def getPossibleComponentToSelect_Test(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get


    val excludedComponentIds: Set[String] = (currentConfigWithTempSelectedComponent flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    //    Logger.info("excludedComponentIds: " + excludedComponentIds)


    val unselectedComponentsIds: List[String] = selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
      currentConfigWithTempSelectedComponent.map(_.componentId.get).contains(c)
    })
    //    Logger.info("unselectedComponentsIds: " + unselectedComponentsIds)
    val unselectedExcludedComponentIds: List[String] = unselectedComponentsIds.filter(uC => {
      excludedComponentIds.contains(uC)
    })

//        Logger.info("unselectedExcludedComponents: " + unselectedExcludedComponentIds)
    val pComponentIdsToSelect: List[String] =
      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
        unselectedExcludedComponentIds :+ selectedComponentBO.selectedComponent.get.componentId.get
      }.contains(c))

//    val currentConfigWithTempSelectedComponentIds: List[String] =
//      currentConfigWithTempSelectedComponent map (_.componentId.get)

//    val pComponentIdsToSelect: List[String] =
//      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
//        unselectedExcludedComponentIds ::: currentConfigWithTempSelectedComponentIds
//      }.contains(c))

//    Logger.info("filteredComponents: " +pComponentIdsToSelect)
    // possible selected component filter from previous selected components
    val pSelectedComponentIdsToSelectWithoutComponentsFromCurrentConfig =
      pComponentIdsToSelect.filterNot(pCTS => previousSelectedComponentsInCurrentConfig.map(_.componentId.get).contains(pCTS))
//    Logger.info("debugval: " + debugval)
//    debugval

    selectedComponentBO.copy(possibleComponentIdsToSelect = Some(pSelectedComponentIdsToSelectWithoutComponentsFromCurrentConfig))



  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO: SelectedComponentBO
    * @return List[String]
    */
  def getPossibleComponentToSelect_Test_Test(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val selectedComponentId = selectedComponentBO.selectedComponent.get.componentId.get

    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = selectedComponentBO.stepCurrentConfig match {
      case Some(step) => step.components
      case None => List()
    }

//    val currentConfigWithTempSelectedComponent: List[ComponentBO] =
//      previousSelectedComponentsInCurrentConfig :+ selectedComponentBO.selectedComponent.get

    val previousSelectedComponentsInCurrentConfigIds: List[String] = previousSelectedComponentsInCurrentConfig map (_.componentId.get)

    val fromCurrentConfigExcludedComponentIds: Set[String] = (previousSelectedComponentsInCurrentConfig flatMap (pSC => {
      pSC.excludeDependenciesOut.get map (_.inId)
    })).toSet

    val fromSelectedComponentExcludedComponentIds: List[String] =
      selectedComponentBO.selectedComponent.get.excludeDependenciesOut.get map (_.inId)

//    val excludedComponentIds: Set[String] = (currentConfigWithTempSelectedComponent flatMap (pSC => {
//      pSC.excludeDependenciesOut.get map (_.inId)
//    })).toSet

    //    Logger.info("excludedComponentIds: " + excludedComponentIds)


    val unselectedComponentIds: List[String] = selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
      (previousSelectedComponentsInCurrentConfigIds :::
        fromCurrentConfigExcludedComponentIds.toList.+:(selectedComponentId)).contains(c)
    })

//    val unselectedComponentsIds: List[String] = selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
//      currentConfigWithTempSelectedComponent.map(_.componentId.get).contains(c)
//    })
    //    Logger.info("unselectedComponentsIds: " + unselectedComponentsIds)
    val unselectedExcludedComponentsFromCurrentConfigComponentsIds: List[String] = unselectedComponentIds.filter(uC => {
      fromCurrentConfigExcludedComponentIds.contains(uC)
    })

    val unselectedExcludedComponentsFromSelectedComponent: List[String] = selectedComponentBO.status.get.excludeDependency.get match {
      case ExcludedComponent() => List()
      case NotExcludedComponent() => unselectedComponentIds.filter(uC => {fromSelectedComponentExcludedComponentIds.contains(uC)})
    }


    val unselectedExcludedComponents: List[String] =
      unselectedExcludedComponentsFromCurrentConfigComponentsIds ::: unselectedExcludedComponentsFromSelectedComponent


    val possibleComponentToSelect: List[String] =
      unselectedComponentIds filterNot(uSIds => {unselectedExcludedComponents.contains(uSIds)})


    //        Logger.info("unselectedExcludedComponents: " + unselectedExcludedComponentIds)
//    val pComponentIdsToSelect: List[String] =
//      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
//        unselectedExcludedComponentIds :+ selectedComponentBO.selectedComponent.get.componentId.get
//      }.contains(c))

    //    val currentConfigWithTempSelectedComponentIds: List[String] =
    //      currentConfigWithTempSelectedComponent map (_.componentId.get)

    //    val pComponentIdsToSelect: List[String] =
    //      selectedComponentBO.currentStep.get.componentIds.get filterNot (c => {
    //        unselectedExcludedComponentIds ::: currentConfigWithTempSelectedComponentIds
    //      }.contains(c))

    //    Logger.info("filteredComponents: " +pComponentIdsToSelect)
    // possible selected component filter from previous selected components
//    val pSelectedComponentIdsToSelectWithoutComponentsFromCurrentConfig =
//    pComponentIdsToSelect.filterNot(pCTS => previousSelectedComponentsInCurrentConfig.map(_.componentId.get).contains(pCTS))
    //    Logger.info("debugval: " + debugval)
    //    debugval

    selectedComponentBO.copy(possibleComponentIdsToSelect = Some(possibleComponentToSelect))



  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param countOfComponents : Int, fatherStep: StepBO
    * @return StatusSelectionCriterium
    */
  def getSelectionCriteriumStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterium = {

    val min: Int = fatherStep.selectionCriteriumMin.get
    val max: Int = fatherStep.selectionCriteriumMax.get

    countOfComponents match {
      case count if min > count && max > count => RequireComponent()
      case count if min <= count && max == count => RequireNextStep()
      case count if min <= count && max > count => AllowNextComponent()
      case count if max < count => NotAllowNextComponent()
    }
  }
}