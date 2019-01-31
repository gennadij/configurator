package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
@RunWith(classOf[JUnitRunner])
class Scenario_002_5_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1 | S2 -> C1 | S3 -> C1" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params-> Json.obj(
               JsonKey.configUrl -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)
      
      //User hat ausgewaelt Schritt 1
      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)
      
      val componentIn_1 = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC11
           )
      )
      Logger.info("componentIn_1 " + componentIn_1)
      
      val componentOut_1: JsValue = wC.handleMessage(componentIn_1, wC.currentConfig)
      
      Logger.info("componentOut_1 " + componentOut_1)
      
      val jsonCurrentConfigIn_1 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_1: JsValue = wC.handleMessage(jsonCurrentConfigIn_1, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_1)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_1)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_2 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_2 = wC.handleMessage(jsonNextStepIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_2 " + jsonNextStepIn_2)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_2 " + jsonNextStepOut_2)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt Schritt 2
      val nextStepOut_2 = Json.fromJson[JsonStepOut](jsonNextStepOut_2)

      val componentIdC21: String = nextStepOut_2.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C21")
        .map(_.componentId).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC21 " + componentIdC21)
      
      val componentIn_2 = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC21
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_2: JsValue = wC.handleMessage(componentIn_2, wC.currentConfig)
      
      Logger.info("componentOut_2 " + componentOut_2)
      
      val jsonCurrentConfigIn_2 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_2: JsValue = wC.handleMessage(jsonCurrentConfigIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_2)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_2)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_3 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_3 = wC.handleMessage(jsonNextStepIn_3, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_3 " + jsonNextStepIn_3)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_3 " + jsonNextStepOut_3)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt

      val nextStepOut_3 = Json.fromJson[JsonStepOut](jsonNextStepOut_3)

      val componentIdC31: String = nextStepOut_3.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C31")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC31 " + componentIdC31)
      
      val componentIn_3 = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC31
           )
      )
      Logger.info("componentIn_3 " + componentIn_3)
      
      val componentOut_3: JsValue = wC.handleMessage(componentIn_3, wC.currentConfig)
      
      Logger.info("componentOut_3 " + componentOut_3)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_4 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_4 = wC.handleMessage(jsonNextStepIn_4, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_4 " + jsonNextStepIn_4)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_4 " + jsonNextStepOut_4)
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonCurrentConfigIn : JsValue = Json.obj(
          JsonKey.json -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut: JsValue = wC.handleMessage(jsonCurrentConfigIn, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut)
      
      val result = (jsonCurrentConfigOut \ "result")
      (jsonCurrentConfigOut \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      ((result \ JsonKey.step \ "components")(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      ((result \ JsonKey.step \ "nextStep" \ JsonKey.nameToShow).asOpt[String]) === Some("S2")
      ((result \ JsonKey.step \ "nextStep" \ "components")(0) \ JsonKey.nameToShow).asOpt[String] === Some("C21")
      ((result \ JsonKey.step \ "nextStep" \ "nextStep" \ JsonKey.nameToShow).asOpt[String]) === Some("S3")
      ((result \ JsonKey.step \ "nextStep" \ "nextStep" \"components")(0) \ JsonKey.nameToShow).asOpt[String] === Some("C31")
    }
  }
}