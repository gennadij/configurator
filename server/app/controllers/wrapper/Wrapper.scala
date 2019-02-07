package controllers.wrapper

import models.bo._
import models.bo.component.SelectedComponentContainerBO
import models.bo.dependency.DependencyBO
import models.bo.info.InfoBO
import models.bo.step.{StepContainerBO, StepCurrentConfigBO}
import models.bo.warning.WarningBO
import org.shared.error.Error
import org.shared.json.{common, currentConfig}
import org.shared.json.common.{JsonError, JsonInfo, JsonWarning}
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut, JsonCurrentConfigResult, JsonStepCurrentConfig}
import org.shared.json.selectedComponent._
import org.shared.json.step._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2017
  */
trait Wrapper extends RIDConverter {

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonStepIn : JsonStepIn
    * @return StepContainerBO
    */
  def toStepIn(jsonStepIn: JsonStepIn): StepContainerBO = {
    StepContainerBO(
      configUrl = Some(jsonStepIn.params.get.configUrl.get)
    )

  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param stepContainerBO : StepContainerBO
    * @return JsonStepOut
    */
  def toJsonStepOut(stepContainerBO: StepContainerBO): JsonStepOut = {

    val convertedIdsStepContainerBO: StepContainerBO = convertRidToHashForStepContainer(stepContainerBO)

    convertedIdsStepContainerBO.step match {
      case Some(_) =>
        JsonStepOut(
          result = JsonStepResult(
            step = Some(JsonStep(
              stepId = convertedIdsStepContainerBO.step.get.stepId.get,
              nameToShow = convertedIdsStepContainerBO.step.get.nameToShow.get,
              selectionCriterion = JsonSelectionCriterion(
                min = convertedIdsStepContainerBO.step.get.selectionCriterionMin.get.toString.toInt,
                max = convertedIdsStepContainerBO.step.get.selectionCriterionMax.get.toString.toInt
              )
            )),
            componentsForSelection = Some(convertedIdsStepContainerBO.componentsForSelection.get.toList map (component => {
              JsonComponent(
                component.componentId.get,
                component.nameToShow.get
              )
            }))
          )
        )
      case None => JsonStepOut(
        result = JsonStepResult(
          errors = Some(
            stepContainerBO.error.get map { e =>
              JsonError(
                message = e.message,
                name = e.name,
                code = e.code
              )
            }
          )
        )
      )
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonCurrentConfigIn : JsonCurrentConfigIn
    * @return CurrentConfigIn
    */
  def toCurrentConfigIn(jsonCurrentConfigIn: JsonCurrentConfigIn): Unit = {}

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param step : Option[StepCurrentConfigBO]
    * @return JsonCurrentConfigOut
    */
  def toJsonCurrentConfigOut(step: Option[StepCurrentConfigBO]): JsonCurrentConfigOut = {
    JsonCurrentConfigOut(
      result = JsonCurrentConfigResult(
        step = getJsonCurrentStepRecursive(step)
      )
    )
  }


  def getJsonCurrentStepRecursive(firstStep: Option[StepCurrentConfigBO]): Option[JsonStepCurrentConfig] = {
    firstStep match {
      case Some(nextStep) =>
        Some(JsonStepCurrentConfig(
          convertRidToHashForStepOrComponentInCurrentConfig(nextStep.stepId),
          nextStep.nameToShow,
          nextStep.components map (c => {
            currentConfig.JsonComponent(
              convertRidToHashForStepOrComponentInCurrentConfig(c.componentId.get),
              c.nameToShow.get
            )
          }),
          getJsonCurrentStepRecursive(nextStep.nextStep)
        ))
      case None => None
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonComponentIn : JsonComponentIn
    * @return ComponentIn
    */

  def toSelectedComponentBO(jsonComponentIn: JsonSelectedComponentIn): SelectedComponentContainerBO =
    convertHashIdToRidForSelectedComponentBO(jsonComponentIn)

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param selectedComponentBO : ComponentOut
    * @return ComponentIn
    */
  def toJsonComponentOut(selectedComponentBO: SelectedComponentContainerBO): JsonSelectedComponentOut = {

    val selectedComponentWithHash: SelectedComponentContainerBO = convertRidToHashForSelectedComponentBO(selectedComponentBO)

    JsonSelectedComponentOut(
      result = JsonSelectedComponentResult(
        selectedComponentId = selectedComponentWithHash.selectedComponent.get.componentId.get,
        stepId = selectedComponentWithHash.currentStep.get.step.get.stepId.get,
        lastComponent = selectedComponentWithHash.selectedComponent.get.lastComponent.get,
        addedComponent = selectedComponentWithHash.selectedComponent.get.addedComponent.get,
        excludeDependenciesOut = toJsonDependency(selectedComponentWithHash.selectedComponent.get.excludeDependenciesOut),
        excludeDependenciesIn = toJsonDependency(selectedComponentWithHash.selectedComponent.get.excludeDependenciesIn),
        requireDependenciesOut = toJsonDependency(selectedComponentWithHash.selectedComponent.get.requireDependenciesOut),
        requireDependenciesIn = toJsonDependency(selectedComponentWithHash.selectedComponent.get.requireDependenciesIn),
        info = Some(getInfo(selectedComponentWithHash.info)),
        errors = getError(selectedComponentWithHash.errors),
        warning = getWarning(selectedComponentWithHash.warning)
      )
    )
  }

  def getAdddeComponent(addedComponent: Option[Boolean]): Option[Boolean] = {
    ???
  }

  def getInfo(infoBO: Option[InfoBO]): JsonSelectedComponentInfo = {
    infoBO.getOrElse(InfoBO()).selectionCriterion match {
      case Some(info) => JsonSelectedComponentInfo(
        selectionCriterion = Some(JsonInfo(
          message = info.message,
          name = info.name,
          code = info.code
        ))
      )
      case None => JsonSelectedComponentInfo(
        selectionCriterion = None
      )
    }
  }



  def getWarning(warningBO: Option[WarningBO]): Option[JsonSelectedComponentWarning] = {
    val eCI: Option[JsonWarning] = warningBO.getOrElse(WarningBO()).excludedComponentInternal match {
      case Some(w) => Some(JsonWarning(
        message = w.message,
        name = w.name,
        code = w.code
      ))
      case None => None
    }
    val eCE: Option[JsonWarning] =  warningBO.getOrElse(WarningBO()).excludedComponentExternal match {
      case Some(w) => Some(JsonWarning(
        message = w.message,
        name = w.name,
        code = w.code
      ))
      case None => None
    }
    (eCE, eCI) match {
      case (Some(_), _) =>
        Some(JsonSelectedComponentWarning(
          excludedComponentExternal = eCE,
          excludedComponentInternal = eCI
        ))
      case (_, Some(_)) =>
        Some(JsonSelectedComponentWarning(
          excludedComponentExternal = eCE,
          excludedComponentInternal = eCI
        ))
      case (None, None) =>
        None
    }

  }

  def getError(errors: Option[List[Error]]) : Option[List[JsonError]] = {
    errors match {
      case Some(err) => Some(err map { e =>
        JsonError(
          message = e.message,
          name = e.name,
          code = e.code
        )
      })
      case None => None
    }

  }

  def toJsonDependency(dependencyBOs: Option[List[DependencyBO]]): Option[List[JsonDependency]] = {
    dependencyBOs match {
      case Some(dependencyBO) => Some(
        dependencyBO map { dBO =>
          JsonDependency(
            outId = dBO.outId,
            inId = dBO.inId,
            dependencyType = dBO.dependencyType,
            visualization = dBO.visualization,
            nameToShow = dBO.nameToShow,
            strategyOfDependencyResolver = dBO.strategyOfDependencyResolver.value
          )
        })
      case None => None
    }
  }
}