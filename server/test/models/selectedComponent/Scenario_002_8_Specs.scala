package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.{AllowNextComponent, RequireNextStep}
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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_8_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (componentOut_1 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === AllowNextComponent().name
      (componentOut_1 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_1 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOut_1 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut_1 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentCongig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC12: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C12")
        .map(_.componentId).head

      val componentOut_2: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (componentOut_2 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (componentOut_2 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_2 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOut_2 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut_2 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentCongig(wC)
      
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
      val result = componentOut_3 \ JsonKey.result
      (result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 2
      (result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (result \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (result \ JsonKey.warning \ JsonKey.excludedComponentInternal \ JsonKey.name).asOpt[String] === Some(ExcludedComponentInternal().name)
      (result \ JsonKey.errors ).asOpt[JsObject] === None
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
    }
  }
}