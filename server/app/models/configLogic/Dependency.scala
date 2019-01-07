package models.configLogic

import models.bo.SelectedComponentBO
import models.persistence.Persistence
import org.shared.status.selectedComponent.{ExcludedComponentExternal, ExcludedComponentInternal, NotExcludedComponentInternal, StatusComponent}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 03.01.2019
  */
trait Dependency {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO: SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyExcludeDependencyInForExternal(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
    selectedComponentBO.selectedComponent.get.excludeDependenciesIn.get match {
      case List() => selectedComponentBO
      case dependencyIn =>

        val excludeComponentsId: List[String] =
          dependencyIn map (_.outId)

        val internComponents: List[String] = selectedComponentBO.currentStep.get.componentIds.get

        val excludeComponentsIdExternal: List[String] =
          excludeComponentsId map (eCsId => internComponents contains  eCsId) zip excludeComponentsId filterNot (_._1) map (_._2)

        if (excludeComponentsIdExternal.nonEmpty) {

          val excludeComponents: List[SelectedComponentBO] = {
            excludeComponentsIdExternal map Persistence.getSelectedComponent
          }

          val nameToShowExcludeComponents: List[String] = excludeComponents map (_.selectedComponent.get.nameToShow.get)

          val statusExcludedComponent: StatusComponent =
            selectedComponentBO.status.get.copy(excludedDependencyExternal =
              Some(ExcludedComponentExternal(
                nameToShowExcludeComponents,
                List()))
            )

          selectedComponentBO.copy(status = Some(statusExcludedComponent))

          //TODO Die Komponente muss aus der CurentConfig entfernt werden
        } else {
          selectedComponentBO
        }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyExcludeDependencyInForInternal(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {
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
}
