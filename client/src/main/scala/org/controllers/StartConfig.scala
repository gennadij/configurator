package org.controllers

import org.controllers.action.Component
import org.shared.startConfig.json.JsonStartConfigOut
import org.views.DrawStartConfig

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class StartConfig(jsonStartConfigOut: JsonStartConfigOut) {

  private[controllers] def startConfig = {

    val jQueryComponentWindows = new DrawStartConfig(jsonStartConfigOut).drawStartConfig

    jQueryComponentWindows foreach(jQCW => {
      new Component().addMouseClickForComponent(jQCW)
    })
  }
}
