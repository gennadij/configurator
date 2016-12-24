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

trait ConfigWeb {
  /**
   * DTOs
   * StartCOnfig
   *   Server <- Client
   *      {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
   *   Server -> Client
   *      {dtoId : 1, dto : StartConfig, result : {step: {id: #21:9, kind: "first" components:
   *      [{id: #22:9, kind: immutable, nextStep: #27:11}, ...]}}}
   * 
   */
  
  
  def handleMessage(receivedMessage: JsValue): JsValue = {
    (receivedMessage \ "dto").asOpt[String] match {
      case Some("StartConfig") => startConfig(receivedMessage)
//      case Some("Login") => login(receivedMessage)
//      case Some("ConfigUri") => configUri(receivedMessage)
//      case Some("FirstStep") => firstStep(receivedMessage)
//      case Some("ConfigTree") => configTree(receivedMessage)
//      case Some("Component") => component(receivedMessage)
//      case Some("ConnStepToComponent") => connStepToComponent(receivedMessage)
//      case Some("Step") => step(receivedMessage)
//      case Some("ConnComponentToStep") => connComponentToStep(receivedMessage)
      case _ => Json.obj("error" -> "keinen Treffer")
    }
  }
  
  private def startConfig(receivedMessage: JsValue): JsValue = {
    val startConfigCS: StartConfigCS = Json.fromJson[StartConfigCS](receivedMessage).get
    val startConfigSC: StartConfigSC = ConfigMgr.startConfig(startConfigCS)
    null
  }
}