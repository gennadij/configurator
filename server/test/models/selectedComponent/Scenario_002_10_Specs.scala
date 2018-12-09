package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.JsonNames
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.JsValue
import util.CommonFunction

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_10_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentCongig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_1 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentIdC12: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C12")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_2: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentCongig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_2 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_2 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_2 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentIdC13: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C13")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_3 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_3 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "EXCLUDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_3 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_3 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentOut_4: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_4 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_4 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentCongig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_4 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_4 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      
      
      val componentOut_5: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_5 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_5 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentCongig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_5 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0
    }
  }
}