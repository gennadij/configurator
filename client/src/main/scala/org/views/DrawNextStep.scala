package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.json.step.JsonStepOut
import org.views.html.{ComponentWindow, ConfigMainWindow, StatusWindow, StepWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class DrawNextStep {
  def drawNextStep(jsonStepOut: JsonStepOut): List[JQuery] = {
    var jQueryConfigMainWindow: JQuery = jQuery()
    if (jQuery(HtmlElementText.configMainJQuery).length == 0) {
      val jQueryCMWindow = ConfigMainWindow.drawConfigMainInSection
      println(jQuery(HtmlElementText.configMainJQuery).length)
      jQuery(HtmlElementText.section).append(jQueryCMWindow)
      jQueryConfigMainWindow = jQuery(HtmlElementText.configMainJQuery)
    }else{
      jQueryConfigMainWindow = jQuery(HtmlElementText.configMainJQuery)
    }

    val jQueryStepWindow = StepWindow.drawStepWindow(jsonStepOut.result.step.get)

    jQueryConfigMainWindow.append(jQueryStepWindow)

    StatusWindow.drawStepStatusWindow(jsonStepOut.result)

    val jQueryComponentWindows: List[JQuery] =
      ComponentWindow.drawComponentWindows(jsonStepOut.result.componentsForSelection.get)

    jQueryComponentWindows foreach(jQCW => {
      jQueryStepWindow.append(jQCW)
    })

    jQueryComponentWindows
  }

  def deleteNextStepButton = jQuery(HtmlElementText.buttonJQuery).remove()
}
