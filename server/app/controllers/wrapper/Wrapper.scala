package controllers.wrapper

import models.bo.{StartConfigBO, StepCurrentConfigBO}
import models.wrapper.component.ComponentOut
import models.wrapper.nextStep.{NextStepIn, NextStepOut}
import org.shared.common.json._
import org.shared.component.json.{JsonComponentOut, JsonComponentResult, JsonComponentStatus}
import org.shared.component.status.StatusComponent
import org.shared.currentConfig.json.{JsonCurrentConfigIn, JsonCurrentConfigOut, JsonCurrentConfigResult, JsonStepCurrentConfig}
import org.shared.nextStep.json.{JsonNextStepIn, JsonNextStepOut, JsonNextStepResult}
import org.shared.startConfig.json.{JsonStartConfigIn, JsonStartConfigOut, JsonStartConfigResult}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2017
  */
trait Wrapper {

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

    startConfigBO.step.get.stepId match {
      case Some(stepId) =>
        JsonStartConfigOut(
          result = JsonStartConfigResult(
            JsonStep(
              startConfigBO.step.get.stepId.get,
              startConfigBO.step.get.nameToShow.get,
              startConfigBO.componentsForSelection.get.components map (component => {
                JsonComponent(
                  component.componentId.get,
                  component.nameToShow.get
                )
              })
            ),
            JsonStepStatus(
              firstStep = Some(JsonStatus(
                startConfigBO.step.get.status.firstStep.get.status,
                startConfigBO.step.get.status.firstStep.get.message)),
              common = Some(JsonStatus(
                startConfigBO.step.get.status.common.get.status,
                startConfigBO.step.get.status.common.get.message))
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
                startConfigBO.step.get.status.firstStep.get.status,
                startConfigBO.step.get.status.firstStep.get.message)),
              common = Some(JsonStatus(
                startConfigBO.step.get.status.common.get.status,
                startConfigBO.step.get.status.common.get.message))
            )

          )
        )
    }


  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonNextStepIn : JsonNextStepIn
    * @return NextStepIn
    */
  def toNextStepIn(jsonNextStepIn: JsonNextStepIn): NextStepIn = {
    NextStepIn()
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param nextStepOut : NextStepOut
    * @return JsonNextStepOut
    */
  def toJsonNextStepOut(nextStepOut: NextStepOut): JsonNextStepOut = {
    JsonNextStepOut(
      result = JsonNextStepResult(
        JsonStep(
          nextStepOut.step.stepId.get,
          nextStepOut.step.nameToShow.get,
//          nextStepOut.components.head.components map (component => {
//            JsonComponent(
//              component.componentId.get,
//              component.nameToShow.get
//            )
//          })
//        )
          List()),
        JsonStepStatus(
          nextStep = Some(JsonStatus(
            nextStepOut.step.status.nextStep.get.status,
            nextStepOut.step.status.nextStep.get.message
          )),
          common = Some(JsonStatus(
            nextStepOut.step.status.common.get.status,
            nextStepOut.step.status.common.get.message
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
    * @param componentOut : ComponentOut
    * @return ComponentIn
    */
  def toJsonComponentOut(componentOut: ComponentOut): JsonComponentOut = {

    val status: StatusComponent = componentOut.status
    JsonComponentOut(
      result = JsonComponentResult(
        componentOut.selectedComponentId,
        componentOut.stepId,
        JsonComponentStatus(
          status.selectionCriterium match {
            case Some(status) =>
              Some(JsonStatus(
                status.status,
                status.message
              ))
            case None => None
          },
          status.selectedComponent match {
            case Some(status) =>
              Some(JsonStatus(
                status.status,
                status.message
              ))
            case None => None
          },
          status.excludeDependency match {
            case Some(status) =>
              Some(JsonStatus(
                status.status,
                status.message
              ))
            case None => None
          },
          status.common match {
            case Some(status) =>
              Some(JsonStatus(
                status.status,
                status.message
              ))
            case None => None
          },
          status.componentType match {
            case Some(status) =>
              Some(JsonStatus(
                status.status,
                status.message
              ))
            case None => None
          }
        ),
        componentOut.dependencies map (d => {
          JsonDependency(
            d.outId,
            d.inId,
            d.dependencyType,
            d.visualization,
            d.nameToShow,
            "",
            ""
          )
        })
      )
    )
  }
}