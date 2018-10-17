package org.models

import org.scalajs.dom.raw.WebSocket
import org.shared.component.json.JsonComponentOut
import org.shared.currentConfig.json.JsonCurrentConfigIn
import org.views.DrawSelectedComponent
import org.views.html.Status
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 28.09.2018
  */
class SelectedComponent(jsonComponentOut: JsonComponentOut, webSocket: WebSocket) {

  def selectedComponent = {

    val drawSelectedComponent: DrawSelectedComponent = new DrawSelectedComponent(jsonComponentOut.result.status)

    drawSelectedComponent.drawStatus

    Status.getSelectedComponentStatusWindow(jsonComponentOut.result.status)

    drawSelectedComponent.markSelectedComponent(jsonComponentOut.result.selectedComponentId)

    //CurrentConfig aufrufen

    val jsonCurrentConfig: String = Json.toJson(JsonCurrentConfigIn()).toString()

    println("OUT -> " + jsonCurrentConfig)

    webSocket.send(jsonCurrentConfig)



  }

}
