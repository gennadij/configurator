package models.config

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import models.persistence.Persistence
import models.json.startConfig.JsonStartConfigOut
import models.json.nextStep.JsonNextStepIn
import models.json.nextStep.JsonNextStepOut
import models.json.currentConfig.JsonCurrentConfigIn
import models.json.currentConfig.JsonCurrentConfigOut
import models.currentConfig.CurrentConfig
import models.json.startConfig.JsonStartConfigIn
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import models.json.JsonNames
import play.api.Logger
import models.json.component.JsonComponentIn
import models.json.component.JsonComponentOut

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 */

trait ConfigWeb {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsValue, Config
   * 
   * @return JsValue
   */
  def handleMessage(receivedMessage: JsValue, client: Config): JsValue = {
    (receivedMessage \ "json").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage, client)
      case Some("NextStep") => nextStep(receivedMessage, client)
      case Some("CurrentConfig") => currentConfig(receivedMessage, client)
      case Some("Component") => component(receivedMessage, client)
      case _ => Json.obj("error" -> "keinen Treffer")
    }
  }

  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsValue, Config
   * 
   * @return JsValue
   */
  private def startConfig(receivedMessage: JsValue, client: Config): JsValue = {
    val jsonStartConfigIn: JsResult[JsonStartConfigIn] = Json.fromJson[JsonStartConfigIn](receivedMessage)
    jsonStartConfigIn match {
      case s: JsSuccess[JsonStartConfigIn] => s
      case e: JsError => Logger.error("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
    }
    val jsonStartConfigOut: JsonStartConfigOut = client.startConfig(jsonStartConfigIn.get)
    Json.toJson(jsonStartConfigOut)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsValue, Config
   * 
   * @return JsValue
   */
  private def nextStep(receiveMessage: JsValue, client: Config): JsValue = {
    val jsonNextStepIn: JsResult[JsonNextStepIn] = Json.fromJson[JsonNextStepIn](receiveMessage)
    jsonNextStepIn match {
      case s : JsSuccess[JsonNextStepIn] => s
      case e : JsError => Logger.error("Errors -> " + JsonNames.NEXT_STEP + ": " + JsError.toJson(e).toString())
    }
    val jsonNextStepOut: JsonNextStepOut = client.nextStep(jsonNextStepIn.get)
    Json.toJson(jsonNextStepOut)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param JsValue, Config
   * 
   * @return JsValue
   */
  private def currentConfig(receivedMessage: JsValue, client: Config): JsValue = {
    val jsonCurrentConfigIn: JsResult[JsonCurrentConfigIn] = Json.fromJson[JsonCurrentConfigIn](receivedMessage)
    jsonCurrentConfigIn match {
      case s: JsSuccess[JsonCurrentConfigIn] => s
      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
    }
    val jsonCurrentConfigOut: JsonCurrentConfigOut = client.currentConfig(jsonCurrentConfigIn.get)
    Json.toJson(jsonCurrentConfigOut)
  }
  
  def component(receivedMessage: JsValue, client: Config): JsValue = {
    val jsonComponentIn: JsResult[JsonComponentIn] = Json.fromJson[JsonComponentIn](receivedMessage)
    jsonComponentIn match {
      case s: JsSuccess[JsonComponentIn] => s
      case e: JsError => Logger.error("Errors -> " + JsonNames.CURRENT_CONFIG + ": " + JsError.toJson(e).toString())
    }
    val jsonComponentOut: JsonComponentOut = client.component(jsonComponentIn.get)
    Json.toJson(jsonComponentOut)
  }
}