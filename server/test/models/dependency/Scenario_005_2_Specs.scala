package models.dependency

import controllers.MessageHandler
import controllers.websocket.WebClient
import models.bo.types.Auto
import org.junit.runner.RunWith
import org.shared.info.{RequireComponent, RequireNextStep}
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.warning.{ExcludeComponentExternal, ExcludedComponentExternal}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}
import util.CommonFunction

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2019
  */
@RunWith(classOf[JUnitRunner])
class Scenario_005_2_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init

  def beforeAll: Unit = {}

  def afterAll: Unit = {}

  "Specification for scenario 1 (Dependency) (visualization auto)" >> {
    "S1 - C11 -> S2 - C22 -> S3 - C33 -> S5 - C52" >> {

      val configUrl = "http://config/client_013"

      //Ausgewaelte Komponente

      val c11 = "C11"

      val c22 = "C22"

      val c33 = "C33"

      val c52 = "C52"

      //Steps

      val (s1, s2, s3, s5) = ("S1", "S2", "S3", "S5")

      val startConfigOut_S1 = CommonFunction.firstStep(wC, configUrl)

      val startConfig = Json.fromJson[JsonStepOut](startConfigOut_S1)

      val componentIdC11: String = startConfig.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC11)

      CommonFunction.currentConfig(wC)

      val jsonNextStepOut_S2 = CommonFunction.nextStep(wC)

      val componentId22: String =
        (jsonNextStepOut_S2 \ JsonKey.result \ JsonKey.componentsForSelection).asOpt[List[JsValue]].get
          .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c22)
          .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      val componentOut_22 = CommonFunction.selectComponent(wC, componentId22)

      val r_22 = componentOut_22 \ JsonKey.result

      (componentOut_22 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      (r_22 \ JsonKey.selectedComponentId).asOpt[String].get.length must be_>=(30)
      (r_22 \ JsonKey.stepId).asOpt[String].get.length must be_>=(30)

      (r_22 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 0
      (r_22 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 1
      ((r_22 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((r_22 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.visualization).asOpt[String].get === "undef"
      ((r_22 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C52) ---> (C22)"
      ((r_22 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.strategyOfDependencyResolver).asOpt[String].get === Auto.value


      (r_22 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireNextStep().name
      (r_22 \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (r_22 \ JsonKey.addedComponent ).asOpt[Boolean].get === true
//      (r_22 \ JsonKey.warning).asOpt[JsObject] === None
      (r_22 \ JsonKey.warning \ JsonKey.excludedComponentExternal \ JsonKey.name).asOpt[String] === Some(ExcludedComponentExternal().name)
      (r_22 \ JsonKey.errors ).asOpt[JsObject] === None

      val jsonNextStepComponents_S3 = CommonFunction.nextStep(wC) \ JsonKey.result \ JsonKey.componentsForSelection

      val componentId33: String = jsonNextStepComponents_S3.asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c33)
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      CommonFunction.selectComponent(wC, componentId33)

      val jsonNextStepComponents_S5 = CommonFunction.nextStep(wC) \ JsonKey.result \ JsonKey.componentsForSelection

      val componentId52: String = jsonNextStepComponents_S5.asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c52)
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      val componentOut52 = CommonFunction.selectComponent(wC, componentId52)

      (componentOut52 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

      val componentOutResult_52 = componentOut52 \ JsonKey.result

      (componentOutResult_52 \ JsonKey.selectedComponentId).asOpt[String].get.length must be_>=(30)
      (componentOutResult_52 \ JsonKey.stepId).asOpt[String].get.length must be_>=(30)

      (componentOutResult_52 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      (componentOutResult_52 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 0
      ((componentOutResult_52 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((componentOutResult_52 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.visualization).asOpt[String].get === "undef"
      ((componentOutResult_52 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C52) ---> (C22)"
      ((componentOutResult_52 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.strategyOfDependencyResolver).asOpt[String].get === Auto.value

      (componentOutResult_52 \ JsonKey.lastComponent ).asOpt[Boolean].get === true
      (componentOutResult_52 \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (componentOutResult_52 \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === RequireComponent().name
      (componentOutResult_52 \ JsonKey.warning \ JsonKey.excludeComponentExternal \ JsonKey.name).asOpt[String] === Some(ExcludeComponentExternal().name)
      (componentOutResult_52 \ JsonKey.errors ).asOpt[JsObject] === None

      val currentConfig = CommonFunction.currentConfig(wC)

      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + currentConfig)

      val result = currentConfig \ JsonKey.result
      (currentConfig \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some(s1)
      (result \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (result \ JsonKey.step \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((result \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c11)
      ((result \ JsonKey.step \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_1 = result \ JsonKey.step \ JsonKey.nextStep
      (nextStep_1 \ JsonKey.nameToShow).asOpt[String] === Some(s2)
      (nextStep_1 \ JsonKey.components).asOpt[List[JsValue]].get.size === 0
      (nextStep_1 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_2 = nextStep_1 \ JsonKey.nextStep
      (nextStep_2 \ JsonKey.nameToShow).asOpt[String] === Some(s3)
      (nextStep_2 \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (nextStep_2 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((nextStep_2 \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c33)
      ((nextStep_2 \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_3 = nextStep_2 \  JsonKey.nextStep
      (nextStep_3 \ JsonKey.nameToShow).asOpt[String] === Some(s5)
      (nextStep_3 \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (nextStep_3 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((nextStep_3 \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c52)
      ((nextStep_3 \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
    }
  }
}
