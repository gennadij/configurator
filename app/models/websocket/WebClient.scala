package models.websocket

import play.api.libs.json.JsValue

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

class WebClient {
  val admin = new Admin()
  
  def handleMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage, admin)
  }
}