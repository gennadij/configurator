package org.controllers

import org.scalajs.dom
import org.shared.common.JsonNames
import org.shared.startConfig.json.{JsonStartConfigIn, JsonStartConfigParams}
import play.api.libs.json._

import scala.util.matching.Regex

//import scala.collection.JavaConverters._


//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
object Websocket {
  val url = "ws://localhost:9000/configurator"
	val socket = new dom.WebSocket(url)
  val numPattern: Regex = "[0-9]+".r
  
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

      val startConfig = Json.toJson(JsonStartConfigIn(
        json = JsonNames.START_CONFIG,
        params = JsonStartConfigParams(
          configUrl = "http://config/client_013"
        )
      )).toString()
      println("OUT -> " + startConfig)
      socket.send(startConfig)
      }
    }
    
    socket.onerror = {
      (e: dom.Event) => {
        println("Websocket error")
      }
    }
  }
}