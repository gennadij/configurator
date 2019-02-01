package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.status.common.Success
import org.shared.status.selectedComponent._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_2_Specs  extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Scenario 002_2 -> Specification spezifiziert CurrentConfig bei der doppeltem Auswahl der Komponente " >> {
    "Die Komponente C11 wird zwei mal ausgewaelt" >> {
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

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

//      //User hat ausgewaelt
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
      
      (componentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (componentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      ((componentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((componentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.visualization).asOpt[String].get === "undef"
      ((componentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C11) ----> (C13)"

      (componentOut_1 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_1 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOut_1 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut_1 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigIn_1 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_1: JsValue = wC.handleMessage(jsonCurrentConfigIn_1, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_1)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_1)
      
       val result_1 = (jsonCurrentConfigOut_1 \ JsonKey.result)
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val componentIn_2 = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC11
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_2: JsValue = wC.handleMessage(componentIn_2, wC.currentConfig)
      
      Logger.info("componentOut_2 " + componentOut_2)
      
      (componentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (componentOut_2 \ JsonKey.result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      ((componentOut_2 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((componentOut_2 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.visualization).asOpt[String].get === "undef"
      ((componentOut_2 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C11) ----> (C13)"

      (componentOut_2 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === info.RequireComponent().name
      (componentOut_2 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_2 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (componentOut_2 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut_2 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigIn_2 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_2: JsValue = wC.handleMessage(jsonCurrentConfigIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_2)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_2)
      
      val result_2 = (jsonCurrentConfigOut_2 \ JsonKey.result)
      (jsonCurrentConfigOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0
    }
  }
}