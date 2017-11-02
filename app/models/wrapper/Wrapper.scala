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

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.10.2017
 */
trait Wrapper {
  
  
  def toStartConfigIn(jsonStartConfigIn: JsonStartConfigIn): StartConfigIn = {
    StartConfigIn(
        jsonStartConfigIn.params.configUrl,
        jsonStartConfigIn.params.clientId
    )
  }
  
  def toJsonStartConfigOut(startConfigOut: StartConfigOut): JsonStartConfigOut = {
    JsonStartConfigOut(
        result = JsonStartConfigResult(
            startConfigOut.status,
            startConfigOut.message,
            JsonStep(
                startConfigOut.step.stepId,
                startConfigOut.step.nameToShow,
                startConfigOut.step.components.map(component => {
                  JsonComponent(
                      component.componentId,
                      component.nameToShow
                  )
                })
            )
        )
    )
  }
  
  def toNextStepIn(jsonNextStepIn: JsonNextStepIn): NextStepIn = {
    NextStepIn(
        jsonNextStepIn.params.componentIds,
        jsonNextStepIn.params.clientId
    )
  }
  
  def toJsonNextStepOut(nextStepOut: NextStepOut): JsonNextStepOut = {
    JsonNextStepOut(
        status = nextStepOut.status,
        result = JsonNextStepResult(
            JsonStep(
                nextStepOut.step.stepId,
                nextStepOut.step.nameToShow,
                nextStepOut.step.components.map(component => {
                  JsonComponent(
                      component.componentId,
                      component.nameToShow
                  )
                })
            )
        )
    )
  }
  
  def toCurrentConfigIn(jsonCurrentConfiugIn: JsonCurrentConfigIn): CurrentConfigIn = {
    CurrentConfigIn(
        jsonCurrentConfiugIn.params.clientId
    )
  }
  
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