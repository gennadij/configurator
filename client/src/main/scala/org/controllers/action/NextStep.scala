package org.controllers.action

import org.controllers.websocket.WebSocket
import org.scalajs.jquery.JQuery
import org.shared.json.step.JsonStepIn
import org.views.DrawNextStep
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
class NextStep {

  def addMouseClick(jQueryElem: JQuery) = {

    jQueryElem.on("click", () => actionNextStep())

  }

  def actionNextStep(): Unit = {

    new DrawNextStep().deleteNextStepButton

    val jsonNextStep: String = Json.toJson(JsonStepIn()).toString()

    println("OUT -> " + jsonNextStep)

    WebSocket.socket.send(jsonNextStep)
  }

}
