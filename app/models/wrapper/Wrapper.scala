package models.wrapper

import models.wrapper.startConfig.StartConfigIn
import models.json.startConfig.JsonStartConfigIn
import models.json.startConfig.JsonStartConfigOut
import models.wrapper.startConfig.StartConfigOut
import models.json.startConfig.JsonStartConfigResult
import models.json.common.JsonStep
import models.json.common.JsonComponent
import models.json.common.JsonComponent
import models.json.common.JsonComponent
import models.json.nextStep.JsonNextStepIn
import models.wrapper.nextStep.NextStepIn
import models.wrapper.nextStep.NextStepIn
import models.wrapper.nextStep.NextStepOut
import models.json.nextStep.JsonNextStepOut
import models.json.nextStep.JsonNextStepResult
import models.json.currentConfig.JsonCurrentConfigIn
import models.wrapper.currentConfig.CurrentConfigIn
import models.wrapper.currentConfig.CurrentConfigOut
import models.json.currentConfig.JsonCurrentConfigOut
import models.json.currentConfig.JsonCurrentConfigResult
import play.api.Logger
import models.json.component.JsonComponentIn
import models.wrapper.component.ComponentOut
import models.json.component.JsonComponentOut
import models.json.component.JsonComponentResult
import models.json.common.JsonDependency
import models.bo.StepCurrentConfigBO
import models.json.currentConfig.JsonStepCurrentConfig
import models.json.component.JsonComponentStatus
import models.json.common.JsonStatus
import models.status.component.StatusComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.10.2017
 */
trait Wrapper {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonStartConfigIn
   * 
   * @return StartConfigIn
   */
  def toStartConfigIn(jsonStartConfigIn: JsonStartConfigIn): StartConfigIn = {
    StartConfigIn(
        jsonStartConfigIn.params.configUrl
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonStartConfigOut
   * 
   * @return StartConfigOut
   */
  def toJsonStartConfigOut(startConfigOut: StartConfigOut): JsonStartConfigOut = {
    
    JsonStartConfigOut(
        result = JsonStartConfigResult(
            startConfigOut.status,
            startConfigOut.message,
            startConfigOut.step match {
              case Some(step) => {
                Some(JsonStep(
                    step.stepId,
                    step.nameToShow,
                    step.components.map(component => {
                      JsonComponent(
                          component.componentId,
                          component.nameToShow
                      )
                    })
                ))
              }
              case None => {
                None
              }
            }
        )
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonNextStepIn
   * 
   * @return NextStepIn
   */
  def toNextStepIn(jsonNextStepIn: JsonNextStepIn): NextStepIn = {
    NextStepIn()
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param NextStepOut
   * 
   * @return JsonNextStepOut
   */
  def toJsonNextStepOut(nextStepOut: NextStepOut): JsonNextStepOut = {
    JsonNextStepOut(
        result = JsonNextStepResult(
            status = nextStepOut.status,
            message = nextStepOut.message,
            nextStepOut.step match {
              case Some(step) => {
                Some(JsonStep(
                    step.stepId,
                    step.nameToShow,
                    step.components.map(component => {
                      JsonComponent(
                          component.componentId,
                          component.nameToShow
                      )
                    })
                ))
              }
              case None => None
            }
        )
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonCurrentConfigIn
   * 
   * @return CurrentConfigIn
   */
  def toCurrentConfigIn(jsonCurrentConfiugIn: JsonCurrentConfigIn) = {
//    CurrentConfigIn(
//        jsonCurrentConfiugIn.params.clientId
//    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param CurrentConfigOut
   * 
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
      case Some(nextStep) => {
        Some(JsonStepCurrentConfig(
            nextStep.stepId,
            nextStep.nameToShow,
            nextStep.components map (c => {
              JsonComponent(
                  c.componentId,
                  c.nameToShow
              )
            }),
            getJsonCurrentStepRecursiv(nextStep.nextStep)
        ))
      }
      case None => None
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonComponentIn
   * 
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
                  case Some(status) => {
                    Some(JsonStatus(
                        status.status,
                        status.message
                    ))
                  }
                  case None => None
                },
                status.selectedComponent match {
                  case Some(status) => {
                    Some(JsonStatus(
                        status.status,
                        status.message
                    ))
                  }
                  case None => None
                },
                status.excludeDependency match {
                  case Some(status) => {
                    Some(JsonStatus(
                        status.status,
                        status.message
                    ))
                  }
                  case None => None
                },
                status.common match {
                  case Some(status) => {
                    Some(JsonStatus(
                        status.status,
                        status.message
                    ))
                  }
                  case None => None
                },
                status.componentType match {
                  case Some(status) => {
                    Some(JsonStatus(
                        status.status,
                        status.message
                    ))
                  }
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