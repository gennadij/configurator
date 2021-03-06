package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.json.selectedComponent.JsonSelectedComponentResult
import org.shared.json.step.JsonComponent
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

  def markSelectedComponent(selectedComponentId: String,
                            jsonSelectedComponentResult: JsonSelectedComponentResult): JQuery = {

    jsonSelectedComponentResult.warning match {
      case Some(w) =>

        val excludedComponentInternalCode: Option[String] = w.excludedComponentInternal match {
          case Some(eCI) => Some(eCI.code)
          case None => None
        }

        (excludedComponentInternalCode, jsonSelectedComponentResult.addedComponent) match {
          case (None, true) =>
            jQuery(s"#$selectedComponentId").css("background-color", "#9FF781")
          case (None, false)      =>
            jQuery(s"#$selectedComponentId").css("background-color", "#F5A9D0")
          case (Some(_), _)  =>
            jQuery(s"#$selectedComponentId").css("background-color", "#B40431")
        }

      case None =>
        jQuery(s"#$selectedComponentId").css("background-color", "#9FF781")
    }
  }
}
