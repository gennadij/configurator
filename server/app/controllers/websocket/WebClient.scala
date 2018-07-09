package controllers.websocket

import controllers.MessageHandler
import models.config.Config
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */

object WebClient {
  def init: WebClient = {
    new WebClient
  }
}

class WebClient extends MessageHandler{
  val client = new Config()
  
  def handleMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage, client)
  }
}