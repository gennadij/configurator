package models.v001

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import models.config.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.libs.json.JsValue
import models.status.StartConfigSuccessful
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */

@RunWith(classOf[JUnitRunner])
class StartConfigSpecs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert das Start der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen" >> {
      val configUrl = "http://contig1/user29_v016"
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
      (startConfigOut \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S1_user29_v016"
      (startConfigOut \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 3
      (((startConfigOut \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_1_1_user29_v016"
      (((startConfigOut \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_1_2_user29_v016"
      (((startConfigOut \ "result" \ "step" \ "components")(2)) \ "nameToShow") .asOpt[String].get === "C_1_3_user29_v016"
      val status = new StartConfigSuccessful
      (startConfigOut \ "result" \ "status").asOpt[String].get === status.status
      (startConfigOut \ "result" \ "message").asOpt[String].get === status.message
    }
  }
  
}