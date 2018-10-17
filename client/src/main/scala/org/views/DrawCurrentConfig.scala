package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.currentConfig.json.JsonStepCurrentConfig
import org.util.HtmlElementIds
import org.views.html.CurrentConfig

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawCurrentConfig {

  def drawCurrentConfigWindow: JQuery = {

    val jQueryCurrentConfigWindow = CurrentConfig.getCurrentConfigWindow()

    jQueryCurrentConfigWindow.appendTo(jQuery(HtmlElementIds.section))
  }

  def updateCurrentConfig(jsonStepCurrentConfig: JsonStepCurrentConfig)= {

    if (jQuery(HtmlElementIds.currentConfgJQuery).length != 0) {
      jQuery(HtmlElementIds.currentConfgJQuery).remove()
    }
    drawCurrentConfigWindow

    CurrentConfig.drawCurrentConfig(jsonStepCurrentConfig)
  }





}
