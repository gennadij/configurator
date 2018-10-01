package org.models

import org.scalajs.dom.raw.WebSocket
import org.scalajs.jquery.jQuery
import org.shared.component.json.{JsonComponentIn, JsonComponentParam}
import org.shared.startConfig.json.JsonStartConfigOut
import org.views.DrawStartConfig
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 27.09.2018
  */
class StartConfg(jsonStartConfigOut: JsonStartConfigOut, webSocket: WebSocket) {

  def startConfig = {
    println("===startConfig===")

    val drawStartConfig: DrawStartConfig =
      new DrawStartConfig(jsonStartConfigOut.result.step, jsonStartConfigOut.result.status)

    drawStartConfig.drawStartConfig()

    drawStartConfig.drawStatus

    jsonStartConfigOut.result.step.components foreach{
      comp => {
        jQuery("#" + comp.componentId).on("click", () => componentSelected(comp.componentId))
      }
    }
  }

  private def componentSelected(cId: String) = {
    val jsonComponent: String = Json.toJson(JsonComponentIn(
      params = JsonComponentParam(
        componentId = cId
      )
    )).toString()

    println("OUT -> " + jsonComponent)

    webSocket.send(jsonComponent)
  }
}
