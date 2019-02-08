package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.RequireComponent
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
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

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC11)

      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentConfig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ JsonKey.result)
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC12: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C12")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC12)

      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentConfig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ JsonKey.result)
      (jsonCurrentConfigOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_2 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_2 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC13: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC13)

      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentConfig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ JsonKey.result)
      (jsonCurrentConfigOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      
      CommonFunction.selectComponent(wC, componentIdC11)

      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentConfig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ JsonKey.result)
      (jsonCurrentConfigOut_4 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_4 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_4 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      
      
      CommonFunction.selectComponent(wC, componentIdC12)

      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentConfig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ JsonKey.result)
      (jsonCurrentConfigOut_5 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_5 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0

      CommonFunction.selectComponent(wC, componentIdC13)

      val jsonCurrentConfigOut_6: JsValue = CommonFunction.currentConfig(wC)
      
      val result_6 = (jsonCurrentConfigOut_6 \ JsonKey.result)
      (jsonCurrentConfigOut_6 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_6 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_6 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
      
      CommonFunction.selectComponent(wC, componentIdC11)

      val jsonCurrentConfigOut_7: JsValue = CommonFunction.currentConfig(wC)
      
      val result_7 = (jsonCurrentConfigOut_7 \ JsonKey.result)
      (jsonCurrentConfigOut_7 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_7 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_7 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_7 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
      
      CommonFunction.selectComponent(wC, componentIdC12)

      val jsonCurrentConfigOut_8: JsValue = CommonFunction.currentConfig(wC)
      
      val result_8 = (jsonCurrentConfigOut_8 \ JsonKey.result)
      (jsonCurrentConfigOut_8 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_8 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_8 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_8 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
      
      val componentOut_9: JsValue = CommonFunction.selectComponent(wC, componentIdC13)

      val rCout_9 = componentOut_9 \ JsonKey.result

      (componentOut_9 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (rCout_9 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 2
      (rCout_9 \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (rCout_9 \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (rCout_9 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireComponent().name
      (rCout_9 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (rCout_9 \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_9: JsValue = CommonFunction.currentConfig(wC)
      
      val result_9 = (jsonCurrentConfigOut_9 \ JsonKey.result)
      (jsonCurrentConfigOut_9 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_9 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_9 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0
    }
  }
}