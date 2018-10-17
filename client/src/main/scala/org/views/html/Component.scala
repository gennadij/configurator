package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.JsonComponent
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object Component {

  def getComponentWindows(components: List[JsonComponent]): List[JQuery] = {
    components map (c => {
      val htmlDiv = "<div></div>"

      val jQueryDiv = jQuery(htmlDiv)

      jQueryDiv.attr("id", c.componentId)
      jQueryDiv.attr("class", HtmlElementIds.classComponent)

      val htmlIdAndNameToShow = "ID " + c.componentId.subSequence(0, 6) +
        "&emsp; || &emsp;" +
        "nameToShow: " + c.nameToShow

      jQueryDiv.append(htmlIdAndNameToShow)

    })

//    var htmlComponents = ""
//
//    components foreach { component =>
//      val componentId = component.componentId
//
//      this.componentIds += componentId
//
//      htmlComponents = htmlComponents +
//        "<div id='" + componentId + "' class='component'>" +
//        "ID " + component.componentId.subSequence(0, 6) +
//        "&emsp; || &emsp;" +
//        "nameToShow: " + component.nameToShow +
//        "&emsp; || &emsp;" +
//        "</br>" +
//        "</div>"
//    }
  }


}
