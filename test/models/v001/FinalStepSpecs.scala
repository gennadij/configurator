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
import models.status.startCongig.StartConfigSuccessful
import play.api.libs.json.JsValue
import models.status.nextStep.NextStepSuccessful
import models.status.nextStep.FinalStepSuccessful

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 10.11.2017
 */
@RunWith(classOf[JUnitRunner])
class FinalStepSpecs extends Specification with ConfigWeb with BeforeAfterAll{

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
      
      Logger.info("componentIdC11 " + componentIdC11)
      val componentIds_1: List[String] = List(componentIdC11)
      
      val nextStepIn_1 = Json.obj(
          "json" -> JsonNames.NEXT_STEP
          ,"params" -> Json.obj(
               "componentIds" -> componentIds_1
           )
      )
      
      val nextStepOut_1: JsValue = wC.handleMessage(nextStepIn_1)
      
      Logger.info("NextStepIn_1 " + nextStepIn_1)
      Logger.info("NextStepOut_1 " + nextStepOut_1)
      
      (nextStepOut_1 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (nextStepOut_1 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S2_user29_v016"
      (nextStepOut_1 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 2
      (((nextStepOut_1 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_2_1_user29_v016"
      (((nextStepOut_1 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_2_2_user29_v016"
      val status_1 = new NextStepSuccessful
      (nextStepOut_1 \ "result" \ "status").asOpt[String].get === status_1.status
      (nextStepOut_1 \ "result" \ "message").asOpt[String].get === status_1.message
      
      //User hat ausgewaelt
    
      val componentIdC21 = (((nextStepOut_1 \ "result" \ "step" \ "components")(0)) \ "componentId") .asOpt[String].get
      
      Logger.info("componentIdC21 " + componentIdC21)
      val componentIds_2: List[String] = List(componentIdC21)
      
      val nextStepIn_2 = Json.obj(
          "json" -> JsonNames.NEXT_STEP
          ,"params" -> Json.obj(
               "componentIds" -> componentIds_2
           )
      )
      
      val nextStepOut_2: JsValue = wC.handleMessage(nextStepIn_2)
      
      Logger.info("NextStepIn " + nextStepIn_2)
      Logger.info("NextStepOut " + nextStepOut_2)
      
      (nextStepOut_2 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (nextStepOut_2 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S3_user29_v016"
      (nextStepOut_2 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 4
      (((nextStepOut_2 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_3_1_user29_v016"
      (((nextStepOut_2 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_3_2_user29_v016"
      (((nextStepOut_2 \ "result" \ "step" \ "components")(2)) \ "nameToShow") .asOpt[String].get === "C_3_3_user29_v016"
      (((nextStepOut_2 \ "result" \ "step" \ "components")(3)) \ "nameToShow") .asOpt[String].get === "C_3_4_user29_v016"
      val status_2 = new NextStepSuccessful
      (nextStepOut_2 \ "result" \ "status").asOpt[String].get === status_2.status
      (nextStepOut_2 \ "result" \ "message").asOpt[String].get === status_2.message
      
      //User hat ausgewaelt
    
      val componentIdC31 = (((nextStepOut_2 \ "result" \ "step" \ "components")(0)) \ "componentId") .asOpt[String].get
      
      Logger.info("componentIdC31 " + componentIdC31)
      val componentIds_3: List[String] = List(componentIdC31)
      
      val nextStepIn_3 = Json.obj(
          "json" -> JsonNames.NEXT_STEP
          ,"params" -> Json.obj(
               "componentIds" -> componentIds_3
           )
      )
      
      val nextStepOut_3: JsValue = wC.handleMessage(nextStepIn_3)
      
      Logger.info("NextStepIn " + nextStepIn_3)
      Logger.info("NextStepOut " + nextStepOut_3)
      
      (nextStepOut_3 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (nextStepOut_3 \ "result" \ "step").asOpt[String] === None
      val status_3 = new FinalStepSuccessful
      (nextStepOut_3 \ "result" \ "status").asOpt[String].get === status_3.status
      (nextStepOut_3 \ "result" \ "message").asOpt[String].get === status_3.message
      
      
    }
  }
  
}