package models.configLogic

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.currentConfig.{CurrentConfigContainerBO, CurrentConfigStepBO}
import models.bo.step.{ComponentForSelectionBO, StepContainerBO}
import models.bo.types.{Auto, SelectableDecision}
import models.bo.warning.WarningBO
import models.persistence.Persistence
import org.shared.warning._

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

          val allSelectedComponents = getAllComponents(currentConfigContainerBO)

          allSelectedComponents.map(allCId => excludeComponentsIdExternal.contains(allCId)).filter (_ == true) match {
            case Nil =>
              // Unter ausgewälten Komponenten exestiert keine ausschliessende Komponente
              val warningBO: WarningBO  = WarningBO(
                excludedComponentExternal = Some(ExcludedComponentExternal())
              )

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
                  case Some(cCSBO) =>
                    val componentToRemove: SelectedComponentBO = Persistence.getSelectedComponent(eDOut.inId).selectedComponent.get
                    removeComponentExternal(cCSBO, componentToRemove)
                    (eDOut.inId, false)
                  //Step exestiert nicht in der CurrentConfig => Futur Configuration
                  case None => (eDOut.inId, false)
                }
              }
            case SelectableDecision => ("", false)
          }
        }

        val excludeComponentsIdInternal: List[String] = excludeComponentsId.filter(_._2).map(_._1)
        val excludeComponentsIdExternal: List[String] = excludeComponentsId.filterNot(_._2).map(_._1)

        (excludeComponentsIdInternal, excludeComponentsIdExternal) match {
          case (List(), List()) => selectedComponentContainerBO
          case (internal, List()) =>
            selectedComponentContainerBO.copy(warning =
              getWarningBO(warningInternal = Some(ExcludeComponentInternal(internal)),
                selectedComponentContainerBO = selectedComponentContainerBO))
          case (List(), external) =>
            selectedComponentContainerBO.copy(warning =
              getWarningBO(
                warningExternal = Some(ExcludeComponentExternal(external)),
                selectedComponentContainerBO = selectedComponentContainerBO))
          case (internal, external) =>
            selectedComponentContainerBO.copy(warning =
              getWarningBO(
                warningInternal = Some(ExcludeComponentInternal(internal)),
                warningExternal = Some(ExcludeComponentExternal(external)),
                selectedComponentContainerBO = selectedComponentContainerBO
              )
            )

        }
      case None => selectedComponentContainerBO
    }
  }

  def getWarningBO(
                    warningInternal: Option[Warning] = None,
                    warningExternal: Option[Warning] = None,
                    selectedComponentContainerBO: SelectedComponentContainerBO
                  ): Option[WarningBO] = {
    selectedComponentContainerBO.warning match {
      case Some(_) =>
        Some(selectedComponentContainerBO.warning.get.copy(
          excludeComponentInternal = warningInternal,
          excludeComponentExternal = warningExternal))
      case None =>
        Some(WarningBO(
          excludeComponentInternal = warningInternal,
          excludeComponentExternal = warningExternal
        ))
    }
  }
}
