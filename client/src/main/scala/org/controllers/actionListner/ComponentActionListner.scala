package org.controllers.actionListner

import org.scalajs.dom.raw.WebSocket
import org.scalajs.jquery.JQuery
import org.shared.component.json.{JsonComponentIn, JsonComponentParam}
import org.views.HtmlElementText
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
class ComponentActionListner(webSocket: WebSocket) {

  def addMouseClickForComponent(jQueryElem: JQuery): Unit = {
    jQueryElem.on("click", () => componentSelected(jQueryElem.attr(HtmlElementText.id).toString))
  }

  def componentSelected(cId: String): Unit = {
    val jsonComponent: String = Json.toJson(JsonComponentIn(
      params = JsonComponentParam(
        componentId = cId
      )
    )).toString()

    println("OUT -> " + jsonComponent)

    webSocket.send(jsonComponent)
  }
}
