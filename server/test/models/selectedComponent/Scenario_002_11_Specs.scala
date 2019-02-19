package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.RequireNextStep
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.warning.ExcludeComponentExternal
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
class Scenario_002_11_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2, C3" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head
      
      CommonFunction.selectComponent(wC, componentIdC11)
      
      val jsoncurrentConfigContainerBOOut_1: JsValue = CommonFunction.currentConfig(wC)
      
      val result_1 = (jsoncurrentConfigContainerBOOut_1 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC12: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C12")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC12)
      
      val jsoncurrentConfigContainerBOOut_2: JsValue = CommonFunction.currentConfig(wC)
      
      val result_2 = (jsoncurrentConfigContainerBOOut_2 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_2 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_2 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")

      val componentIdC13: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      val jsoncurrentConfigContainerBOOut_3: JsValue = CommonFunction.currentConfig(wC)
      
      val result_3 = (jsoncurrentConfigContainerBOOut_3 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_3 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_3 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      ((result_3 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_3 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      
      CommonFunction.selectComponent(wC, componentIdC11)
      
      val jsoncurrentConfigContainerBOOut_4: JsValue = CommonFunction.currentConfig(wC)
      
      val result_4 = (jsoncurrentConfigContainerBOOut_4 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_4 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_4 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_4 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")

      CommonFunction.selectComponent(wC, componentIdC12)
      
      val jsoncurrentConfigContainerBOOut_5: JsValue = CommonFunction.currentConfig(wC)
      
      val result_5 = (jsoncurrentConfigContainerBOOut_5 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_5 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_5 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 0

      val componentOut_6: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_6 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (componentOut_6 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (componentOut_6 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_6 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      //TODO ExcludeComponentInternal
      (componentOut_6 \ JsonKey.result \ JsonKey.warning \ JsonKey.excludeComponentExternal \ JsonKey.name).asOpt[String] ===
        Some(ExcludeComponentExternal().name)
//      (componentOut_6 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut_6 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsoncurrentConfigContainerBOOut_6: JsValue = CommonFunction.currentConfig(wC)
      
      val result_6 = (jsoncurrentConfigContainerBOOut_6 \ JsonKey.result)
      (jsoncurrentConfigContainerBOOut_6 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_6 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_6 \ JsonKey.step \ "components")(0) \ JsonKey.nameToShow).asOpt[String] === Some("C13")
    }
  }
}