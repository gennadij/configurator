package org.controllers.websocket

import org.controllers.{NextStep, SelectedComponent}
import org.shared.json.JsonNames
import org.shared.json.currentConfig.JsonCurrentConfigOut
import org.shared.json.nextStep.JsonNextStepOut
import org.shared.json.selectedComponent.JsonComponentOut
import org.views.DrawCurrentConfig
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 25.04.2018
 */
class MessageHandler {


  def handleMessage(receivedMessage: JsValue) = (receivedMessage \ "json").asOpt[String] match {
//    case Some(JsonNames.START_CONFIG) => startConfig(receivedMessage)
    case Some(JsonNames.COMPONENT) => selectedComponent(receivedMessage)
    case Some(JsonNames.CURRENT_CONFIG) => currentConfig(receivedMessage)
    case Some(JsonNames.NEXT_STEP) => nextStep(receivedMessage)
    case _ => Json.obj("error" -> "keinen Treffer")
  }

//  private def startConfig(receivedMessage: JsValue): Unit = {
//    val jsonStartConfigOut: JsResult[JsonStartConfigOut] = Json.fromJson[JsonStartConfigOut](receivedMessage)
//    jsonStartConfigOut match {
//      case jSCOut: JsSuccess[JsonStartConfigOut] => new StartConfig(jSCOut.value).startConfig
//      case e: JsError => println("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
//    }
//  }

  private def selectedComponent(receivedMessage: JsValue): Unit = {
    val jsonComponentOut: JsResult[JsonComponentOut] = Json.fromJson[JsonComponentOut](receivedMessage)
    jsonComponentOut match {
      case jCOut: JsSuccess[JsonComponentOut] =>
        new SelectedComponent(jCOut.value).selectedComponent
        new NextStep().requirenNextStep(jCOut.value)
      case e: JsError => println("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
    }
  }
  private def currentConfig(receivedMessage: JsValue): Unit = {
    val currentConfigOut: JsResult[JsonCurrentConfigOut] = Json.fromJson[JsonCurrentConfigOut](receivedMessage)
    currentConfigOut match {
      case jCCOut: JsSuccess[JsonCurrentConfigOut] =>
        new DrawCurrentConfig().updateCurrentConfig(jCCOut.value.result.step.get)
      case e: JsError => println("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
    }
  }
  private def nextStep(receivedMessage: JsValue): Unit = {
    val nextStepOut: JsResult[JsonNextStepOut] = Json.fromJson[JsonNextStepOut](receivedMessage)
    nextStepOut match {
      case jNSOut: JsSuccess[JsonNextStepOut] =>
        new NextStep().nextStep(jNSOut.value)
      case e: JsError => println("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
    }
  }
}