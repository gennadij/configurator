package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonStep
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object Step {

  def getStepWindow(step: JsonStep): JQuery = {
    val htmlDiv = "<div> </div>"

    val jQueryStepWindow = jQuery(htmlDiv)

    jQueryStepWindow.attr("id", step.stepId)
    jQueryStepWindow.attr("class", HtmlElementIds.classStep)

    val htmlIdAndNameToShowtext = "ID: " + step.stepId.subSequence(0, 6) +
      "&emsp; || &emsp;" +
      "nameToShow: " + step.nameToShow

    jQueryStepWindow.append(htmlIdAndNameToShowtext)
  }
}
