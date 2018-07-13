package models.v001

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
import util.CommonFunction


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 10.11.2017
 */
@RunWith(classOf[JUnitRunner])
class FinalStepSpecs extends Specification with ConfigWeb with BeforeAfterAll{
  
  val wC: WebClient = WebClient.init
  
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
      
      //User hat ausgewaelt Schritt 1
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
//      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)
      
      val componentIn_1 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
//      Logger.info("componentIn_1 " + componentIn_1)
      
      val componentOut_1: JsValue = wC.handleMessage(componentIn_1)
      
      Logger.info("componentOut_1 " + componentOut_1)
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_1 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      (componentOut_1 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === 
        "Sie koennen weitere Komponente auswaelen"
      (componentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === 
        "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
      (componentOut_1 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === 
        "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
      (componentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_1 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist erfolgreich"
      (componentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \ "componentType" \ "message").asOpt[String].get === ""
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      Logger.info(this.getClass.getSimpleName + ": currentConfig " + CommonFunction.currentCongig(wC))
      
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
      
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 0
      (componentOut_2 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === 
        "Es darf keine weitere Komponente ausgewaelt werden und es muss naechste Schhritt geladen werden"
      (componentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === 
        "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
      (componentOut_2 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === 
        "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
      (componentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_2 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist erfolgreich"
      (componentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_2 \ "result" \ "status" \ "componentType" \ "message").asOpt[String].get === ""
      
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_3 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_3 = wC.handleMessage(jsonNextStepIn_3)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_3 " + jsonNextStepIn_3)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_3 " + jsonNextStepOut_3)
      
      (jsonNextStepOut_3 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (jsonNextStepOut_3 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S3_user29_v016"
      (jsonNextStepOut_3 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 4
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C_3_1_user29_v016"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C_3_2_user29_v016"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(2)) \ "nameToShow") .asOpt[String].get === "C_3_3_user29_v016"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(3)) \ "nameToShow") .asOpt[String].get === "C_3_4_user29_v016"
//      val status_5 = NextStepSuccessful()
//      (jsonNextStepOut_3 \ "result" \ "status").asOpt[String].get === status_5.status
//      (jsonNextStepOut_3 \ "result" \ "message").asOpt[String].get === status_5.message
      
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
      
      (componentOut_3 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_3 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 0
      (componentOut_3 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === 
        "Es darf keine weitere Komponente ausgewaelt werden und es muss naechste Schhritt geladen werden"
      (componentOut_3 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === 
        "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
      (componentOut_3 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === 
        "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
      (componentOut_3 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_3 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist erfolgreich"
      (componentOut_3 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "FINAL_COMPONENT"
      (componentOut_3 \ "result" \ "status" \ "componentType" \ "message").asOpt[String].get === ""
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_4 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_4 = wC.handleMessage(jsonNextStepIn_4)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_4 " + jsonNextStepIn_4)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_4 " + jsonNextStepOut_4)
      
      (jsonNextStepOut_4 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (jsonNextStepOut_4 \ "result" \ "step").asOpt[String] === None
    }
  }
}