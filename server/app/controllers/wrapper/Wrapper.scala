package controllers.wrapper

import models.bo._
import org.shared.common.json._
import org.shared.component.json.{JsonComponentIn, JsonComponentOut, JsonComponentResult, JsonComponentStatus}
import org.shared.component.status.StatusComponent
import org.shared.currentConfig.json.{JsonCurrentConfigIn, JsonCurrentConfigOut, JsonCurrentConfigResult, JsonStepCurrentConfig}
import org.shared.nextStep.json.{JsonNextStepOut, JsonNextStepResult}
import org.shared.startConfig.json.{JsonStartConfigIn, JsonStartConfigOut, JsonStartConfigResult}

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
                convertedIdsStartConfigBO.step.get.status.get.firstStep.get.status,
                convertedIdsStartConfigBO.step.get.status.get.firstStep.get.message)),
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
                startConfigBO.step.get.status.get.firstStep.get.status,
                startConfigBO.step.get.status.get.firstStep.get.message)),
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
    JsonNextStepOut(
      result = JsonNextStepResult(
        JsonStep(
          nextStepBO.step.get.stepId.get,
          nextStepBO.step.get.nameToShow.get,
          List()),
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

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonCurrentConfiugIn : JsonCurrentConfigIn
    * @return CurrentConfigIn
    */
  def toCurrentConfigIn(jsonCurrentConfiugIn: JsonCurrentConfigIn): Unit = {}

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param step : Option[StepCurrentConfigBO]
    * @return JsonCurrentConfigOut
    */
  def toJsonCurentConfigOut(step: Option[StepCurrentConfigBO]): JsonCurrentConfigOut = {
    JsonCurrentConfigOut(
      result = JsonCurrentConfigResult(
        step = getJsonCurrentStepRecursiv(step)
      )
    )
  }


  def getJsonCurrentStepRecursiv(firstStep: Option[StepCurrentConfigBO]): Option[JsonStepCurrentConfig] = {
    firstStep match {
      case Some(nextStep) =>
        Some(JsonStepCurrentConfig(
          nextStep.stepId,
          nextStep.nameToShow,
          nextStep.components map (c => {
            JsonComponent(
              c.componentId.get,
              c.nameToShow.get
            )
          }),
          getJsonCurrentStepRecursiv(nextStep.nextStep)
        ))
      case None => None
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param selectedComponentBO : ComponentOut
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
        selectedComponentWithHash.component.get.componentId.get,
        selectedComponentWithHash.currentStep.get.stepId.get,
        JsonComponentStatus(
          status.selectionCriterium match {
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
          status.excludeDependency match {
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
        excludeDependenciesOut = toJsonDependency(selectedComponentWithHash.component.get.excludeDependenciesOut),
        excludeDependenciesIn = toJsonDependency(selectedComponentWithHash.component.get.requireDependenciesIn),
        requireDependenciesOut = toJsonDependency(selectedComponentWithHash.component.get.requireDependenciesOut),
        requireDependenciesIn = toJsonDependency(selectedComponentWithHash.component.get.requireDependenciesIn)
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
            nameToShow = dBO.nameToShow
          )
        })
      case None => None
    }
  }
}