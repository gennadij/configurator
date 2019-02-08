package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.RequireNextStep
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
@RunWith(classOf[JUnitRunner])
class Scenario_002_12_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2, C3, C1" >> {
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

      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentConfig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      
      CommonFunction.selectComponent(wC, componentIdC11)
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentConfig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_4 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_4 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      
      
      CommonFunction.selectComponent(wC, componentIdC12)
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentConfig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_5 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0

      CommonFunction.selectComponent(wC, componentIdC13)
      
      val jsonCurrentConfigOut_6: JsValue = CommonFunction.currentConfig(wC)
      
      val result_6 = (jsonCurrentConfigOut_6 \ "result")
      (jsonCurrentConfigOut_6 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_6 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_6 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
      
      val componentOut_7: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_7 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      val result = componentOut_3 \ JsonKey.result
      (result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 2
      (result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (result \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      (result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (result \ JsonKey.warning \ JsonKey.excludedComponentInternal \ JsonKey.name).asOpt[String] === Some(ExcludedComponentInternal().name)
      (result \ JsonKey.errors ).asOpt[JsObject] === None
      
      val jsonCurrentConfigOut_7: JsValue = CommonFunction.currentConfig(wC)
      
      val result_7 = (jsonCurrentConfigOut_7 \ "result")
      (jsonCurrentConfigOut_7 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_7 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_7 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_7 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
    }
  }
}