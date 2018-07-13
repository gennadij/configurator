package models.v001

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import models.genericConfig.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.libs.json.JsValue
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 08.11.2017
 */
@RunWith(classOf[JUnitRunner])
class StartConfigWithDefectURLSpecs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert das Start der Konfiguration mit defektem URL" >> {
    "Es wird keinen Step geladen" >> {
      val configUrl = "http://contig1/user29_v0"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      Logger.info(startConfigIn.toString())
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