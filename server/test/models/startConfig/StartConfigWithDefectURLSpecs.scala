package models.startConfig

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsNull, JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 08.11.2017
 */
@RunWith(classOf[JUnitRunner])
class StartConfigWithDefectURLSpecs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert das Start der Konfiguration mit defektem URL" >> {
    "Es wird keinen Step geladen" >> {
      val configUrl = "http://config/client_01"
      val startConfigIn = Json.obj(
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params -> Json.obj(
               JsonKey.configUrl -> configUrl
           )
      )

      Logger.info(startConfigIn.toString())
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)

      Logger.info(startConfigOut.toString())
      
      
      
      (startConfigOut \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      (startConfigOut \ JsonKey.result \ JsonKey.step).asOpt[JsValue].get === JsNull
      ((startConfigOut \ JsonKey.result \ JsonKey.errors)(0) \ JsonKey.message).asOpt[String].get ===
        "Der aufgeforderte Schritt (stepId = undefined) exestiert nicht in dem Datenbank"
      ((startConfigOut \ JsonKey.result \ JsonKey.errors)(0) \ JsonKey.name).asOpt[String].get === "STEP_NOT_EXIST"
      ((startConfigOut \ JsonKey.result \ JsonKey.errors)(0) \ JsonKey.code).asOpt[String].get === "E010001"
      (startConfigOut \ JsonKey.result \ JsonKey.warnings).asOpt[List[JsValue]] === None
    }
  }
}