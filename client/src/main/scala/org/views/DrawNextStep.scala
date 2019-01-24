package org.views

import org.scalajs.jquery.jQuery

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class DrawNextStep {
  def drawNextStep(jsonNextStepOut: Any) = {

    val jQueryConfigMainWindow = jQuery(HtmlElementText.configMainJQuery)

//    val jQueryStepWindow = StepWindow.drawStepWindow(jsonNextStepOut.result.step)
//
//    jQueryConfigMainWindow.append(jQueryStepWindow)
//
//    StatusWindow.drawNextStepStatusWindow(jsonNextStepOut.result.status)
//
//    val jQueryComponentWindows: List[JQuery] =
//      ComponentWindow.drawComponentWindows(jsonNextStepOut.result.step.components)
//
//    jQueryComponentWindows foreach(jQCW => {
//      jQueryStepWindow.append(jQCW)
//    })
//
//    jQueryComponentWindows
    ???
  }

  def deleteNextStepButton = jQuery(HtmlElementText.buttonJQuery).remove()
}
