package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonStepStatus
import org.shared.component.json.JsonComponentStatus
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object Status {

  def getStepStatusWindow(jsonStepStatus: JsonStepStatus) :JQuery = {

    jQuery(HtmlElementText.statusJQuery).remove()

    val htmlDiv = "<div></div>"

    val jQueryDiv = jQuery(htmlDiv)

    jQueryDiv.attr("id", HtmlElementText.statusHtml)
    jQueryDiv.attr("class", HtmlElementText.statusHtml)

    val htmlStatus = jsonStepStatus.firstStep.get.status +" , " + jsonStepStatus.common.get.status

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementText.header).append(jQueryDiv)
  }

  def drawSelectedComponentStatusWindow(jsonComponentStatus: JsonComponentStatus): JQuery = {

    jQuery("#status").remove()

    val htmlDiv = "<div></div>"

    val jQueryDiv = jQuery(htmlDiv)

    jQueryDiv.attr("id", HtmlElementText.statusHtml)
    jQueryDiv.attr("class", HtmlElementText.statusHtml)

    val htmlStatus = "componentType = " + jsonComponentStatus.componentType.get.status +
        " , " +
        "selectionCriterium = " + jsonComponentStatus.selectionCriterium.get.status +
        " , " + "</br>" +
        "selectedComponent = " + jsonComponentStatus.selectedComponent.get.status +
        " , " +
        "excludeDependency = " + jsonComponentStatus.excludeDependency.get.status +
        " , " +
        "common = " + jsonComponentStatus.common.get.status

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementText.header).append(jQueryDiv)
  }

}
