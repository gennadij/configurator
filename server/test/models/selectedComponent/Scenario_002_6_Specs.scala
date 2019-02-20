package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.warning.ExcludeComponentInternal
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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_6_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)

      //User hat ausgewaelt Schritt 1
      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      
      (componentOut_1 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut_1 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOut_1 \ JsonKey.result \ JsonKey.warning \ JsonKey.excludeComponentInternal \ JsonKey.name).asOpt[String] ===
        Some(ExcludeComponentInternal().name)
      (componentOut_1 \ JsonKey.result \ JsonKey.warning \ JsonKey.excludeComponentExternal).asOpt[String] === None
      (componentOut_1 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentConfig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step\ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step\ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step\ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
    }
  }
}