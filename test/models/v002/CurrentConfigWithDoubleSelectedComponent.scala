package models.v002

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
import models.status.AllowNextComponent
import models.status.NextStepSuccessful
import models.status.RequireNextStep
import models.status.FinalComponent
import models.status.NextStepError
import models.persistence.db.orientdb.PropertyKey

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
@RunWith(classOf[JUnitRunner])
class CurrentConfigWithDoubleSelectedComponent  extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert CurrentConfig bei der doppeltem Auswahl der Komponente " >> {
    "Die Komponente C_1_1_user29_v016 wird zwei mal ausgewaelt" >> {
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
      
      //User hat ausgewaelt Schritt 1
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)
      
      val componentIn_1 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      Logger.info("componentIn_1 " + componentIn_1)
      
      val componentOut_1: JsValue = wC.handleMessage(componentIn_1)
      
      Logger.info("componentOut_1 " + componentOut_1)
      
      val jsonCurrentConfigIn_1 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_1: JsValue = wC.handleMessage(jsonCurrentConfigIn_1)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_1)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_1)
      
       val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_1 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_1_user29_v016")
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val componentIn_2 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_2: JsValue = wC.handleMessage(componentIn_2)
      
      Logger.info("componentOut_2 " + componentOut_2)
      
      val jsonCurrentConfigIn_2 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_2: JsValue = wC.handleMessage(jsonCurrentConfigIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_2)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_2)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_2 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val componentIdC12: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_2_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      val componentIn_3 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_3: JsValue = wC.handleMessage(componentIn_3)
      
      Logger.info("componentOut_3 " + componentOut_3)
      
      val jsonCurrentConfigIn_3 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_3: JsValue = wC.handleMessage(jsonCurrentConfigIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_3)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_3)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_2_user29_v016")
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val componentIn_4 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )
      Logger.info("componentIn_4 " + componentIn_4)
      
      val componentOut_4: JsValue = wC.handleMessage(componentIn_4)
      
      Logger.info("componentOut_4 " + componentOut_4)
      
      val jsonCurrentConfigIn_4 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_4: JsValue = wC.handleMessage(jsonCurrentConfigIn_4)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_4)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_4)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_4 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0
    }
  }
}