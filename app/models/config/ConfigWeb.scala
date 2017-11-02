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

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 */

trait ConfigWeb {
  
  /**
   * DTOs
   * StartConfig
   *   Server <- Client
   *      {dtoId : 1, dto : StartConfig, params : {configUrl: http://config/test}}
   *   Server -> Client
   *      {dtoId : 1, dto : StartConfig, status: {kind: error, id: 12, message: Nachricht} result : {
   *         configId: #21:23, 
   *         step: {id: #21:9, kind: "first", 
   *            components: [{id: #22:9, kind: immutable}, ...]}}}
   *  NextStep
   *   Client -> Server
   *      {dtoId: 2, dto: "NextStep", params: {
   *         configId: #21:23, 
   *         componentIds: [#21:2, ... ]}}
   *   Server -> Client
   *      {dtoId: 2, dto: "NextStep", result: {
   *         configId: #21:23, 
   *         step :{id: #21:3, kind: "default",
   *            components: [{id: #45:2, kind: immutable}, ...]}}
   */
  
  def handleMessage(receivedMessage: JsValue, client: Config): JsValue = {
    (receivedMessage \ "dto").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage, client)
      case Some("NextStep") => nextStep(receivedMessage, client)
      case Some("CurrentConfig") => currentConfig(receivedMessage, client)
      case _ => Json.obj("error" -> "keinen Treffer")
    }
  }
  
  private def startConfig(receivedMessage: JsValue, client: Config): JsValue = {
    val jsonStartConfigIn: JsonStartConfigIn = Json.fromJson[JsonStartConfigIn](receivedMessage).get
    val jsonStartConfigOut: JsonStartConfigOut = client.startConfig(jsonStartConfigIn)
    Json.toJson(jsonStartConfigOut)
  }
  
  private def nextStep(receiveMessage: JsValue, client: Config): JsValue = {
    val jsonNextStepIn: JsonNextStepIn = Json.fromJson[JsonNextStepIn](receiveMessage).get
    val jsonNextStepOut: JsonNextStepOut = client.nextStep(jsonNextStepIn)
    Json.toJson(jsonNextStepOut)
  }
  
  private def currentConfig(receivedMessage: JsValue, client: Config): JsValue = {
    val jsonCurrentConfigIn: JsonCurrentConfigIn = Json.fromJson[JsonCurrentConfigIn](receivedMessage).get
    val jsonCurrentConfigOut: JsonCurrentConfigOut = client.currentConfig(jsonCurrentConfigIn)
    Json.toJson(jsonCurrentConfigOut)
  }
}