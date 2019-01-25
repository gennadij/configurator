package org.controllers.action

import org.controllers.websocket.WebSocket
import org.scalajs.jquery.JQuery
import org.shared.json.selectedComponent.{JsonSelectedComponentIn, JsonSelectedComponentParam}
import org.views.HtmlElementText
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
class Component {

  def addMouseClickForComponent(jQueryElem: JQuery): Unit = {
    jQueryElem.on("click", () => componentSelected(jQueryElem.attr(HtmlElementText.id).toString))
  }

  def componentSelected(cId: String): Unit = {
    val jsonComponent: String = Json.toJson(JsonSelectedComponentIn(
      params = JsonSelectedComponentParam(
        componentId = cId
      )
    )).toString()

    println("OUT -> " + jsonComponent)

    WebSocket.socket.send(jsonComponent)
  }
}
