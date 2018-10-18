package controllers.websocket

import akka.actor.{Actor, ActorRef, Props}
import play.api.Logger
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.10.2017
 */

object ClientActor {
  def props(out: ActorRef, webClientsMgr: ActorRef) = Props(new ClientActor(out, webClientsMgr))
}

class ClientActor(out: ActorRef, webClientsMgr: ActorRef) extends Actor {
  
  val webClient: WebClient = WebClient.init
  
  webClientsMgr ! Join
  
  override def postStop(): Unit = webClientsMgr ! Leave
  
  def receive: PartialFunction[Any, Unit] = {
    case msg: JsValue =>
      Logger.debug("WebClient " + this.hashCode() +" receive => " + msg)
      webClientsMgr ! ClientSentMessage(webClient.handleClientMessage(msg))
    
    case ClientSentMessage(msg) => 
      out ! msg
  }
}