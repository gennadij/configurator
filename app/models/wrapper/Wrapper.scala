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
    NextStepIn(
        jsonNextStepIn.params.componentIds
    )
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
            JsonStep(
                nextStepOut.step.get.stepId,
                nextStepOut.step.get.nameToShow,
                nextStepOut.step.get.components.map(component => {
                  JsonComponent(
                      component.componentId,
                      component.nameToShow
                  )
                })
            )
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
  def toCurrentConfigIn(jsonCurrentConfiugIn: JsonCurrentConfigIn): CurrentConfigIn = {
    CurrentConfigIn(
        jsonCurrentConfiugIn.params.clientId
    )
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
  def toJsonCurentConfigOut(currentConfigOut: CurrentConfigOut): JsonCurrentConfigOut = {
    JsonCurrentConfigOut(
        result = JsonCurrentConfigResult(
            currentConfigOut.steps.map(step => {
                JsonStep(
                    step.stepId,
                    step.nameToShow,
                    step.components.map(component => {
                        JsonComponent(
                            component.componentId,
                            component.nameToShow
                        )
                    })
                 )
             })
        )
    )
  }
}