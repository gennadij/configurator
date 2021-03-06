package controllers.wrapper

import models.bo.component.{SelectedComponentBO, SelectedComponentContainerBO}
import models.bo.dependency.DependencyBO
import models.bo.step.{ComponentForSelectionBO, StepBO, StepContainerBO}
import org.shared.json.selectedComponent.JsonSelectedComponentIn

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.09.2018
  */
trait RIDConverter extends RidToHash {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param stepContainerBO: StepContainerBO
    * @return StepContainerBO
    */
  private[wrapper] def convertRidToHashForStepContainer(stepContainerBO: StepContainerBO): StepContainerBO = {
    stepContainerBO.error match {
      case Some(_) => stepContainerBO
      case _ =>
        val stepIdHash: String = setIdAndHash(stepContainerBO.step.get.stepId.get)._2

        val stepBOWithHashId: StepBO = stepContainerBO.step.get.copy(stepId = Some(stepIdHash))

        val componentsBOWithHashId: Option[List[ComponentForSelectionBO]] =
          stepContainerBO.error match {
            case Some(_) => None

            case _ => Some(
              stepContainerBO.componentsForSelection.get map (c => {
                c.copy(componentId = Some(setIdAndHash(c.componentId.get)._2))
              })
            )
          }

        stepContainerBO.copy(step = Some(stepBOWithHashId), componentsForSelection = componentsBOWithHashId)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param jsonComponentIn: JsonComponentIn
    * @return SelectedComponentBO
    */
  private[wrapper] def convertHashIdToRidForSelectedComponentBO(jsonComponentIn: JsonSelectedComponentIn): SelectedComponentContainerBO = {

    val componentRid: String = getRId(jsonComponentIn.params.componentId) match {
      case Some(id) => id
      case None => ""
    }

    SelectedComponentContainerBO(
      selectedComponent = Some(SelectedComponentBO(
        componentId = Some(componentRid)
      ))
    )
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[wrapper] def convertRidToHashForSelectedComponentBO(selectedComponentBO: SelectedComponentContainerBO): SelectedComponentContainerBO = {

    val stepIdHash: Option[String] = selectedComponentBO.currentStep.get.step.get.stepId match {
      case Some(sId) => getHash(sId)
      case None => None
    }

    val selectedComponentIdHash: Option[String] = selectedComponentBO.selectedComponent.get.componentId match {
      case Some(cId) => getHash(cId)
      case None => None
    }

    val excludeDependencyInHashId: Option[List[DependencyBO]] = selectedComponentBO.selectedComponent.get.excludeDependenciesIn match {
      case Some(dependencies) => Some(dependencies map {
        d => {d.copy(outId = setIdAndHash(d.outId)._2, inId = setIdAndHash(d.outId)._2)}
      })
      case None => None
    }

    val excludeDependencyOutHashId: Option[List[DependencyBO]] = selectedComponentBO.selectedComponent.get.excludeDependenciesOut match {
      case Some(dependencies) => Some(dependencies map {
        d => {d.copy(outId = setIdAndHash(d.outId)._2, inId = setIdAndHash(d.outId)._2)}
      })
      case None => None
    }

    val requireDependencyOutHashId: Option[List[DependencyBO]] = selectedComponentBO.selectedComponent.get.requireDependenciesOut match {
      case Some(dependencies) => Some(dependencies map {
        d => {d.copy(outId = setIdAndHash(d.outId)._2, inId = setIdAndHash(d.outId)._2)}
      })
      case None => None
    }

    val requireDependencyInHashId: Option[List[DependencyBO]] = selectedComponentBO.selectedComponent.get.requireDependenciesIn match {
      case Some(dependencies) => Some(dependencies map {
        d => {d.copy(outId = setIdAndHash(d.outId)._2, inId = setIdAndHash(d.outId)._2)}
      })
      case None => None
    }

    val currentStepBO: StepBO = selectedComponentBO.currentStep.get.step.get.copy(stepId = stepIdHash)

    selectedComponentBO.copy(
      selectedComponent = Some(selectedComponentBO.selectedComponent.get.copy(
        componentId = selectedComponentIdHash,
        excludeDependenciesOut = excludeDependencyOutHashId,
        excludeDependenciesIn = excludeDependencyInHashId,
        requireDependenciesOut = requireDependencyOutHashId,
        requireDependenciesIn = requireDependencyInHashId)),
      currentStep = Some(selectedComponentBO.currentStep.get.copy(step = Some(currentStepBO))))
  }

  def convertRidToHashForStepOrComponentInCurrentConfig(id: String): String = {
    getHash(id).get
  }
}
