package controllers.genericConfig

import controllers.wrapper.Wrapper
import models.currentConfig.CurrentConfig
import models.logic.{NextStep, SelectedComponent, StartConfig}
import org.shared.component.json.{JsonComponentIn, JsonComponentOut}
import org.shared.currentConfig.json.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.nextStep.json.JsonNextStepOut
import org.shared.startConfig.json.{JsonStartConfigIn, JsonStartConfigOut}

//TODO FirstStepStatus zu StartConfig umbenennen

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
    * @param jsonStartConfigIn : JsonStartConfigIn
    * @return JsonStartConfigOut
    */
  def startConfig(jsonStartConfigIn: JsonStartConfigIn, currentConfig: CurrentConfig): JsonStartConfigOut = {
    toJsonStartConfigOut(StartConfig.startConfig(toStartConfigIn(jsonStartConfigIn), currentConfig))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @return JsonNextStepOut
    */
  def getNextStep(currentConfig: CurrentConfig): JsonNextStepOut = toJsonNextStepOut(NextStep.getNextStep(currentConfig))

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