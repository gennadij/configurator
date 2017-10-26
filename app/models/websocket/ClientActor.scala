package models.websocket

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import play.api.libs.json.JsValue
import play.api.Logger
import play.api.libs.json.Json

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
  
  override def postStop() = webClientsMgr ! Leave
  
  def receive = {
    case msg: JsValue =>
      Logger.debug("ClientAktor " + this.hashCode() +" receive => " + msg)
      webClientsMgr ! ClientSentMessage(webClient.handleMessage(msg))
    
    case ClientSentMessage(msg) => 
      out ! msg
  }
}