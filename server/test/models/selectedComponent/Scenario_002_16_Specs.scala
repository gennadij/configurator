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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_16_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification for scenario 16" >> {
    "S1 -> C1, C2, C3 | S2 -> C1, C2" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_1 \ JsonKey.result \ JsonKey.status \JsonKey.selectionCriterion \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
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

      val result_6 = componentOut_2 \ JsonKey.result \ JsonKey.status
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (result_6 \ "componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (result_6 \ "selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (result_6 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (result_6 \ JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (result_6 \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentCongig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ JsonKey.result \ JsonKey.step)
      (jsonCurrentConfigOut_2 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ "nameToShow").asOpt[String] === Some("S1")
      (result_2 \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_2 \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_2 \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentIdC13: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C13")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)

      val result_7 = componentOut_3 \ JsonKey.result \ JsonKey.status
      (componentOut_3 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (result_7 \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (result_7 \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (result_7 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (result_7 \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(ExcludedComponentInternal().status)
      (result_7 \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_3 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_3 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val jsonNextStepOut = CommonFunction.nextStep(wC)
      
      (jsonNextStepOut \ "json").asOpt[String].get === JsonNames.STEP
      (jsonNextStepOut \ "result" \ "step" \ "nameToShow").asOpt[String].get === "S2"
      (jsonNextStepOut \ "result" \ "step" \ "components").asOpt[Set[JsValue]].get.size === 2
      (((jsonNextStepOut \ "result" \ "step" \ "components")(0)) \ "nameToShow").asOpt[String].get === "C21"
      (((jsonNextStepOut \ "result" \ "step" \ "components")(1)) \ "nameToShow").asOpt[String].get === "C22"
      (jsonNextStepOut \ "result" \ "status" \ "firstStep").asOpt[String] === None
      (jsonNextStepOut \ "result" \ "status" \ "nextStep" \ "status").asOpt[String].get === "NEXT_STEP_EXIST"
      (jsonNextStepOut \ "result" \ "status" \ "fatherStep").asOpt[String] === None
      (jsonNextStepOut \ "result" \ "status" \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      val componentId21: String = (jsonNextStepOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C21")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
            
    
      val componentOut4_21 = CommonFunction.selectComponent(wC, componentId21)

      val result_8 = componentOut4_21 \ JsonKey.result \ JsonKey.status
      (componentOut4_21 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (result_8 \ "componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (result_8 \ "selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (result_8 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "REQUIRE_NEXT_STEP"
      (result_8 \ JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (result_8 \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentCongig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_4 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_4 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_4 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
      
      val componentId22: String = (jsonNextStepOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C22")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      val componentOut4_22 = CommonFunction.selectComponent(wC, componentId22)

      val r_9 = componentOut4_22 \ JsonKey.result \ JsonKey.status
      (componentOut4_22 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (r_9 \ "componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (r_9 \ "selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (r_9 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String].get === "NOT_ALLOW_NEXT_COMPONENT"
      (r_9 \ JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_9 \ "common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentCongig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result_5 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_5 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C12")
      ((result_5 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C11")
    }
  }
}