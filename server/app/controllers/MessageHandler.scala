package controllers

import controllers.genericConfig.GenericConfigurator
import org.shared.common.JsonNames
import org.shared.component.json.JsonComponentIn
import org.shared.currentConfig.json.{JsonCurrentConfigIn, JsonCurrentConfigOut}
import org.shared.error.{JsonErrorIn, JsonErrorParams}
import org.shared.nextStep.json.JsonNextStepIn
import org.shared.startConfig.json.JsonStartConfigIn
import play.api.Logger
import play.api.libs.json._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 23.12.2016
  */
//old
//trait MessageHandler {
//
//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param receivedMessage : JsValue, client: Config
//    * @return JsValue
//    */
//  def handleMessage(receivedMessage: JsValue, client: Config): JsValue = {
//    (receivedMessage \ "json").asOpt[String] match {
//      case Some("StartConfig") => startConfig(receivedMessage, client)
//      case Some("NextStep") => nextStep(receivedMessage, client)
//      case Some("CurrentConfig") => currentConfig(receivedMessage, client)
//      case Some("Component") => component(receivedMessage, client)
//      case _ => Json.obj("error" -> "keinen Treffer")
//    }
//  }
//
//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param receivedMessage : JsValue, client: Config
//    * @return JsValue
//    */
//  private def startConfig(receivedMessage: JsValue, client: Config): JsValue = {
//    val jsonStartConfigIn: JsResult[JsonStartConfigIn] = Json.fromJson[JsonStartConfigIn](receivedMessage)
//    jsonStartConfigIn match {
//      case s: JsSuccess[JsonStartConfigIn] => Json.toJson(client.startConfig(jsonStartConfigIn.get))
//      case e: JsError => jsonError(JsonNames.START_CONFIG, e)
//    }
//  }
//
//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param receiveMessage : JsValue, client: Config
//    * @return JsValue
//    */
//  private def nextStep(receiveMessage: JsValue, client: Config): JsValue = {
//    val jsonNextStepIn: JsResult[JsonNextStepIn] = Json.fromJson[JsonNextStepIn](receiveMessage)
//    jsonNextStepIn match {
//      case s: JsSuccess[JsonNextStepIn] => Json.toJson(client.nextStep)
//      case e: JsError => jsonError(JsonNames.NEXT_STEP, e)
//    }
//  }
//
//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param JsValue , Config
//    * @return JsValue
//    */
//  private def currentConfig(receivedMessage: JsValue, client: Config): JsValue = {
//    val jsonCurrentConfigIn: JsResult[JsonCurrentConfigIn] = Json.fromJson[JsonCurrentConfigIn](receivedMessage)
//    jsonCurrentConfigIn match {
//      case s: JsSuccess[JsonCurrentConfigIn] => s
//      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
//    }
//    val jsonCurrentConfigOut: JsonCurrentConfigOut = client.currentConfig(jsonCurrentConfigIn.get)
//    Json.toJson(jsonCurrentConfigOut)
//  }
//
//  def component(receivedMessage: JsValue, client: Config): JsValue = {
//    val jsonComponentIn: JsResult[JsonComponentIn] = Json.fromJson[JsonComponentIn](receivedMessage)
//    jsonComponentIn match {
//      case s: JsSuccess[JsonComponentIn] => s
//      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
//    }
//    val jsonComponentOut: JsonComponentOut = client.component(jsonComponentIn.get)
//    Json.toJson(jsonComponentOut)
//  }
//
//  private def jsonError(errorText: String, e: JsError): JsValue = {
//    val error = JsonErrorIn(
//      JsonNames.ERROR,
//      JsonErrorParams(
//        "Errors -> " + errorText + " : " + JsError.toJson(e).toString()
//      )
//    )
//    Logger.error(error.toString)
//    Json.toJson(error)
//  }
//}

//new
trait MessageHandler extends GenericConfigurator{

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage : JsValue, client: Config
    * @return JsValue
    */
  def handleMessage(receivedMessage: JsValue): JsValue = {
    (receivedMessage \ "json").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage)
      case Some("NextStep") => nextStep(receivedMessage)
      case Some("CurrentConfig") => currentConfig(receivedMessage)
      case Some("Component") => selectedComponent(receivedMessage)
      case error => jsonError(errorText = "Input JSON is not permitted")
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage : JsValue
    * @return JsValue
    */
  private def startConfig(receivedMessage: JsValue): JsValue = {
    val jsonStartConfigIn: JsResult[JsonStartConfigIn] = Json.fromJson[JsonStartConfigIn](receivedMessage)
    jsonStartConfigIn match {
      case s: JsSuccess[JsonStartConfigIn] => Json.toJson(startConfig(jsonStartConfigIn.get))
      case e: JsError => jsonError(JsonNames.START_CONFIG, e)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receiveMessage : JsValue
    * @return JsValue
    */
  private def nextStep(receiveMessage: JsValue): JsValue = {
    val jsonNextStepIn: JsResult[JsonNextStepIn] = Json.fromJson[JsonNextStepIn](receiveMessage)
    jsonNextStepIn match {
      case s: JsSuccess[JsonNextStepIn] => Json.toJson(getNextStep)
      case e: JsError => jsonError(JsonNames.NEXT_STEP, e)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage: JsValue
    * @return JsValue
    */
  private def currentConfig(receivedMessage: JsValue): JsValue = {
    val jsonCurrentConfigIn: JsResult[JsonCurrentConfigIn] = Json.fromJson[JsonCurrentConfigIn](receivedMessage)
    jsonCurrentConfigIn match {
      case s: JsSuccess[JsonCurrentConfigIn] => s
      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
    }
    val jsonCurrentConfigOut: JsonCurrentConfigOut = currentConfig(jsonCurrentConfigIn.get)
    Json.toJson(jsonCurrentConfigOut)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param receivedMessage: JsValue
    * @return JsValue
    */
  def selectedComponent(receivedMessage: JsValue): JsValue = {
    val jsonComponentIn: JsResult[JsonComponentIn] = Json.fromJson[JsonComponentIn](receivedMessage)
    jsonComponentIn match {
      case s: JsSuccess[JsonComponentIn] => Json.toJson(selectedComponent(jsonComponentIn.get))
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