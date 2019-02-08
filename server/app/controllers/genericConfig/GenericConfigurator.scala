package controllers.genericConfig

import controllers.wrapper.Wrapper
import models.bo.currentConfig.CurrentConfigContainerBO
import models.configLogic.{NextStep, SelectedComponent, StartConfig}
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.json.selectedComponent.{JsonSelectedComponentIn, JsonSelectedComponentOut}
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
  def startConfig(jsonStepIn: JsonStepIn, currentConfigContainerBO: CurrentConfigContainerBO): JsonStepOut = {
    toJsonStepOut(StartConfig.startConfig(toStepIn(jsonStepIn), currentConfigContainerBO))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @return JsonNextStepOut
    */
  def getNextStep(currentConfigContainerBO: CurrentConfigContainerBO): JsonStepOut = toJsonStepOut(NextStep.getNextStep(currentConfigContainerBO))

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonCurrentConfigIn : JsonCurrentConfigIn
    * @return JsonCurrentConfigOut
    */
  def currentConfig(jsonCurrentConfigIn: JsonCurrentConfigIn, currentConfigContainerBO: CurrentConfigContainerBO): JsonCurrentConfigOut = {
    toJsonCurrentConfigOut(currentConfigContainerBO.currentConfig)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param jsonComponentIn : JsonComponentIn
    * @return JsonComponentOut
    */
  def selectedComponent(jsonComponentIn: JsonSelectedComponentIn, currentConfigContainerBO: CurrentConfigContainerBO): JsonSelectedComponentOut = {
    toJsonComponentOut(SelectedComponent.verifySelectedComponent(toSelectedComponentBO(jsonComponentIn), currentConfigContainerBO))
  }
}