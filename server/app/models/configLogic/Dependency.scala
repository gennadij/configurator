package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.CurrentConfigContainerBO
import models.bo.warning.WarningBO
import models.persistence.Persistence
import org.shared.warning.{ExcludedComponentExternal, ExcludedComponentInternal}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 03.01.2019
  */
trait Dependency extends CurrentConfig {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentContainerBO: SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[configLogic] def verifyExcludeDependencyInForExternal(
                                                                 selectedComponentContainerBO: SelectedComponentContainerBO,
                                                                 currentConfigContainerBO: CurrentConfigContainerBO
                                                               ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.selectedComponent.get.excludeDependenciesIn.get match {
      case List() => selectedComponentContainerBO
      case dependencyIn =>

        val excludeComponentsId: List[String] =
          dependencyIn map (_.outId)

        val internComponents: List[String] = selectedComponentContainerBO.currentStep.get.componentsForSelection.get map (_.componentId.get)

        //aus excludeComponentes die excludeComponentsExternal entfernen (ausfiltern)
        val excludeComponentsIdExternal: List[String] =
          excludeComponentsId map (eCsId => internComponents contains  eCsId) zip excludeComponentsId filterNot (_._1) map (_._2)

        if (excludeComponentsIdExternal.nonEmpty) {

          //hole ueber componentId excludeComponentsExternal aus DB
          val excludeComponents: List[SelectedComponentContainerBO] = {
            excludeComponentsIdExternal map Persistence.getSelectedComponent
          }

          val allSelectedComponents = getAllComponents(currentConfigContainerBO)

          allSelectedComponents.map(allCId => excludeComponentsIdExternal.contains(allCId)).filter(_ == true) match {
            case Nil =>
              println("excludeComponent ist nicht von dem SelectedComponents")
              selectedComponentContainerBO
            case _ =>
              println("excludeComponent ist aus der SelectedComponents " + excludeComponents.head.selectedComponent.get.nameToShow)
              val warningBO: WarningBO  = WarningBO(
                excludedComponentExternal = Some(ExcludedComponentExternal())
              )

              val selectedComponent: SelectedComponentBO = selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))

              selectedComponentContainerBO.copy(selectedComponent = Some(selectedComponent), warning = Some(warningBO))
          }
          //TODO erkennen wenn selectedComponent von der Komponente die noch nicht ausgewählt war ausgeschlossen
          //TODO Scenario_005_2

//          val nameToShowExcludeComponents: List[String] = excludeComponents map (_.selectedComponent.get.nameToShow.get)

//          val statusExcludedComponent: StatusComponent =
//            selectedComponentContainerBO.status.get.copy(excludedDependencyExternal =
//              Some(ExcludedComponentExternal(
//                nameToShowExcludeComponents,
//                List()))
//            )


        } else {
          //es gab keine excludeDependencies External
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
      case _ =>
        val warningBO: WarningBO = selectedComponentContainerBO.warning match {
          case Some(_) =>
            selectedComponentContainerBO.warning.get.copy(excludedComponentInternal = Some(ExcludedComponentInternal()))
          case None    =>
            WarningBO(
              excludedComponentInternal = Some(ExcludedComponentInternal())
            )
        }
        selectedComponentContainerBO.copy(warning = Some(warningBO))
    }
  }
}
