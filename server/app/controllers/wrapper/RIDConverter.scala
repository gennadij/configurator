package controllers.wrapper

import models.bo._
import org.shared.json.selectedComponent.JsonComponentIn
import org.shared.status.common.Success

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.09.2018
  */
trait RIDConverter extends RidToHash {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param stepContainerBO: StartConfigBO
    * @return SelectedComponentBO
    */
  private[wrapper] def convertRidToHashforStartConfig(stepContainerBO: StepContainerBO): StepContainerBO = {
    stepContainerBO.step.get.status.get.common match {
      case Some(Success()) =>
        val stepIdHash: String = setIdAndHash(stepContainerBO.step.get.stepId.get)._2

        val stepBOWithHashId: StepBO = stepContainerBO.step.get.copy(stepId = Some(stepIdHash))

        val componentsBOWithHashId: Option[Set[ComponentBO]] =
          stepContainerBO.error match {
            case Some(List.empty) =>
              Some(
                stepContainerBO.componentsForSelection.get map (c => {
                  c.copy(componentId = Some(setIdAndHash(c.componentId.get)._2))
                })
              )
            case _ => None
          }

        stepContainerBO.copy(step = Some(stepBOWithHashId), componentsForSelection = componentsBOWithHashId)

      case _ => stepContainerBO
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param jsonComponentIn: JsonComponentIn
    * @return SelectedComponentBO
    */
  private[wrapper] def convertHashIdToRidForSelectedComponentBO(jsonComponentIn: JsonComponentIn): SelectedComponentBO = {

    val componentRid: String = getRId(jsonComponentIn.params.componentId) match {
      case Some(id) => id
      case None => ""
    }

    SelectedComponentBO(
      selectedComponent = Some(ComponentBO(
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
  private[wrapper] def convertRidToHashForSelectedComponentBO(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val stepIdHash: Option[String] = selectedComponentBO.currentStep.get.stepId match {
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

    selectedComponentBO.copy(
      selectedComponent = Some(selectedComponentBO.selectedComponent.get.copy(
        componentId = selectedComponentIdHash,
        excludeDependenciesOut = excludeDependencyOutHashId,
        excludeDependenciesIn = excludeDependencyInHashId,
        requireDependenciesOut = requireDependencyOutHashId,
        requireDependenciesIn = requireDependencyInHashId)),
      currentStep = Some(selectedComponentBO.currentStep.get.copy(
        stepId = stepIdHash)))
  }

  def convertRidToHashForNextStepBO(nextStepBO: NextStepBO): NextStepBO = {

    val nextStepId: String = setIdAndHash(nextStepBO.step.get.stepId.get)._2

    val nextStep: StepBO = nextStepBO.step.get.copy(stepId = Some(nextStepId))

    val componentHashIds =  nextStepBO.componentsForSelection.get.components map (cBO => {
      cBO.copy(componentId = Some(setIdAndHash(cBO.componentId.get)._2))
    })

    val componentsForSelectionBO: ComponentsForSelectionBO =
      nextStepBO.componentsForSelection.get.copy(components = componentHashIds)

    nextStepBO.copy(step = Some(nextStep), componentsForSelection = Some(componentsForSelectionBO))

  }

  def convertRidToHashForStepOrComponentInCurrentConfig(id: String): String = {
    getHash(id).get
  }
}
