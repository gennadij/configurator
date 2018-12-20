package models.dependency

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.shared.json.startConfig.JsonStartConfigOut
import org.shared.status.common.Success
import org.shared.status.selectedComponent._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import util.CommonFunction

@RunWith(classOf[JUnitRunner])
class Scenario_005_1_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init

  def beforeAll: Unit = {}

  def afterAll: Unit = {}

  "Specification for scenario 1 (Dependency) (visualization auto)" >> {
    "S1 - C11 -> S2 - C21 -> S3 - C33 -> S5 - C51" >> {

      val configUrl = "http://config/client_013"

      //Ausgewaelte Komponente

      val c11 = "C11"

      val c21 = "C21"

      val c33 = "C33"

      val c51 = "C51"

      //Steps

      val (s1, s2, s3, s5) = ("S1", "S2", "S3", "S5")

      val startConfigOut_S1 = CommonFunction.firstStep(wC, configUrl)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut_S1)

      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == c11)
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC11)

//      CommonFunction.currentCongig(wC)

      val jsonNextStepOut_S2 = CommonFunction.nextStep(wC)

      val componentId21: String =
        (jsonNextStepOut_S2 \ JsonKey.result \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c21)
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      val componentOut_21 = CommonFunction.selectComponent(wC, componentId21)

      val r_3 = componentOut_21 \ JsonKey.result

      (r_3 \ JsonKey.selectedComponentId).asOpt[String].get.length must be_>=(30)
      (r_3 \ JsonKey.stepId).asOpt[String].get.length must be_>=(30)

      (r_3 \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      ((r_3 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((r_3 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.visualization).asOpt[String].get === "auto"
      ((r_3 \ JsonKey.excludeDependenciesOut)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C21) ---> (C51)"

      val r_1 = componentOut_21 \ JsonKey.result \ JsonKey.status

      (componentOut_21 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (r_1 \ JsonKey.componentType \ JsonKey.status).asOpt[String] === Some(DefaultComponent().status)
      (r_1 \ JsonKey.selectedComponent \ JsonKey.status).asOpt[String] === Some(AddedComponent().status)
      (r_1 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String] === Some(RequireNextStep().status)
      (r_1 \ JsonKey.excludeDependencyInternal \ JsonKey.status).asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_1 \ JsonKey.common \ JsonKey.status).asOpt[String] === Some(Success().status)

      val jsonNextStepComponents_S3 = CommonFunction.nextStep(wC) \ JsonKey.result \ JsonKey.step \ JsonKey.components

      val componentId33: String = jsonNextStepComponents_S3.asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c33)
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      CommonFunction.selectComponent(wC, componentId33)

      val jsonNextStepComponents_S5 = CommonFunction.nextStep(wC) \ JsonKey.result \ JsonKey.step \ JsonKey.components

      val componentId51: String = jsonNextStepComponents_S5.asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == c51)
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      val componentOut4_21 = CommonFunction.selectComponent(wC, componentId51)

      val componentOutResult4_21 = componentOut4_21 \ JsonKey.result

      (componentOutResult4_21 \ JsonKey.selectedComponentId).asOpt[String].get.length must be_>=(30)
      (componentOutResult4_21 \ JsonKey.stepId).asOpt[String].get.length must be_>=(30)

      (componentOutResult4_21 \ JsonKey.excludeDependenciesIn).asOpt[List[JsValue]].get.size === 1
      ((componentOutResult4_21 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      ((componentOutResult4_21 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.visualization).asOpt[String].get === "auto"
      ((componentOutResult4_21 \ JsonKey.excludeDependenciesIn)(0) \ JsonKey.nameToShow).asOpt[String].get === "(C21) ---> (C51)"

      val r_2 = componentOutResult4_21 \ JsonKey.status
      (componentOut4_21 \ JsonKey.json).asOpt[String].get === JsonNames.COMPONENT
      (r_2 \ JsonKey.componentType \ JsonKey.status).asOpt[String] === Some(FinalComponent().status)
      (r_2 \ JsonKey.selectedComponent \ JsonKey.status).asOpt[String] === Some(AddedComponent().status)
      (r_2 \ JsonKey.selectionCriterion \ JsonKey.status).asOpt[String] === Some(RequireComponent().status)
      (r_2 \ JsonKey.excludeDependencyInternal \ JsonKey.status).asOpt[String] === Some(NotExcludedComponentInternal().status)
      (r_2 \ JsonKey.excludeDependencyExternal \ JsonKey.message).asOpt[String] ===
        Some(ExcludedComponentExternal(List("C21"), List()).message)
      (r_2 \ JsonKey.common \ JsonKey.status).asOpt[String] === Some(Success().status)

      val currentConfig = CommonFunction.currentCongig(wC)

      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + currentConfig)

      val result = currentConfig \ "result"
      (currentConfig \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some(s1)
      (result \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (result \ JsonKey.step \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((result \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c11)
      ((result \ JsonKey.step \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_1 = result \ JsonKey.step \ JsonKey.nextStep
      (nextStep_1 \ JsonKey.nameToShow).asOpt[String] === Some(s2)
      (nextStep_1 \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (nextStep_1 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((nextStep_1 \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c21)
      ((nextStep_1 \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_2 = nextStep_1 \ JsonKey.nextStep
      (nextStep_2 \ JsonKey.nameToShow).asOpt[String] === Some(s3)
      (nextStep_2 \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      (nextStep_2 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      ((nextStep_2 \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some(c33)
      ((nextStep_2 \ JsonKey.components) (0) \ JsonKey.componentId).asOpt[String].get.length must be_<=(32) and be_>=(30)
      val nextStep_3 = nextStep_2 \  JsonKey.nextStep
      (nextStep_3 \ JsonKey.nameToShow).asOpt[String] === Some(s5)
      (nextStep_3 \ JsonKey.components).asOpt[List[JsValue]].get.size === 0
      (nextStep_3 \ JsonKey.stepId).asOpt[String].get.length must be_<=(32) and be_>=(30)


      //TODO der Komponent C51 darf nicht in der CurrentConfig, da sie von der Komponent C21 ausgeschloßen war.
      // TODO Visualizierung ist auto automatisch aus der Curent Config entfernen.
    }
  }
}
