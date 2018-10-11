package models.nextStep

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.common.JsonNames
import org.shared.nextStep.json.JsonNextStepOut
import org.shared.startConfig.json.JsonStartConfigOut
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import util.CommonFunction


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 10.11.2017
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class FinalStepSpecs extends Specification with MessageHandler with BeforeAfterAll{
  
  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component 1 ausgewaelt" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      
      Logger.info("IN " + startConfigIn)
      Logger.info("OUT" + startConfigOut)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut)

      //User hat ausgewaelt

      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      val componentIn_1 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )

      val jsonComponentOut_1: JsValue = wC.handleMessage(componentIn_1)
      
      Logger.info("OUT " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_1 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "visualization").asOpt[String].get === "undef"
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "nameToShow").asOpt[String].get === "(C11) ----> (C13)"
      (jsonComponentOut_1 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (jsonComponentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (jsonComponentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"

      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      Logger.info(this.getClass.getSimpleName + ": currentConfig " + CommonFunction.currentCongig(wC))
      
      val jsonNextStepIn_2 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_2 = wC.handleMessage(jsonNextStepIn_2)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_2 " + jsonNextStepIn_2)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_2 " + jsonNextStepOut_2)
      
      (jsonNextStepOut_2 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (jsonNextStepOut_2 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S2"
      (jsonNextStepOut_2 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 2
      (((jsonNextStepOut_2 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C21"
      (((jsonNextStepOut_2 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C22"
      (jsonNextStepOut_2 \ "result" \ "status" \ "firstStep").asOpt[String] === None
      (jsonNextStepOut_2 \ "result" \ "status" \ "nextStep" \ "status").asOpt[String].get === "NEXT_STEP_EXIST"
      (jsonNextStepOut_2 \ "result" \ "status" \ "currentStep").asOpt[String] === None
      (jsonNextStepOut_2 \ "result" \ "status" \ "currentConfig" \ "status").asOpt[String].get === "STEP_CURRENT_CONFIG_SUCCESS"
      (jsonNextStepOut_2 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")


      val jNSOut = Json.fromJson[JsonNextStepOut](jsonNextStepOut_2)

      //User hat ausgewaelt Schritt 2
      val componentIdC21: String = jNSOut.get.result.step.components.filter(comp => comp.nameToShow == "C21")
        .map(_.componentId).head

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
      (componentOut_2 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 0
      (componentOut_2 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"

      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_3 : JsValue = Json.obj(
          "json" -> JsonNames.NEXT_STEP
      )
      
      val jsonNextStepOut_3 = wC.handleMessage(jsonNextStepIn_3)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_3 " + jsonNextStepIn_3)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_3 " + jsonNextStepOut_3)
      
      (jsonNextStepOut_3 \ "json").asOpt[String].get === JsonNames.NEXT_STEP
      (jsonNextStepOut_3 \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S3"
      (jsonNextStepOut_3 \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 4
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(0)) \ "nameToShow") .asOpt[String].get === "C32"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(1)) \ "nameToShow") .asOpt[String].get === "C34"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(2)) \ "nameToShow") .asOpt[String].get === "C33"
      (((jsonNextStepOut_3 \ "result" \ "step" \ "components")(3)) \ "nameToShow") .asOpt[String].get === "C31"
      (jsonNextStepOut_3 \ "result" \ "status" \ "firstStep").asOpt[String] === None
      (jsonNextStepOut_3 \ "result" \ "status" \ "nextStep" \ "status").asOpt[String].get === "NEXT_STEP_EXIST"
      (jsonNextStepOut_3 \ "result" \ "status" \ "currentStep").asOpt[String] === None
      (jsonNextStepOut_3 \ "result" \ "status" \ "currentConfig" \ "status").asOpt[String].get === "STEP_CURRENT_CONFIG_SUCCESS"
      (jsonNextStepOut_3 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")

      val jNSOut_3 = Json.fromJson[JsonNextStepOut](jsonNextStepOut_3)

      //User hat ausgewaelt Schritt 2
      val componentIdC31: String = jNSOut_3.get.result.step.components.filter(comp => comp.nameToShow == "C31")
        .map(_.componentId).head

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
      (componentOut_3 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 0
      (componentOut_3 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_3 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "FINAL_COMPONENT"

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