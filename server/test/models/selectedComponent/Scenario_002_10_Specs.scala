package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.{AllowNextComponent, RequireComponent, RequireNextStep}
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.warning.ExcludedComponentInternal
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.{JsObject, JsValue, Json}
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

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentConfig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC12: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C12")
        .map(_.componentId).head

      val componentOut_2: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentConfig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_2 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_2 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC13: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_3 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      val rCOut_3 = componentOut_3 \ JsonKey.result
      (rCOut_3 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 2
      (rCOut_3 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 2
      (rCOut_3 \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (rCOut_3 \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (rCOut_3 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (rCOut_3 \ JsonKey.warning \ JsonKey.excludedComponentInternal \ JsonKey.name).asOpt[String] === Some(ExcludedComponentInternal().name)
      (rCOut_3 \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentConfig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      
      val componentOut_4: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_4 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      val rCOut_4 = componentOut_4 \ JsonKey.result
      (rCOut_4 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      (rCOut_4 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 1
      (rCOut_4 \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (rCOut_4 \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (rCOut_4 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === AllowNextComponent().name
      (rCOut_4 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (rCOut_4 \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentConfig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_4 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_4 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      
      
      val componentOut_5: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_5 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      val rCOut_5 = componentOut_5 \ JsonKey.result
      (rCOut_5 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      (rCOut_5 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 1
      (rCOut_5 \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (rCOut_5 \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (rCOut_5 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireComponent().name
      (rCOut_5 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (rCOut_5 \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentConfig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_5 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0
    }
  }
}