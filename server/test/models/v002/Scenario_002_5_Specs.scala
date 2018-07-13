package models.v002

import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import models.genericConfig.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.Logger
import play.api.libs.json.JsValue
import models.persistence.orientdb.PropertyKeys

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
@RunWith(classOf[JUnitRunner])
class Scenario_002_5_Specs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1 | S2 -> C1 | S3 -> C1" >> {
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
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_2 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_2 = wC.handleMessage(jsonNextStepIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_2 " + jsonNextStepIn_2)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_2 " + jsonNextStepOut_2)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt Schritt 2
      val componentIdC21: String = (jsonNextStepOut_2 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_2_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC21 " + componentIdC21)
      
      val componentIn_2 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC21
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
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_3 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_3 = wC.handleMessage(jsonNextStepIn_3)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_3 " + jsonNextStepIn_3)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_3 " + jsonNextStepOut_3)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt
      val componentIdC31: String = (jsonNextStepOut_3 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_3_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC31 " + componentIdC31)
      
      val componentIn_3 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC31
           )
      )
      Logger.info("componentIn_3 " + componentIn_3)
      
      val componentOut_3: JsValue = wC.handleMessage(componentIn_3)
      
      Logger.info("componentOut_3 " + componentOut_3)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_4 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_4 = wC.handleMessage(jsonNextStepIn_4)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_4 " + jsonNextStepIn_4)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_4 " + jsonNextStepOut_4)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonCurrentConfigIn : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut: JsValue = wC.handleMessage(jsonCurrentConfigIn)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut)
      
      val result = (jsonCurrentConfigOut \ "result")
      (jsonCurrentConfigOut \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      ((result \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_1_user29_v016")
      ((result \ "step" \ "nextStep" \ "nameToShow").asOpt[String]) === Some("S2_user29_v016")
      ((result \ "step" \ "nextStep" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_2_1_user29_v016")
      ((result \ "step" \ "nextStep" \ "nextStep" \ "nameToShow").asOpt[String]) === Some("S3_user29_v016")
      ((result \ "step" \ "nextStep" \ "nextStep" \"components")(0) \ "nameToShow").asOpt[String] === Some("C_3_1_user29_v016")
    }
  }
}