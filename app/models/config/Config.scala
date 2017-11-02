package models.config

import models.wrapper.Wrapper
import models.json.startConfig.JsonStartConfigOut
import models.json.startConfig.JsonStartConfigIn
import models.persistence.Persistence
import models.json.nextStep.JsonNextStepIn
import models.json.nextStep.JsonNextStepOut
import models.json.currentConfig.JsonCurrentConfigIn
import models.json.currentConfig.JsonCurrentConfigOut
import models.currentConfig.CurrentConfig

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 26.10.2017
 */
class Config extends Wrapper{
  
  def startConfig(jsonStartConfigIn: JsonStartConfigIn): JsonStartConfigOut = {
    toJsonStartConfigOut(Persistence.startConfig(toStartConfigIn(jsonStartConfigIn)))
  }
  
  def nextStep(jsonNextStepIn: JsonNextStepIn): JsonNextStepOut = {
    toJsonNextStepOut(Persistence.nestStep(toNextStepIn(jsonNextStepIn)))
  }
  
  def currentConfig(jsonCurrentConfigIn: JsonCurrentConfigIn): JsonCurrentConfigOut = {
    toJsonCurentConfigOut(CurrentConfig.getCurrentConfig(toCurrentConfigIn(jsonCurrentConfigIn)))
  }
  
}