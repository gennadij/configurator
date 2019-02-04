package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.RequireNextStep
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
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class Scenario_002_15_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll: Unit = {}
  
  def afterAll: Unit = {}
  
  "Specification for scenario 15" >> {
    "S1 -> C1, C2, C3 | S2 -> C1" >> {
      val configUrl = "http://config/client_013"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head
      
      CommonFunction.selectComponent(wC, componentIdC11)
      
      val componentIdC12: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C12")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC12)

      val componentIdC13: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC13)
      
      val jsonNextStepOut = CommonFunction.nextStep(wC)

      (jsonNextStepOut \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      val nextStep = (jsonNextStepOut \ JsonKey.result \ JsonKey.step)
      (nextStep \ JsonKey.nameToShow).asOpt[String].get === "S2"
      val components = (jsonNextStepOut \ JsonKey.result \ JsonKey.componentsForSelection)
      (components).asOpt[Set[JsValue]].get.size === 2
      ((components (0)) \ JsonKey.nameToShow).asOpt[String].get === "C21"
      ((components (1)) \ JsonKey.nameToShow).asOpt[String].get === "C22"
      (jsonNextStepOut \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None
      (jsonNextStepOut \ JsonKey.result \ JsonKey.warnings).asOpt[JsObject] === None

      val jsonNextStep = Json.fromJson[JsonStepOut](jsonNextStepOut)

      val componentId21: String = jsonNextStep.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C21")
        .map(_.componentId).head
      
      val componentOut4_21 = CommonFunction.selectComponent(wC, componentId21)

      (componentOut4_21 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (componentOut4_21 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (componentOut4_21 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (componentOut4_21 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOut4_21 \ JsonKey.result \ JsonKey.warning).asOpt[JsObject] === None
      (componentOut4_21 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None
    }
  }
}