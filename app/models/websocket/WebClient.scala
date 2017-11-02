package models.websocket

import play.api.libs.json.JsValue
import models.config.Config
import models.config.ConfigWeb

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */

object WebClient {
  def init = {
    new WebClient
  }
}

class WebClient extends ConfigWeb{
  val client = new Config()
  
  def handleMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage, client)
  }
}