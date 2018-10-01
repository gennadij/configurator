package org.views

import org.scalajs.jquery.jQuery
import org.shared.component.json.JsonComponentStatus

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawSelectedComponent(jsonComponentStatus: JsonComponentStatus) {
  def drawStatus = {
    val htmlHeader =
      s"<dev id='status' class='status'>" +
        "componentType = " + jsonComponentStatus.componentType.get.status +
        " , " +
        "selectionCriterium = " + jsonComponentStatus.selectionCriterium.get.status +
        " , " + "</br>" +
        "selectedComponent = " + jsonComponentStatus.selectedComponent.get.status +
        " , " +
        "excludeDependency = " + jsonComponentStatus.excludeDependency.get.status +
        " , " +
        "common = " + jsonComponentStatus.common.get.status +
        "</dev>"

    jQuery("#status").remove()
    jQuery(htmlHeader).appendTo(jQuery("header"))
  }


}
