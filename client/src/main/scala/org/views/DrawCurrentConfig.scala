package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.currentConfig.json.JsonStepCurrentConfig
import org.views.html.CurrentConfig

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawCurrentConfig {

  def drawCurrentConfigWindow: JQuery = {

    val jQueryCurrentConfigWindow = CurrentConfig.getCurrentConfigWindow()

    jQueryCurrentConfigWindow.appendTo(jQuery(HtmlElementText.section))
  }

  def updateCurrentConfig(jsonStepCurrentConfig: JsonStepCurrentConfig)= {

    if (jQuery(HtmlElementText.currentConfigJQuery).length != 0) {
      jQuery(HtmlElementText.currentConfigJQuery).remove()
    }
    drawCurrentConfigWindow

    CurrentConfig.drawCurrentConfig(jsonStepCurrentConfig)
  }





}
