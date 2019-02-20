package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.{CurrentConfigContainerBO, CurrentConfigStepBO}
import models.bo.step.{ComponentForSelectionBO, StepContainerBO}
import models.bo.types.{Auto, SelectableDecision}
import models.bo.warning.WarningBO
import models.persistence.Persistence
import org.shared.warning
import org.shared.warning.{ExcludeComponentExternal, ExcludeComponentInternal, ExcludedComponentExternal, ExcludedComponentInternal}

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

          allSelectedComponents.map(allCId => excludeComponentsIdExternal.contains(allCId)).filter (_ == true) match {
            case Nil =>
              // Unter ausgewälten Komponenten exestiert keine ausschliessende Komponente
              val warningBO: WarningBO  = WarningBO(
                excludedComponentExternal = Some(ExcludedComponentExternal())
              )

              selectedComponentContainerBO

              selectedComponentContainerBO.copy(warning = Some(warningBO))
            case _ =>
              //SelectedComponent is excluded from external

              val warningBO: WarningBO  = WarningBO(
                excludedComponentExternal = Some(ExcludedComponentExternal())
              )

              val selectedComponent: SelectedComponentBO = selectedComponentContainerBO.selectedComponent.get.copy(addedComponent = Some(false))

              selectedComponentContainerBO.copy(selectedComponent = Some(selectedComponent), warning = Some(warningBO))
          }
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

/**
  * @author Gennadi Heimann
  * @version 0.0.2
  * @param selectedComponentContainerBO : SelectedComponentBO
  * @return SelectedComponentBO
  */
  private[configLogic] def verifyExcludeDependencyOutForExternal(
                                                                  selectedComponentContainerBO: SelectedComponentContainerBO,
                                                                  currentConfigContainerBO: CurrentConfigContainerBO
                                                                ): SelectedComponentContainerBO = {
    selectedComponentContainerBO.selectedComponent.get.excludeDependenciesOut match {
      case Some(List()) => selectedComponentContainerBO
      case Some(excludeDependenciesOut) =>

        val excludeComponentsId: List[(String, Boolean)] = excludeDependenciesOut map { eDOut =>
          eDOut.strategyOfDependencyResolver match {
            case Auto =>

              val componentForSelectionBO: List[ComponentForSelectionBO] =
                selectedComponentContainerBO.currentStep.get.componentsForSelection.get

              if(componentForSelectionBO.exists(_.componentId.get == eDOut.inId))
                // Exclude internal Component
                (eDOut.inId, true)
              else {
                val stepBO: StepContainerBO = Persistence.getCurrentStep(eDOut.inId)

                val currentConfigStepBO: Option[CurrentConfigStepBO] =
                  getCurrentConfigStep(currentConfigContainerBO, stepBO.step.get.stepId.get)

                currentConfigStepBO match {
                  //Step exestiert in der CurrentConfig
                  case Some(currentConfigStepBO) =>
                    val componentToRemove: SelectedComponentBO = Persistence.getSelectedComponent(eDOut.inId).selectedComponent.get
                    removeComponentExternal(currentConfigStepBO, componentToRemove)
                    (eDOut.inId, false)
                  //Step exestiert nicht in der CurrentConfig => Futur Configuration
                  case None => (eDOut.inId, false)
                }
              }
            case SelectableDecision => ("", false)
          }
        }

        val excludeComponentsIdInternal: List[String] = (excludeComponentsId.filter(_._2)).map(_._1)
        val excludeComponentsIdExternal: List[String] = (excludeComponentsId.filterNot(_._2)).map(_._1)

        //TODO Specs für die Komponente mit external und internal exclude Komponenten
        //TODO Refactoring

        (excludeComponentsIdInternal, excludeComponentsIdExternal) match {
          case (List(), List()) => selectedComponentContainerBO
          case (internal, List()) =>
            val warningBO: WarningBO = selectedComponentContainerBO.warning match {
              case Some(_) =>
                selectedComponentContainerBO.warning.get.copy(
                  excludeComponentInternal = Some(ExcludeComponentInternal(internal)))
              case None =>
                WarningBO(
                  excludeComponentInternal = Some(warning.ExcludeComponentInternal(internal))
                )
            }
            selectedComponentContainerBO.copy(warning = Some(warningBO))
          case (List(), external) =>
            val warningBO: WarningBO = selectedComponentContainerBO.warning match {
              case Some(_) =>
                selectedComponentContainerBO.warning.get.copy(
                  excludeComponentExternal = Some(ExcludeComponentExternal(external)))
              case None =>
                WarningBO(
                  excludeComponentExternal = Some(ExcludeComponentExternal(external))
                )
            }
            selectedComponentContainerBO.copy(warning = Some(warningBO))
          case (internal, external) =>
            val warningBO: WarningBO = selectedComponentContainerBO.warning match {
              case Some(_) =>
                selectedComponentContainerBO.warning.get.copy(
                  excludeComponentInternal = Some(ExcludeComponentInternal(internal)),
                  excludeComponentExternal = Some(ExcludeComponentExternal(external))
                )
              case None =>
                WarningBO(
                  excludeComponentInternal = Some(ExcludeComponentInternal(internal)),
                  excludeComponentExternal = Some(ExcludeComponentExternal(external))
                )
            }
            selectedComponentContainerBO.copy(warning = Some(warningBO))

        }


//        val warningBO: WarningBO = selectedComponentContainerBO.warning match {
//          case Some(_) =>
//            selectedComponentContainerBO.warning.get.copy(
//              excludeComponentExternal = Some(ExcludeComponentExternal(excludeComponentsId)))
//          case None =>
//            WarningBO(
//              excludeComponentExternal = Some(ExcludeComponentExternal(excludeComponentsId))
//            )
//        }
//
//        selectedComponentContainerBO.copy(warning = Some(warningBO))
    }
  }

}
