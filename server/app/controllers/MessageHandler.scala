package controllers

import controllers.genericConfig.GenericConfigurator
import models.configLogic.CurrentConfig
import org.shared.json.{JsonKey, JsonNames}
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.json.error.{JsonErrorIn, JsonErrorParams}
import org.shared.json.selectedComponent.JsonSelectedComponentIn
import org.shared.json.step.JsonStepIn
import play.api.Logger
import play.api.libs.json._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 23.12.2016
  */
trait MessageHandler extends GenericConfigurator{

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage : JsValue, client: Config
    * @return JsValue
    */
  def handleMessage(receivedMessage: JsValue, cC: CurrentConfig): JsValue = {
    (receivedMessage \ JsonKey.json).asOpt[String] match {
      case Some(JsonNames.STEP) => step(receivedMessage, cC)
      case Some(JsonNames.CURRENT_CONFIG) => currentConfig(receivedMessage, cC)
      case Some(JsonNames.SELECTED_COMPONENT) => selectedComponent(receivedMessage, cC)
      case _ => jsonError(errorText = "Input JSON is not permitted")
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param receivedMessage : JsValue
    * @return JsValue
    */
  private def step(receivedMessage: JsValue, currentConfig: CurrentConfig): JsValue = {
    val jsonStepIn: JsResult[JsonStepIn] = Json.fromJson[JsonStepIn](receivedMessage)
    jsonStepIn match {
      case jSIn: JsSuccess[JsonStepIn] =>
        jSIn.get.params match {
          case Some(_) =>Json.toJson(startConfig(jSIn.get, currentConfig))
          case None => Json.toJson(getNextStep(currentConfig))
        }
      case e: JsError => jsonError(JsonNames.STEP, e)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage: JsValue
    * @return JsValue
    */
  private def currentConfig(receivedMessage: JsValue, cC: CurrentConfig): JsValue = {
    val jsonCurrentConfigIn: JsResult[JsonCurrentConfigIn] = Json.fromJson[JsonCurrentConfigIn](receivedMessage)
    jsonCurrentConfigIn match {
      case s: JsSuccess[JsonCurrentConfigIn] => s
      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
    }
    val jsonCurrentConfigOut: JsonCurrentConfigOut = currentConfig(jsonCurrentConfigIn.get, cC)
    Json.toJson(jsonCurrentConfigOut)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage: JsValue
    * @return JsValue
    */
  def selectedComponent(receivedMessage: JsValue, currentConfig: CurrentConfig): JsValue = {
    val jsonComponentIn: JsResult[JsonSelectedComponentIn] = Json.fromJson[JsonSelectedComponentIn](receivedMessage)
    jsonComponentIn match {
      case _: JsSuccess[JsonSelectedComponentIn] => Json.toJson(selectedComponent(jsonComponentIn.get, currentConfig))
      case e: JsError => jsonError(JsonNames.SELECTED_COMPONENT, e)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param errorText: String, e: JsError
    * @return JsValue
    */
  private def jsonError(errorText: String, e: JsError): JsValue = {
    val error = JsonErrorIn(
      JsonNames.ERROR,
      JsonErrorParams(
        "Errors -> " + errorText + " : " + JsError.toJson(e).toString()
      )
    )
    Logger.error(error.toString)
    Json.toJson(error)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param errorText: String, e: JsError
    * @return JsValue
    */
  private def jsonError(errorText: String): JsValue = {
    Logger.error(errorText)

    Json.toJson(JsonErrorIn(
      JsonNames.ERROR,
      JsonErrorParams(
        message = errorText
      )
    ))
  }
}