package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.nextStep.json.JsonNextStepOut
import org.views.html.{ComponentWindow, StatusWindow, StepWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class DrawNextStep {
  def drawNextStep(jsonNextStepOut: JsonNextStepOut) = {

    val jQueryConfigMainWindow = jQuery(HtmlElementText.configMainJQuery)

    val jQueryStepWindow = StepWindow.drawStepWindow(jsonNextStepOut.result.step)

    jQueryConfigMainWindow.append(jQueryStepWindow)

    StatusWindow.drawNextStepStatusWindow(jsonNextStepOut.result.status)

    val jQueryComponentWindows: List[JQuery] =
      ComponentWindow.drawComponentWindows(jsonNextStepOut.result.step.components)

    jQueryComponentWindows foreach(jQCW => {
      jQueryStepWindow.append(jQCW)
    })

    jQueryComponentWindows
  }

  def deleteNextStepButton = jQuery(HtmlElementText.buttonJQuery).remove()
}
