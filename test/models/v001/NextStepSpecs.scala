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
import play.api.libs.json.JsValue
import models.status.RequireComponent
import models.status.AllowNextComponent
import models.status.RequireNextStep
import models.status.NextStepSuccessful

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 07.12.2017
 */
@RunWith(classOf[JUnitRunner])
class NextStepSpecs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {   
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component_1_1 und Componente_1_2  ausgewaelt"  + 
    " und naechste Step geladen">> {
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
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11" + componentIdC11)
      val componentIds: List[String] = List(componentIdC11)
      
      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      
      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_1 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      
      val status = AllowNextComponent()
      (jsonComponentOut_1 \ "result" \ "status").asOpt[String].get === status.status
      (jsonComponentOut_1 \ "result" \ "message").asOpt[String].get === status.message
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt
      val componentIdC12: String = 
        (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_2_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC12 " + componentIdC12)
      
      val jsonComponentIn_2: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )
      
      val jsonComponentOut_2: JsValue = wC.handleMessage(jsonComponentIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_2 " + jsonComponentIn_2)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_2 " + jsonComponentOut_2)
      
      (jsonComponentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((jsonComponentOut_2 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_2_user29_v016) ----> (C_1_3_user29_v016)"
      val status_2 = RequireNextStep()
      (jsonComponentOut_2 \ "result" \ "status").asOpt[String].get === status_2.status
      (jsonComponentOut_2 \ "result" \ "message").asOpt[String].get === status_2.message
    
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_2 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_2 = wC.handleMessage(jsonNextStepIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_2 " + jsonNextStepIn_2)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_2 " + jsonNextStepOut_2)
      
      (jsonNextStepOut_2 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (jsonNextStepOut_2 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S2_user29_v016"
      (jsonNextStepOut_2 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 2
      (((jsonNextStepOut_2 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_2_1_user29_v016"
      (((jsonNextStepOut_2 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_2_2_user29_v016"
      val status_3 = NextStepSuccessful()
      (jsonNextStepOut_2 \ "result" \ "status").asOpt[String].get === status_3.status
      (jsonNextStepOut_2 \ "result" \ "message").asOpt[String].get === status_3.message
    }
  }
  
}