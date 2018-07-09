package models.config

import models.currentConfig.CurrentConfig
import models.json.component.{JsonComponentIn, JsonComponentOut}
import models.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import models.json.nextStep.JsonNextStepOut
import models.logic.{NextStep, SelectedComponent, StartConfig}
import models.wrapper.Wrapper
import org.shared.startConfig.json.{JsonStartConfigIn, JsonStartConfigOut}

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
   * @param jsonStartConfigIn: JsonStartConfigIn
   * 
   * @return JsonStartConfigOut
   */
  def startConfig(jsonStartConfigIn: JsonStartConfigIn): JsonStartConfigOut = {
    toJsonStartConfigOut(StartConfig.startConfig(toStartConfigIn(jsonStartConfigIn)))
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return JsonNextStepOut
   */
  def nextStep: JsonNextStepOut = {
    toJsonNextStepOut(NextStep.getNextStep())
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param jsonCurrentConfigIn: JsonCurrentConfigIn
   * 
   * @return JsonCurrentConfigOut
   */
  def currentConfig(jsonCurrentConfigIn: JsonCurrentConfigIn): JsonCurrentConfigOut = {
    toJsonCurentConfigOut(CurrentConfig.getCurrentConfig)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param jsonComponentIn: JsonComponentIn
   * 
   * @return JsonComponentOut
   */
  def component(jsonComponentIn: JsonComponentIn): JsonComponentOut = {
    toJsonComponentOut(SelectedComponent.verifySelectedComponent(jsonComponentIn.params.componentId))
  }
}