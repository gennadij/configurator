package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.{JsonComponent, JsonStep}
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object StepWindow {

  def drawStepWindowInConfigMain(step: JsonStep, jQueryConfigMain: JQuery): JQuery = {

    val jQueryStepWindow = jQuery(HtmlElementText.stepDiv)

    jQueryStepWindow.attr(HtmlElementText.id, step.stepId)
    jQueryStepWindow.attr(HtmlElementText.clazz, HtmlElementText.classStep)

    jQueryStepWindow.append(HtmlElementText.stepText(step))

    jQueryConfigMain.append(jQueryStepWindow)
  }

  def appendComponentsToStepWindow(components: List[JsonComponent]): List[JQuery] = {
    components map (c => {
      val htmlDiv =

      val jQueryDiv = jQuery(htmlDiv)

      jQueryDiv.attr("id", c.componentId)
      jQueryDiv.attr("class", HtmlElementText.classComponent)

      val htmlIdAndNameToShow = "ID " + c.componentId.subSequence(0, 6) +
        "&emsp; || &emsp;" +
        "nameToShow: " + c.nameToShow

      jQueryDiv.append(htmlIdAndNameToShow)

    })
  }
}
