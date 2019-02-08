package controllers.websocket

import controllers.MessageHandler
import models.bo.currentConfig.CurrentConfigContainerBO
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */

object WebClient {

  def init: WebClient = {
   new WebClient(
     CurrentConfigContainerBO()
   )
  }
}

class WebClient(
               val currentConfigContainerBO: CurrentConfigContainerBO
               ) extends MessageHandler {

  def handleClientMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage,
      currentConfigContainerBO
    )
  }
}
