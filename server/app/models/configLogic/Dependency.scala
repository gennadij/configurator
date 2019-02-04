package models.configLogic

import models.bo.{SelectedComponentContainerBO, WarningBO}
import models.persistence.Persistence
import org.shared.warning.{ExcludedComponentExternal, ExcludedComponentInternal}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 03.01.2019
  */
trait Dependency {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentContainerBO: SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyExcludeDependencyInForExternal(
                                                                 selectedComponentContainerBO: SelectedComponentContainerBO
                                                               ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.selectedComponent.get.excludeDependenciesIn.get match {
      case List() => selectedComponentContainerBO
      case dependencyIn =>

        val excludeComponentsId: List[String] =
          dependencyIn map (_.outId)

        val internComponents: List[String] = selectedComponentContainerBO.currentStep.get.componentsForSelection.get.toList map (_.componentId.get)

        val excludeComponentsIdExternal: List[String] =
          excludeComponentsId map (eCsId => internComponents contains  eCsId) zip excludeComponentsId filterNot (_._1) map (_._2)

        if (excludeComponentsIdExternal.nonEmpty) {

          val excludeComponents: List[SelectedComponentContainerBO] = {
            excludeComponentsIdExternal map Persistence.getSelectedComponent
          }

//          val nameToShowExcludeComponents: List[String] = excludeComponents map (_.selectedComponent.get.nameToShow.get)

//          val statusExcludedComponent: StatusComponent =
//            selectedComponentContainerBO.status.get.copy(excludedDependencyExternal =
//              Some(ExcludedComponentExternal(
//                nameToShowExcludeComponents,
//                List()))
//            )

          val warningBO: WarningBO  = WarningBO(
            excludedComponentExternal = Some(ExcludedComponentExternal())
          )
//            selectedComponentContainerBO.warning.get.copy(excludedComponentExternal = Some(ExcludedComponentExternal()))

          selectedComponentContainerBO.copy(warning = Some(warningBO))
          //TODO Die Komponente muss aus der CurentConfig entfernt werden
        } else {
          selectedComponentContainerBO
        }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentContainerBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyExcludeDependencyInForInternal(
                                                                 selectedComponentContainerBO: SelectedComponentContainerBO
                                                               ): SelectedComponentContainerBO = {
    val previousSelectedComponentIdsFromCurrentConfig: List[String] = selectedComponentContainerBO.stepCurrentConfig match {
      case Some(step) => step.components map (_.componentId.get)
      case None => List()
    }

    val excludedDependencyOutIds = selectedComponentContainerBO.selectedComponent.get.excludeDependenciesIn.get map (_.outId)

    val excludeComponentsIds: List[String] = previousSelectedComponentIdsFromCurrentConfig flatMap {
      sCId => excludedDependencyOutIds filter { inECId => sCId == inECId }
    }

    excludeComponentsIds match {
      case List() =>
        selectedComponentContainerBO
        //TODO Delete comments
//        val statusNotExcludedComponent: StatusComponent
//          selectedComponentContainerBO.status.get.copy(excludedDependencyInternal = Some(NotExcludedComponentInternal()))
//        selectedComponentContainerBO.copy(status = Some(statusNotExcludedComponent))
      case _ =>
        val warningBO: WarningBO = selectedComponentContainerBO.warning match {
          case Some(_) =>
            selectedComponentContainerBO.warning.get.copy(excludedComponentInternal = Some(ExcludedComponentInternal()))
          case None    =>
            WarningBO(
              excludedComponentInternal = Some(ExcludedComponentInternal())
            )
//            selectedComponentContainerBO.copy(warning = warningBO)
        }


        selectedComponentContainerBO.copy(warning = Some(warningBO))
//        val statusExcludedComponent: StatusComponent =
//          selectedComponentContainerBO.status.get.copy(excludedDependencyInternal = Some(ExcludedComponentInternal()))
//        selectedComponentContainerBO.copy(status = Some(statusExcludedComponent))
    }
  }
}
