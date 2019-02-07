package org.controllers

import org.controllers.action.CurrentConfig
import org.shared.json.selectedComponent.JsonSelectedComponentOut
import org.views.DrawSelectedComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class SelectedComponent(jsonComponentOut: JsonSelectedComponentOut) {

  def selectedComponent = {
    jsonComponentOut.result.lastComponent match {
      case true =>
        new DrawSelectedComponent().drawSelectedComponent(jsonComponentOut)
        new CurrentConfig().getCurrentConfig
      case _ =>
        new DrawSelectedComponent().drawSelectedComponent(jsonComponentOut)

        //CurrentConfig aufrufen

        new CurrentConfig().getCurrentConfig
    }
  }
}
