package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonStepStatus
import org.shared.component.json.JsonComponentStatus
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object Status {

  def getStepStatusWindow(jsonStepStatus: JsonStepStatus) :JQuery = {

    jQuery(HtmlElementIds.statusJQuery).remove()

    val htmlDiv = "<div></div>"

    val jQueryDiv = jQuery(htmlDiv)

    jQueryDiv.attr("id", HtmlElementIds.statusHtml)
    jQueryDiv.attr("class", HtmlElementIds.statusHtml)

    val htmlStatus = jsonStepStatus.firstStep.get.status +" , " + jsonStepStatus.common.get.status

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementIds.header).append(jQueryDiv)
  }

  def getSelectedComponentStatusWindow(jsonComponentStatus: JsonComponentStatus): JQuery = {

    jQuery("#status").remove()

    val htmlDiv = "<div></div>"

    val jQueryDiv = jQuery(htmlDiv)

    jQueryDiv.attr("id", HtmlElementIds.statusHtml)
    jQueryDiv.attr("class", HtmlElementIds.statusHtml)

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

    jQuery(HtmlElementIds.header).append(jQueryDiv)
  }

}
