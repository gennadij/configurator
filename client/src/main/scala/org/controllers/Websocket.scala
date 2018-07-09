package org.controllers

import org.scalajs.dom
import play.api.libs.json._

//import scala.collection.JavaConverters._


object Websocket {
  val url = "ws://localhost:9000/configurator"
	val socket = new dom.WebSocket(url)
  val numPattern = "[0-9]+".r
  
  def main(args: Array[String]): Unit = {
    
    println("main")
    socket.onmessage = {
      (e: dom.MessageEvent) => {
        println("IN -> " + e.data.toString())
        val jsValue: JsValue = Json.parse(e.data.toString())
//        new AdminClienWeb(socket).handleMessage(jsValue)
      }
    }
    
    socket.onopen = { (e: dom.Event) => {
      println("Websocket open")
      val getUser = """{"json" : "test"}"""
//        Json.toJson(
//          JsonUserIn(
//              JsonNames.GET_USER,
//              JsonUserParams(
//                  "user_v016_4_client",
//                  "user_v016_4_client"
//              )
//          )
//      ).toString
      println("OUT -> " + getUser)
      socket.send(getUser)
      }
    }
    
    socket.onerror = {
      (e: dom.Event) => {
        println("Websocket error")
      }
    }
  }
}