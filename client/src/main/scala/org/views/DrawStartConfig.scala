package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.{JsonStep, JsonStepStatus}
import org.views.html.{Component, Config, StepWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 28.09.2018
  */
class DrawStartConfig(jsonStep: JsonStep, jsonStepStatus: JsonStepStatus) {

  def drawStartConfig(): JQuery = {

    val jQueryConfigWindow: JQuery = Config.getConfigWindow()

    val jQueryStepWindow = StepWindow.drawStepWindowInConfigMain(jsonStep: JsonStep, null)

    val jQueryComponentWindows: List[JQuery] =  Component.getComponentWindows(jsonStep.components)

    jQueryComponentWindows foreach  (jQueryComponentWindow => {
      jQueryStepWindow.append(jQueryComponentWindow)
    })

    jQueryConfigWindow.append(jQueryStepWindow)

    jQuery(HtmlElementText.section).append(jQueryConfigWindow)
  }
}
