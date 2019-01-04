package controllers.wrapper

import models.bo._
import org.shared.json.common
import org.shared.json.common.{JsonComponent, JsonStatus, JsonStep, JsonStepStatus}
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut, JsonCurrentConfigResult, JsonStepCurrentConfig}
import org.shared.json.nextStep.{JsonNextStepOut, JsonNextStepResult}
import org.shared.json.selectedComponent._
import org.shared.json.startConfig.{JsonStartConfigIn, JsonStartConfigOut, JsonStartConfigResult}
import org.shared.status.nextStep.NextStepExist
import org.shared.status.selectedComponent.StatusComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2017
  */
trait Wrapper extends RIDConverter {

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonStartConfigIn : JsonStartConfigIn
    * @return StartConfigBO
    */
  def toStartConfigIn(jsonStartConfigIn: JsonStartConfigIn): StartConfigBO = {
    StartConfigBO(
      Some(jsonStartConfigIn.params.configUrl)
    )
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param startConfigBO : StartConfigBO
    * @return JsonStartConfigOut
    */
  def toJsonStartConfigOut(startConfigBO: StartConfigBO): JsonStartConfigOut = {

    val convertedIdsStartConfigBO: StartConfigBO = convertRidToHashforStartConfig(startConfigBO)

    convertedIdsStartConfigBO.step.get.stepId match {
      case Some(_) =>
        JsonStartConfigOut(
          result = JsonStartConfigResult(
            JsonStep(
              convertedIdsStartConfigBO.step.get.stepId.get,
              convertedIdsStartConfigBO.step.get.nameToShow.get,
              convertedIdsStartConfigBO.componentsForSelection.get.components map (component => {
                JsonComponent(
                  component.componentId.get,
                  component.nameToShow.get
                )
              })
            ),
            JsonStepStatus(
              firstStep = Some(JsonStatus(
                convertedIdsStartConfigBO.step.get.status.get.startConfig.get.status,
                convertedIdsStartConfigBO.step.get.status.get.startConfig.get.message)),
              common = Some(JsonStatus(
                convertedIdsStartConfigBO.step.get.status.get.common.get.status,
                convertedIdsStartConfigBO.step.get.status.get.common.get.message))
            )

          )
        )
      case None =>
        JsonStartConfigOut(
          result = JsonStartConfigResult(
            JsonStep(
              "",
              "",
              List()
            ),
            JsonStepStatus(
              firstStep = Some(JsonStatus(
                startConfigBO.step.get.status.get.startConfig.get.status,
                startConfigBO.step.get.status.get.startConfig.get.message)),
              common = Some(JsonStatus(
                startConfigBO.step.get.status.get.common.get.status,
                startConfigBO.step.get.status.get.common.get.message))
            )

          )
        )
    }


  }

//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param jsonNextStepIn : JsonNextStepIn
//    * @return NextStepIn
//    */
//  def toNextStepIn(jsonNextStepIn: JsonNextStepIn)

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param nextStepBO : NextStepOut
    * @return JsonNextStepOut
    */
  def toJsonNextStepOut(nextStepBO: NextStepBO): JsonNextStepOut = {

    nextStepBO.step.get.status.get.nextStep.get match {
      case NextStepExist() =>

        val nextStepWithConvertedRids: NextStepBO = convertRidToHashForNextStepBO(nextStepBO)

        JsonNextStepOut(
          result = JsonNextStepResult(
            common.JsonStep(
              stepId = nextStepWithConvertedRids.step.get.stepId.get,
              nameToShow = nextStepWithConvertedRids.step.get.nameToShow.get,
              components = nextStepWithConvertedRids.componentsForSelection.get.components map (c => {
                JsonComponent(
                  componentId = c.componentId.get,
                  nameToShow = c.nameToShow.get
                )
              })
            ),
            JsonStepStatus(
              nextStep = Some(JsonStatus(
                nextStepWithConvertedRids.step.get.status.get.nextStep.get.status,
                nextStepWithConvertedRids.step.get.status.get.nextStep.get.message
              )),
              currentConfig = Some(JsonStatus(
                status = nextStepWithConvertedRids.step.get.status.get.currentConfig.get.status,
                message = nextStepWithConvertedRids.step.get.status.get.currentConfig.get.message
              )),
              common = Some(JsonStatus(
                nextStepWithConvertedRids.step.get.status.get.common.get.status,
                nextStepWithConvertedRids.step.get.status.get.common.get.message
              ))
            )
          )
        )
      case _ =>
        JsonNextStepOut(
          result = JsonNextStepResult(
            step = JsonStep("", "", List()),
            JsonStepStatus(
              nextStep = Some(JsonStatus(
                nextStepBO.step.get.status.get.nextStep.get.status,
                nextStepBO.step.get.status.get.nextStep.get.message
              )),
              common = Some(JsonStatus(
                nextStepBO.step.get.status.get.common.get.status,
                nextStepBO.step.get.status.get.common.get.message
              ))
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
            JsonComponent(
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
    * @param jsonComponentIn: JsonComponentIn
    * @return ComponentIn
    */

  def toSelectedComponentBO(jsonComponentIn: JsonComponentIn): SelectedComponentBO =
    convertHashIdToRidForSelectedComponentBO(jsonComponentIn)

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param selectedComponentBO : ComponentOut
    * @return ComponentIn
    */
  def toJsonComponentOut(selectedComponentBO: SelectedComponentBO): JsonComponentOut = {

    val selectedComponentWithHash: SelectedComponentBO = convertRidToHashForSelectedComponentBO(selectedComponentBO)

    val status: StatusComponent = selectedComponentWithHash.status.get
    JsonComponentOut(
      result = JsonComponentResult(
        selectedComponentWithHash.selectedComponent.get.componentId.get,
        selectedComponentWithHash.currentStep.get.stepId.get,
        JsonComponentStatus(
          status.selectionCriterion match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          },
          status.selectedComponent match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          },
          status.excludedDependencyInternal match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          },
          status.excludedDependencyExternal match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          },
          status.common match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          },
          status.componentType match {
            case Some(s) =>
              Some(JsonStatus(
                s.status,
                s.message
              ))
            case None => None
          }
        ),
        excludeDependenciesOut = toJsonDependency(selectedComponentWithHash.selectedComponent.get.excludeDependenciesOut),
        excludeDependenciesIn = toJsonDependency(selectedComponentWithHash.selectedComponent.get.excludeDependenciesIn),
        requireDependenciesOut = toJsonDependency(selectedComponentWithHash.selectedComponent.get.requireDependenciesOut),
        requireDependenciesIn = toJsonDependency(selectedComponentWithHash.selectedComponent.get.requireDependenciesIn)
      )
    )
  }

  def toJsonDependency(dependencyBOs: Option[List[DependencyBO]]): Option[List[JsonDependency]] = {
    dependencyBOs match {
      case Some(dependencyBO) => Some(
        dependencyBO map {dBO =>
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