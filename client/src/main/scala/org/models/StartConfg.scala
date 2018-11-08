package org.models

import org.controllers.actionListner.ComponentActionListner
import org.scalajs.dom.raw.WebSocket
import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.startConfig.json.JsonStartConfigOut
import org.views.HtmlElementText
import org.views.html.{ComponentWindow, ConfigMainWindow, Status, StepWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 27.09.2018
  */
class StartConfg(jsonStartConfigOut: JsonStartConfigOut, webSocket: WebSocket) {

  def startConfig = {

    val jQueryConfigMainWindow = ConfigMainWindow.drawConfigMainInSection

    jQuery(HtmlElementText.section).append(jQueryConfigMainWindow)

    val jQueryStepWindow = StepWindow.drawStepWindow(jsonStartConfigOut.result.step)

    jQueryConfigMainWindow.append(jQueryStepWindow)

    val jQueryComponentWindows: List[JQuery] =
      ComponentWindow.drawComponentWindows(jsonStartConfigOut.result.step.components)

    Status.getStepStatusWindow(jsonStartConfigOut.result.status)

    jQueryComponentWindows foreach(jQCW => {
      jQueryStepWindow.append(jQCW)

      new ComponentActionListner(webSocket).addMouseClickForComponent(jQCW)
    })
  }
}
