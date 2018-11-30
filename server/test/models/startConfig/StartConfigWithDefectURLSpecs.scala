package models.startConfig

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.JsonNames
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

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
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )

      Logger.info(startConfigIn.toString())
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)

      Logger.info(startConfigOut.toString())
      
      
      
      (startConfigOut \ "json").asOpt[String].get === JsonNames.START_CONFIG
      (startConfigOut \ "result" \ "step").asOpt[String] === None
      (startConfigOut \ "result" \ "step" \ "nameToShow").asOpt[String].get === ""
      (startConfigOut \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 0
      (startConfigOut \ "result" \ "status" \ "firstStep" \ "status").asOpt[String].get === "FIRST_STEP_NOT_EXIST"
      (startConfigOut \ "result" \ "status" \ "firstStep" \ "message").asOpt[String].get === ""
      (startConfigOut \ "result" \ "status" \ "nextStep").asOpt[String] === None
      (startConfigOut \ "result" \ "status" \ "fatherStep").asOpt[String] === None
      (startConfigOut \ "result" \ "status" \ "common" \ "status").asOpt[String].get === "ERROR"
      (startConfigOut \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist nicht erfolgreich"
    }
  }
}