package org.controllers

import org.controllers.action.CurrentConfig
import org.scalajs.jquery.jQuery
import org.shared.json.selectedComponent.JsonComponentOut
import org.views.{DrawNextStep, DrawSelectedComponent, HtmlElementText}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class SelectedComponent(jsonComponentOut: JsonComponentOut) {

  def selectedComponent = {

    jsonComponentOut.result.status.componentType.get.status match {
      case "FINAL_COMPONENT" =>

        new DrawSelectedComponent().drawSelectedComponent(jsonComponentOut)
        new CurrentConfig().getCurrentConfig
      case "DEFAULT_COMPONENT" =>
        new DrawSelectedComponent().drawSelectedComponent(jsonComponentOut)

        //CurrentConfig aufrufen

        new CurrentConfig().getCurrentConfig
    }


  }

}
