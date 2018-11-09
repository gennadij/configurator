package org.controllers.action

import org.controllers.websocket.WebSocket
import org.shared.currentConfig.json.JsonCurrentConfigIn
import play.api.libs.json.Json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class CurrentConfig {

  private[controllers] def getCurrentConfig = {

    val jsonCurrentConfig: String = Json.toJson(JsonCurrentConfigIn()).toString()

    println("OUT -> " + jsonCurrentConfig)

    WebSocket.socket.send(jsonCurrentConfig)
  }

}
