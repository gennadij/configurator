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
import models.json.component.JsonComponentIn
import models.json.component.JsonComponentOut

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 26.10.2017
 */
class Config extends Wrapper{
  
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonStartConfigIn
   * 
   * @return JsonStartConfigOut
   */
  def startConfig(jsonStartConfigIn: JsonStartConfigIn): JsonStartConfigOut = {
    toJsonStartConfigOut(Persistence.startConfig(toStartConfigIn(jsonStartConfigIn)))
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonNextStepIn
   * 
   * @return JsonNextStepOut
   */
  def nextStep(jsonNextStepIn: JsonNextStepIn): JsonNextStepOut = {
    toJsonNextStepOut(Persistence.nestStep(toNextStepIn(jsonNextStepIn)))
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsonCurrentConfigIn
   * 
   * @return JsonCurrentConfigOut
   */
  def currentConfig(jsonCurrentConfigIn: JsonCurrentConfigIn): JsonCurrentConfigOut = {
//    toJsonCurentConfigOut(CurrentConfig.getCurrentConfig(toCurrentConfigIn(jsonCurrentConfigIn)))
    ???
  }
  
  def component(jsonComponentIn: JsonComponentIn): JsonComponentOut = {
    toJsonComponentOut(Persistence.component(toComponentIn(jsonComponentIn)))
  }
  
}