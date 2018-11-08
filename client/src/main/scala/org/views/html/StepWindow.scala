package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonStep
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object StepWindow {

  def drawStepWindow(step: JsonStep): JQuery = {

    val jQueryStepWindow = jQuery(HtmlElementText.stepDiv)

    jQueryStepWindow.attr(HtmlElementText.id, step.stepId)
    jQueryStepWindow.attr(HtmlElementText.clazz, HtmlElementText.classStep)

    jQueryStepWindow.append(HtmlElementText.stepText(step))
  }
}
