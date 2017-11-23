package models.v001

import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import models.config.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.Logger
import models.status.startConfig.StartConfigSuccessful
import play.api.libs.json.JsValue
import models.status.nextStep.NextStepSuccessful

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */
@RunWith(classOf[JUnitRunner])
class NextStepSpecs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component 1 ausgewaelt" >> {
      val configUrl = "http://contig1/user29_v016"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)
      
      //User hat ausgewaelt
      val componentIdC11: String = (((startConfigOut \ "result" \ "step" \ "components")(0)) \ "componentId") .asOpt[String].get
      
      Logger.info("componentIdC11" + componentIdC11)
      val componentIds: List[String] = List(componentIdC11)
      
      val jsonComponentIn: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      
//      val nextStepIn = Json.obj(
//          "json" -> JsonNames.NEXT_STEP
//          ,"params" -> Json.obj(
//               "componentIds" -> componentIds
//           )
//      )
      
      val jsonComponentOut: JsValue = wC.handleMessage(jsonComponentIn)
      
      Logger.info("NextStepIn " + jsonComponentIn)
      Logger.info("NextStepOut " + jsonComponentOut)
      
      (jsonComponentOut \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S2_user29_v016"
      (jsonComponentOut \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 2
      (((jsonComponentOut \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_2_1_user29_v016"
      (((jsonComponentOut \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_2_2_user29_v016"
//      (((nextStepOut \ "result" \ "step" \ "components")(2)) \ "nameToShow") .asOpt[String].get === "C_1_3_user29_v016"
      val status = new NextStepSuccessful
      (jsonComponentOut \ "result" \ "status").asOpt[String].get === status.status
      (jsonComponentOut \ "result" \ "message").asOpt[String].get === status.message
    }
  }
  
}