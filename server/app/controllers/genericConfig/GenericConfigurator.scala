package controllers.genericConfig

import controllers.wrapper.Wrapper
import models.configLogic.{CurrentConfig, NextStep, SelectedComponent, StartConfig}
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.json.selectedComponent.{JsonComponentIn, JsonComponentOut}
import org.shared.json.step.{JsonStepIn, JsonStepOut}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann on 26.10.2017
  *
  * Stellt eine Schnittstelle zwischen Controller und
  * Logik des generischen Konfigurators
  */

trait GenericConfigurator extends Wrapper{

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonStepIn : JsonStartConfigIn
    * @return JsonStartConfigOut
    */
  def startConfig(jsonStepIn: JsonStepIn, currentConfig: CurrentConfig): JsonStepOut = {
    toJsonStepOut(StartConfig.startConfig(toStepIn(jsonStepIn), currentConfig))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @return JsonNextStepOut
    */
  def getNextStep(currentConfig: CurrentConfig): JsonStepOut = toJsonStepOut(NextStep.getNextStep(currentConfig))

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonCurrentConfigIn : JsonCurrentConfigIn
    * @return JsonCurrentConfigOut
    */
  def currentConfig(jsonCurrentConfigIn: JsonCurrentConfigIn, currentConfig: CurrentConfig): JsonCurrentConfigOut = {
    toJsonCurrentConfigOut(currentConfig.getCurrentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonComponentIn : JsonComponentIn
    * @return JsonComponentOut
    */
  def selectedComponent(jsonComponentIn: JsonComponentIn, currentConfig: CurrentConfig): JsonComponentOut = {
    toJsonComponentOut(SelectedComponent.verifySelectedComponent(toSelectedComponentBO(jsonComponentIn), currentConfig))
  }
}