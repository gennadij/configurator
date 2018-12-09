package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.json.startConfig.JsonStartConfigOut
import org.views.html.{ComponentWindow, ConfigMainWindow, StatusWindow, StepWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class DrawStartConfig(jsonStartConfigOut: JsonStartConfigOut) {

  def drawStartConfig : List[JQuery] = {

    val jQueryConfigMainWindow = ConfigMainWindow.drawConfigMainInSection

    jQuery(HtmlElementText.section).append(jQueryConfigMainWindow)

    val jQueryStepWindow = StepWindow.drawStepWindow(jsonStartConfigOut.result.step)

    jQueryConfigMainWindow.append(jQueryStepWindow)

    StatusWindow.drawStepStatusWindow(jsonStartConfigOut.result.status)

    val jQueryComponentWindows: List[JQuery] =
      ComponentWindow.drawComponentWindows(jsonStartConfigOut.result.step.components)

    jQueryComponentWindows foreach(jQCW => {
      jQueryStepWindow.append(jQCW)
    })

    jQueryComponentWindows
  }
}
