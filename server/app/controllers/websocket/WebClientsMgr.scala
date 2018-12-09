package controllers.websocket

import akka.actor._
import play.api.Logger
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 24.10.2017
 */

case object Join
case object Leave

final case class ClientSentMessage(message: JsValue)

object WebClientsMgr{
  def props(): Props = Props(new WebClientsMgr)
}

class WebClientsMgr extends Actor {
  
  def receive: Receive = process(Set.empty)
  
  def process(subscribers: Set[ActorRef]): Receive = {
    case Join => 
      Logger.debug("WebClient Aktor Join")
      context become process(subscribers + sender)
    case Leave =>
      Logger.debug("WebClient Aktor Leave")
      context become process(subscribers - sender)
      
    case msg: ClientSentMessage =>
      Logger.debug("WebClient send => " + msg.toString)
      sender ! msg
  }
}