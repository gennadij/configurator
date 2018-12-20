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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_14_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2, C3, C1, C2, C3" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)

      val r_1 = componentOut_1 \ JsonKey.result \ JsonKey.status
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_1 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_1 \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (r_1 \JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (r_1 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_1 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
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

      val r_2 = componentOut_2 \ JsonKey.result \ JsonKey.status
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_2 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_2 \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (r_2 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (r_2 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_2 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
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

      val r_3 = componentOut_3 \ JsonKey.result \ JsonKey.status

      (componentOut_3 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_3 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_3 \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (r_3 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (r_3 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (r_3 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_3 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_3 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentOut_4: JsValue = CommonFunction.selectComponent(wC, componentIdC11)

      val r_4 = componentOut_4 \ JsonKey.result \ JsonKey.status
      (componentOut_4 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_4 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_4 \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (r_4 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (r_4 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_4 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentCongig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_4 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_4 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      
      
      val componentOut_5: JsValue = CommonFunction.selectComponent(wC, componentIdC12)

      val r_5 = componentOut_5 \ JsonKey.result \ JsonKey.status
      (componentOut_5 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_5 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_5 \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (r_5 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_COMPONENT"
      (r_5 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_5 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentCongig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_5 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0

      val componentOut_6: JsValue = CommonFunction.selectComponent(wC, componentIdC13)

      val r_6 = componentOut_6 \ JsonKey.result \ JsonKey.status

      (componentOut_6 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_6 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_6 \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (r_6 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (r_6 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_6 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_6: JsValue = CommonFunction.currentCongig(wC)
      
      val result_6 = (jsonCurrentConfigOut_6 \ "result")
      (jsonCurrentConfigOut_6 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_6 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_6 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C13")
      
      val componentOut_7: JsValue = CommonFunction.selectComponent(wC, componentIdC11)

      val r_7 = componentOut_7 \ JsonKey.result \ JsonKey.status

      (componentOut_7 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_7 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_7 \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (r_7 \ JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (r_7 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (r_7 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_7: JsValue = CommonFunction.currentCongig(wC)
      
      val result_7 = (jsonCurrentConfigOut_7 \ "result")
      (jsonCurrentConfigOut_7 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_7 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_7 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_7 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C13")
      
      val componentOut_8: JsValue = CommonFunction.selectComponent(wC, componentIdC12)

      val r_8 = componentOut_8 \ JsonKey.result \ JsonKey.status
      
      (componentOut_8 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_8 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_8 \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (r_8 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (r_8 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (r_8\"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_8: JsValue = CommonFunction.currentCongig(wC)
      
      val result_8 = (jsonCurrentConfigOut_8 \ "result")
      (jsonCurrentConfigOut_8 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_8 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_8 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_8 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C13")
      
      val componentOut_9: JsValue = CommonFunction.selectComponent(wC, componentIdC13)

      val r_9 = componentOut_9 \ JsonKey.result \ JsonKey.status

      (componentOut_9 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_9 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_9 \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (r_9 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_COMPONENT"
      (r_9 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_9 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_9: JsValue = CommonFunction.currentCongig(wC)
      
      val result_9 = (jsonCurrentConfigOut_9 \ "result")
      (jsonCurrentConfigOut_9 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_9 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_9 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0
    }
  }
}