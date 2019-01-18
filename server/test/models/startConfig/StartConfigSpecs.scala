package models.startConfig

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */

@RunWith(classOf[JUnitRunner])
class StartConfigSpecs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert das Start der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params -> Json.obj(
               JsonKey.configUrl -> configUrl,
               JsonKey.componentId -> ""
           )
      )
      
      
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      Logger.info("IN -> " + startConfigIn.toString())
      Logger.info("OUT -> " + startConfigOut.toString())
      
      
      
      (startConfigOut \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      (startConfigOut \ JsonKey.result \ JsonKey.step \ JsonKey.nameToShow).asOpt[String].get === "S1"
      (startConfigOut \ JsonKey.result \ JsonKey.componentsForSelection).asOpt[Set[JsValue]].get.size === 3
      ((startConfigOut \ JsonKey.result \ JsonKey.componentsForSelection )(0) \ JsonKey.nameToShow).asOpt[String].get === "C11"
      ((startConfigOut \ JsonKey.result \ JsonKey.componentsForSelection )(1) \ JsonKey.nameToShow).asOpt[String].get === "C12"
      ((startConfigOut \ JsonKey.result \ JsonKey.componentsForSelection )(2) \ JsonKey.nameToShow).asOpt[String].get === "C13"
      (startConfigOut \ JsonKey.result \ JsonKey.errors).asOpt[List[JsObject]] === None
      (startConfigOut \ JsonKey.result \ JsonKey.warnings).asOpt[List[JsObject]] === None
    }
  }
  
}