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
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      Logger.info("IN -> " + startConfigIn.toString())
      Logger.info("OUT -> " + startConfigOut.toString())
      
      
      
      (startConfigOut \ "json").asOpt[String].get === JsonNames.START_CONFIG
      (startConfigOut \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S1"
      (startConfigOut \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 3
      ((startConfigOut \ "result" \ "step" \ "components")(0) \ "nameToShow") .asOpt[String].get === "C11"
      ((startConfigOut \ "result" \ "step" \ "components")(1) \ "nameToShow") .asOpt[String].get === "C12"
      ((startConfigOut \ "result" \ "step" \ "components")(2) \ "nameToShow") .asOpt[String].get === "C13"
      (startConfigOut \ "result" \ "status" \ "firstStep" \ "status").asOpt[String].get === "FIRST_STEP_EXIST"
      (startConfigOut \ "result" \ "status" \ "firstStep" \ "message").asOpt[String].get === ""
      (startConfigOut \ "result" \ "status" \ "nextStep").asOpt[String] === None
      (startConfigOut \ "result" \ "status" \ "fatherStep").asOpt[String] === None
      (startConfigOut \ "result" \ "status" \ "common" \ "status").asOpt[String].get === "SUCCESS"
      (startConfigOut \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist erfolgreich"
    }
  }
  
}