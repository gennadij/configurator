package controllers.websocket

import controllers.MessageHandler
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */
//old
//object WebClient {
//  def init: WebClient = {
//    new WebClient
//  }
//}
//
//class WebClient extends MessageHandler{
//  val client = new Config()
//
//  def handleMessage(receivedMessage: JsValue): JsValue = {
//    handleMessage(receivedMessage, client)
//  }
//}

//new
object WebClient {
  def init: WebClient = {
    new WebClient
  }
}

class WebClient extends MessageHandler{
//  val client = new Config()

  def handleClientMessage(receivedMessage: JsValue): JsValue = {
    handleMessage(receivedMessage)
  }
}