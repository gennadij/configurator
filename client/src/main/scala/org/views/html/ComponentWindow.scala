package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonComponent
import org.shared.component.json.JsonComponentStatus
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object ComponentWindow {

  def drawComponentWindows(components: List[JsonComponent]): List[JQuery] = {
    components map (c => {

      val jQueryDivComponent = jQuery(HtmlElementText.componentDiv)

      jQueryDivComponent.attr(HtmlElementText.id, c.componentId)
      jQueryDivComponent.attr(HtmlElementText.clazz, HtmlElementText.classComponent)

      jQueryDivComponent.append(HtmlElementText.componentText(c))

      jQueryDivComponent

    })
  }

  def markSelectedComponent(selectedComponentId: String, jsonComponentStatus: JsonComponentStatus): JQuery = {
    jsonComponentStatus.selectedComponent.get.status match {
      case "ADDED_COMPONENT"        =>
        jQuery(s"#$selectedComponentId").css("background-color", "#9FF781")
      case "REMOVED_COMPONENT"      =>
        jQuery(s"#$selectedComponentId").css("background-color", "#F5A9D0")
      case "NOT_ALLOWED_COMPONENT"  =>
        jQuery(s"#$selectedComponentId").css("background-color", "#B40431")
    }
  }
}
