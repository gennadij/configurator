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
import models.status.RequireComponent
import models.status.AllowNextComponent

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
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11" + componentIdC11)
      val componentIds: List[String] = List(componentIdC11)
      
      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      
      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_1 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      
      val status = AllowNextComponent()
      (jsonComponentOut_1 \ "result" \ "status").asOpt[String].get === status.status
      (jsonComponentOut_1 \ "result" \ "message").asOpt[String].get === status.message
      
      //User hat ausgewaelt
      val componentIdC12: String = (((startConfigOut \ "result" \ "step" \ "components")(2)) \ "componentId") .asOpt[String].get
      
      val jsonComponentIn_2: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )
      
      val jsonComponentOut_2: JsValue = wC.handleMessage(jsonComponentIn_2)
      
      (jsonComponentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      
      val status_2 = AllowNextComponent()
      (jsonComponentOut_2 \ "result" \ "status").asOpt[String].get === status_2.status
      (jsonComponentOut_2 \ "result" \ "message").asOpt[String].get === status_2.message
    }
  }
  
}