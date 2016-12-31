/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.config.web

/**
 * Created by Gennadi Heimann 23.12.2016
 */
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import org.dto.startConfig.StartConfigCS
import org.dto.startConfig.StartConfigSC
import org.configMgr.ConfigMgr
import org.dto.nextStep.NextStepCS
import org.dto.nextStep.NextStepSC

trait ConfigWeb {
  /**
   * DTOs
   * StartConfig
   *   Server <- Client
   *      {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
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
  
  
  def handleMessage(receivedMessage: JsValue): JsValue = {
    (receivedMessage \ "dto").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage)
      case Some("NextStep") => nextStep(receivedMessage)
      case _ => Json.obj("error" -> "keinen Treffer")
    }
  }
  
  private def startConfig(receivedMessage: JsValue): JsValue = {
    val startConfigCS: StartConfigCS = Json.fromJson[StartConfigCS](receivedMessage).get
    val startConfigSC: StartConfigSC = ConfigMgr.startConfig(startConfigCS)
    Json.toJson(startConfigSC)
  }
  
  private def nextStep(receiveMessage: JsValue): JsValue = {
    val nextStepCS: NextStepCS = Json.fromJson[NextStepCS](receiveMessage).get
    val nextStepSC: NextStepSC = ConfigMgr.nextStep(nextStepCS)
    Json.toJson(nextStepSC)
  }
}