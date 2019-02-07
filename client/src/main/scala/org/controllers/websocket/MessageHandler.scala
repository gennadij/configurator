package org.controllers.websocket

import org.controllers.{NextStep, SelectedComponent}
import org.shared.json.currentConfig.JsonCurrentConfigOut
import org.shared.json.selectedComponent.JsonSelectedComponentOut
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.views.DrawCurrentConfig
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 25.04.2018
 */
class MessageHandler {


  def handleMessage(receivedMessage: JsValue) = (receivedMessage \ JsonKey.json).asOpt[String] match {
    case Some(JsonNames.SELECTED_COMPONENT) => selectedComponent(receivedMessage)
    case Some(JsonNames.CURRENT_CONFIG) => currentConfig(receivedMessage)
    case Some(JsonNames.STEP) => step(receivedMessage)
    case _ => Json.obj("error" -> "keinen Treffer")
  }

  private def selectedComponent(receivedMessage: JsValue): Unit = {
    val jsonComponentOut: JsResult[JsonSelectedComponentOut] = Json.fromJson[JsonSelectedComponentOut](receivedMessage)
    jsonComponentOut match {
      case jCOut: JsSuccess[JsonSelectedComponentOut] =>
        new SelectedComponent(jCOut.value).selectedComponent
        new NextStep().requireNextStep(jCOut.value)
      case e: JsError => println("Errors -> " + JsonNames.SELECTED_COMPONENT + ": " + JsError.toJson(e).toString())
    }
  }
  private def currentConfig(receivedMessage: JsValue): Unit = {
    val currentConfigOut: JsResult[JsonCurrentConfigOut] = Json.fromJson[JsonCurrentConfigOut](receivedMessage)
    currentConfigOut match {
      case jCCOut: JsSuccess[JsonCurrentConfigOut] =>
        new DrawCurrentConfig().updateCurrentConfig(jCCOut.value.result.step.get)
      case e: JsError => println("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
    }
  }
  private def step(receivedMessage: JsValue): Unit = {
    val stepOut: JsResult[JsonStepOut] = Json.fromJson[JsonStepOut](receivedMessage)
    stepOut match {
      case jNSOut: JsSuccess[JsonStepOut] => new NextStep().nextStep(jNSOut.value)
      case e: JsError => println("Errors -> " + JsonNames.STEP + ": " + JsError.toJson(e).toString())
    }
  }
}