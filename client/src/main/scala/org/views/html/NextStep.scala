package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.10.2018
  */
object NextStep {

  def drawNextStepButton(stepId: String): JQuery = {
    val jQueryDivButtonNextSTep: JQuery = jQuery("<div></div>")
    jQueryDivButtonNextSTep.attr(HtmlElementText.id, HtmlElementText.buttonHtml)
    jQueryDivButtonNextSTep.attr(HtmlElementText.clazz, HtmlElementText.buttonHtml)
    jQueryDivButtonNextSTep.append(HtmlElementText.textInButtonHtml)
    val jQueryDivStep = jQuery(HtmlElementText.numberSign + stepId)
    jQueryDivStep.append(jQueryDivButtonNextSTep)
  }
}
