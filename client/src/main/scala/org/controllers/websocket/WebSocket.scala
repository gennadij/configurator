package org.controllers.websocket

import org.scalajs.dom
import org.shared.json.JsonNames
import org.shared.json.step.{JsonStepIn, JsonStepParams}
import play.api.libs.json.{JsValue, Json}

import scala.util.matching.Regex

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 
  */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
object WebSocket {
  val url = "ws://localhost:9000/configurator"
	val socket = new dom.WebSocket(url)
  val numPattern: Regex = "[0-9]+".r

  def main(args: Array[String]): Unit = {

    println("main")
    socket.onmessage = {
      (e: dom.MessageEvent) => {
        println("IN -> " + e.data.toString)
        val jsValue: JsValue = Json.parse(e.data.toString)
        new MessageHandler().handleMessage(jsValue)
      }
    }

    socket.onopen = { (_: dom.Event) => {
      println("Websocket open")

      val startConfig = Json.toJson(JsonStepIn(
        json = JsonNames.STEP,
        params = Some(JsonStepParams(
          configUrl = Some("http://config/client_013")
        ))
      )).toString()
      println("OUT -> " + startConfig)
      socket.send(startConfig)
      }
    }

    socket.onerror = {
      (_: dom.Event) => {
        println("""Websocket error""")
      }
    }
  }
}
