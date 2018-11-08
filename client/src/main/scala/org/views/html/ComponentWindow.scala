package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonComponent
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
}
