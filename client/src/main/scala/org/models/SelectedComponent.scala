package org.models

import org.scalajs.dom.raw.WebSocket
import org.shared.component.json.JsonComponentOut
import org.views.DrawSelectedComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 28.09.2018
  */
class SelectedComponent(jsonComponentOut: JsonComponentOut, webSocket: WebSocket) {

  def selectedComponent = {
    println("selectedComponent")

    val drawSelectedComponent: DrawSelectedComponent = new DrawSelectedComponent(jsonComponentOut.result.status)

    drawSelectedComponent.drawStatus

  }

}
