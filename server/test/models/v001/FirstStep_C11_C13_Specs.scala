package models.v001

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.shared.common.JsonNames
import org.shared.startConfig.json.JsonStartConfigOut
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.Json
import play.api.Logger
import play.api.libs.json.JsValue


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 04.12.2017
 */

@RunWith(classOf[JUnitRunner])
class FirstStep_C11_C13_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component_1_1 und Componente_1_3  ausgewaelt" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut)

      //User hat ausgewaelt
      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head
      
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
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "undef"
      (((jsonComponentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C11) ----> (C13)"
      (jsonComponentOut_1 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === 
        "Sie koennen weitere Komponente auswaelen"
      (jsonComponentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === 
        "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
      (jsonComponentOut_1 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === 
        "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
      (jsonComponentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (jsonComponentOut_1 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === "Die Aktion ist erfolgreich"
      (jsonComponentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \ "componentType" \ "message").asOpt[String].get === ""
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val sCOut_2 = Json.fromJson[JsonStartConfigOut](startConfigOut)

      //User hat ausgewaelt
      val componentIdC13: String = sCOut_2.get.result.step.components.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC13 " + componentIdC13)
      
      val jsonComponentIn_2: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC13
           )
      )
      
      val jsonComponentOut_2: JsValue = wC.handleMessage(jsonComponentIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_2 " + jsonComponentIn_2)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_2 " + jsonComponentOut_2)
      
      (jsonComponentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 2
      (jsonComponentOut_2 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (jsonComponentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (jsonComponentOut_2 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "EXCLUDED_COMPONENT"
      (jsonComponentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (jsonComponentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
    }
  }
}