package controllers.websocket

import controllers.MessageHandler
import models.currentConfig.CurrentConfig
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */

object WebClient {
  def init: WebClient = {

    val webClient = new WebClient(new CurrentConfig)

    webClient
  }
}

class WebClient(val currentConfig: CurrentConfig) extends MessageHandler {

  def handleClientMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage, currentConfig)
  }
}
