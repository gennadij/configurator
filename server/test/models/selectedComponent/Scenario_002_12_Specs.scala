package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.shared.status.selectedComponent.{ExcludedComponentInternal, NotExcludedComponentInternal}
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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_12_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2, C3, C1" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      
      val componentIdC11: String = (startConfigOut \ JsonKey.result \ JsonKey.step \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head
      
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_1 \ JsonKey.result \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_1 \ JsonKey.result \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_1 \ JsonKey.result \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentCongig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentIdC12: String = (startConfigOut \ "result" \ JsonKey.step \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C12")
            .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head
      
      
      val componentOut_2: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentCongig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_2 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_2 \ JsonKey.step \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentIdC13: String = (startConfigOut \ "result" \ JsonKey.step \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C13")
            .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head
      
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_3 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_3 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (componentOut_3 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentOut_4: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_4 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_4 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_4 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_4 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentCongig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_4 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_4 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      
      
      val componentOut_5: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_5 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_5 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_5 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_COMPONENT"
      (componentOut_5 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_5 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentCongig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_5 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 0

      val componentOut_6: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_6 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_6 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_6 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_6 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_6 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_6 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_6: JsValue = CommonFunction.currentCongig(wC)
      
      val result_6 = (jsonCurrentConfigOut_6 \ "result")
      (jsonCurrentConfigOut_6 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_6 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_6 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C13")
      
      val componentOut_7: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_7 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_7 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_7 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (componentOut_7 \ "result" \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_7 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (componentOut_7 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_7: JsValue = CommonFunction.currentCongig(wC)
      
      val result_7 = (jsonCurrentConfigOut_7 \ "result")
      (jsonCurrentConfigOut_7 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_7 \ JsonKey.step \ "nameToShow").asOpt[String] === Some("S1")
      (result_7 \ JsonKey.step \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_7 \ JsonKey.step \ "components")(0) \ "nameToShow").asOpt[String] === Some("C13")
    }
  }
}