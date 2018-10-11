package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.common.JsonNames
import org.shared.common.status.Success
import org.shared.component.status._
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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_2_Specs  extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Scenario 002_2 -> Specification spezifiziert CurrentConfig bei der doppeltem Auswahl der Komponente " >> {
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
      
      //User hat ausgewaelt Component 1
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
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_1 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      
      val statusSelectionCriterium = AllowNextComponent()
      (componentOut_1 \ "result" \ "status" \ "selectionCriterium" \ "status").asOpt[String].get === statusSelectionCriterium.status
      (componentOut_1 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === statusSelectionCriterium.message
      
      val statusSelectedComponent = AddedComponent()
      
      (componentOut_1 \ "result" \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent.status
      (componentOut_1 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === statusSelectedComponent.message
      
      val statusExcludeDependency = NotExcludedComponent()
      
      (componentOut_1 \ "result" \ "status" \ "excludeDependency" \ "status").asOpt[String].get === statusExcludeDependency.status
      (componentOut_1 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === statusExcludeDependency.message
      
      val statusCommon = Success()
      
      (componentOut_1 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === statusCommon.status
      (componentOut_1 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === statusCommon.message
      
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
      
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      
      val statusSelectionCriterium_2 = RequireComponent()
      (componentOut_2 \ "result" \ "status" \ "selectionCriterium" \ "status").asOpt[String].get === statusSelectionCriterium_2.status
      (componentOut_2 \ "result" \ "status" \ "selectionCriterium" \ "message").asOpt[String].get === statusSelectionCriterium_2.message
      
      val statusSelectedComponent_2 = RemovedComponent()
      
      (componentOut_2 \ "result" \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent_2.status
      (componentOut_2 \ "result" \ "status" \ "selectedComponent" \ "message").asOpt[String].get === statusSelectedComponent_2.message
      
      val statusExcludeDependency_2 = NotExcludedComponent()
      
      (componentOut_2 \ "result" \ "status" \ "excludeDependency" \ "status").asOpt[String].get === statusExcludeDependency_2.status
      (componentOut_2 \ "result" \ "status" \ "excludeDependency" \ "message").asOpt[String].get === statusExcludeDependency_2.message
      
      val statusCommon_2 = Success()
      
      (componentOut_2 \ "result" \ "status" \ "common" \ "status").asOpt[String].get === statusCommon_2.status
      (componentOut_2 \ "result" \ "status" \ "common" \ "message").asOpt[String].get === statusCommon_2.message
      
      
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
    }
  }
}