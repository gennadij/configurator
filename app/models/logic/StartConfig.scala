package models.logic

import models.wrapper.startConfig.StartConfigOut
import models.wrapper.startConfig.StartConfigIn

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 19.02.2018
 */

object StartConfig{
  def startConfig(startConfigIn: StartConfigIn): StartConfigOut = {
    new StartConfig(startConfigIn.configUrl).getFirstStep
  }
}

class StartConfig(configUrl: String) {
  
  private def getFirstStep: StartConfigOut = {
    ???
  }
}