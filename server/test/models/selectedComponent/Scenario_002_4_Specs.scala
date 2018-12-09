package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.JsonNames
import org.shared.status.common.Success
import org.shared.status.selectedComponent._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_4_Specs  extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert CurrentConfig bei der doppeltem Auswahl der Komponente " >> {
    "Die Komponente C11, C12, C12 wird ausgewaelt" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)
      
      //User hat ausgewaelt Component 1
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)
      
      val componentIn_1 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      Logger.info("componentIn_1 " + componentIn_1)
      
      val componentOut_1: JsValue = wC.handleMessage(componentIn_1, wC.currentConfig)
      Logger.info("componentOut_1 " + componentOut_1)
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT

      val statusSelectionCriterium = AllowNextComponent()
      (componentOut_1 \ "result" \ "status" \ "selectionCriterium" \ "status").asOpt[String].get === statusSelectionCriterium.status

      val statusSelectedComponent = AddedComponent()
      
      (componentOut_1 \ "result" \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent.status

      val statusExcludeDependency = NotExcludedComponent()
      
      (componentOut_1 \ "result" \ "status" \ "excludeDependency" \ "status").asOpt[String].get === statusExcludeDependency.status

      val statusCommon = Success()
      
      (componentOut_1 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === statusCommon.status

      val jsonCurrentConfigIn_1 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_1: JsValue = wC.handleMessage(jsonCurrentConfigIn_1, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_1)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_1)
      
       val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_1 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C11")
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt Component 1
      val componentIdC12: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C12")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      val componentIn_2 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_2: JsValue = wC.handleMessage(componentIn_2, wC.currentConfig)
      
      Logger.info("componentOut_2 " + componentOut_2)
      
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT

      val statusSelectionCriterium_2 = RequireNextStep()
      (componentOut_2 \ "result" \ "status" \ "selectionCriterium" \ "status").asOpt[String].get === statusSelectionCriterium_2.status

      val statusSelectedComponent_2 = AddedComponent()
      
      (componentOut_2 \ "result" \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent_2.status

      val statusExcludeDependency_2 = NotExcludedComponent()
      
      (componentOut_2 \ "result" \ "status" \ "excludeDependency" \ "status").asOpt[String].get === statusExcludeDependency_2.status

      val statusCommon_2 = Success()
      
      (componentOut_2 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === statusCommon_2.status

      
      val jsonCurrentConfigIn_2 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_2: JsValue = wC.handleMessage(jsonCurrentConfigIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_2)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_2)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_2 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val componentIn_3 = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )
      Logger.info("componentIn_3 " + componentIn_3)
      
      val componentOut_3: JsValue = wC.handleMessage(componentIn_3, wC.currentConfig)
      
      Logger.info("componentOut_3 " + componentOut_3)
      
      (componentOut_3 \ "json").asOpt[String].get === JsonNames.COMPONENT

      val statusSelectionCriterium_3 = RequireNextStep()
      (componentOut_2 \ "result" \ "status" \ "selectionCriterium" \ "status").asOpt[String].get === statusSelectionCriterium_3.status

      val statusSelectedComponent_3 = RemovedComponent()
      
      (componentOut_3 \ "result" \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent_3.status

      val statusExcludeDependency_3 = NotExcludedComponent()
      
      (componentOut_3 \ "result" \ "status" \ "excludeDependency" \ "status").asOpt[String].get === statusExcludeDependency_3.status

      val statusCommon_3 = Success()
      
      (componentOut_3 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === statusCommon_3.status

      val jsonCurrentConfigIn_3 : JsValue = Json.obj(
          "json" -> JsonNames.CURRENT_CONFIG
      )
      
      val jsonCurrentConfigOut_3: JsValue = wC.handleMessage(jsonCurrentConfigIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_3)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_3)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_3 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C11")
    }
  }
}