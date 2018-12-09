package controllers

import controllers.genericConfig.GenericConfigurator
import models.currentConfig.CurrentConfig
import org.shared.json.JsonNames
import org.shared.json.currentConfig.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.json.error.{JsonErrorIn, JsonErrorParams}
import org.shared.json.nextStep.JsonNextStepIn
import org.shared.json.selectedComponent.JsonComponentIn
import org.shared.json.startConfig.JsonStartConfigIn
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
    (receivedMessage \ "json").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage, cC)
      case Some("NextStep") => nextStep(receivedMessage, cC)
      case Some("CurrentConfig") => currentConfig(receivedMessage, cC)
      case Some("Component") => selectedComponent(receivedMessage, cC)
      case _ => jsonError(errorText = "Input JSON is not permitted")
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage : JsValue
    * @return JsValue
    */
  private def startConfig(receivedMessage: JsValue, currentConfig: CurrentConfig): JsValue = {
    val jsonStartConfigIn: JsResult[JsonStartConfigIn] = Json.fromJson[JsonStartConfigIn](receivedMessage)
    jsonStartConfigIn match {
      case _: JsSuccess[JsonStartConfigIn] => Json.toJson(startConfig(jsonStartConfigIn.get, currentConfig))
      case e: JsError => jsonError(JsonNames.START_CONFIG, e)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receiveMessage : JsValue
    * @return JsValue
    */
  private def nextStep(receiveMessage: JsValue, currentConfig: CurrentConfig): JsValue = {
    val jsonNextStepIn: JsResult[JsonNextStepIn] = Json.fromJson[JsonNextStepIn](receiveMessage)
    jsonNextStepIn match {
      case _: JsSuccess[JsonNextStepIn] => Json.toJson(getNextStep(currentConfig))
      case e: JsError => jsonError(JsonNames.NEXT_STEP, e)
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
    val jsonComponentIn: JsResult[JsonComponentIn] = Json.fromJson[JsonComponentIn](receivedMessage)
    jsonComponentIn match {
      case _: JsSuccess[JsonComponentIn] => Json.toJson(selectedComponent(jsonComponentIn.get, currentConfig))
      case e: JsError => jsonError(JsonNames.COMPONENT, e)
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