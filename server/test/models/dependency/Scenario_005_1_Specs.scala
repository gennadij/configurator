package models.dependency

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.JsonNames
import org.shared.json.startConfig.JsonStartConfigOut
import org.shared.status.selectedComponent.{ExcludedComponentExternal, NotExcludedComponentInternal}
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

      val startConfigOut_S1 = CommonFunction.firstStep(wC, configUrl)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut_S1)

      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC11)

//      CommonFunction.currentCongig(wC)

      val jsonNextStepOut_S2 = CommonFunction.nextStep(wC)

      val componentId21: String = (jsonNextStepOut_S2 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C21")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      val componentOut_21 = CommonFunction.selectComponent(wC, componentId21)

      (componentOut_21 \ "result" \ "selectedComponentId").asOpt[String].get.length must be_>=(30)
      (componentOut_21 \ "result" \ "stepId").asOpt[String].get.length must be_>=(30)

      (componentOut_21 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      ((componentOut_21 \ "result" \ "excludeDependenciesOut")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((componentOut_21 \ "result" \ "excludeDependenciesOut")(0) \ "visualization").asOpt[String].get === "auto"
      ((componentOut_21 \ "result" \ "excludeDependenciesOut")(0) \ "nameToShow").asOpt[String].get === "(C21) ---> (C51)"

      (componentOut_21 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_21 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_21 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_21 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_21 \ "result" \ "status" \"excludeDependencyInternal" \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_21 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"

      val jsonNextStepOut_S3 = CommonFunction.nextStep(wC)

      val componentId33: String = (jsonNextStepOut_S3 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C33")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      CommonFunction.selectComponent(wC, componentId33)

      val jsonNextStepOut_S5 = CommonFunction.nextStep(wC)

      val componentId51: String = (jsonNextStepOut_S5 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C51")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      val componentOut4_21 = CommonFunction.selectComponent(wC, componentId51)

      (componentOut4_21 \ "result" \ "selectedComponentId").asOpt[String].get.length must be_>=(30)
      (componentOut4_21 \ "result" \ "stepId").asOpt[String].get.length must be_>=(30)

      (componentOut4_21 \ "result" \ "excludeDependenciesIn").asOpt[List[JsValue]].get.size === 1
      ((componentOut4_21 \ "result" \ "excludeDependenciesIn")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((componentOut4_21 \ "result" \ "excludeDependenciesIn")(0) \ "visualization").asOpt[String].get === "auto"
      ((componentOut4_21 \ "result" \ "excludeDependenciesIn")(0) \ "nameToShow").asOpt[String].get === "(C21) ---> (C51)"

      (componentOut4_21 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut4_21 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "FINAL_COMPONENT"
      (componentOut4_21 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut4_21 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_COMPONENT"
      (componentOut4_21 \ "result" \ "status" \"excludeDependencyInternal" \ "status").asOpt[String] ===
        Some(NotExcludedComponentInternal().status)
      (componentOut4_21 \ "result" \ "status" \"excludeDependencyExternal" \ "message").asOpt[String] ===
        Some(ExcludedComponentExternal(List("C21"), List()).message)
      (componentOut4_21 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"

      val currentConfig = CommonFunction.currentCongig(wC)

      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + currentConfig)

      val result = (currentConfig \ "result")
      (currentConfig \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result \ "step" \ "nameToShow").asOpt[String] === Some("S1")
      (result \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      (result \ "step" \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      ((result \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C11")
      ((result \ "step" \ "components")(0) \ "componentId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      val nextStep_1 = result \ "step" \ "nextStep"
      (nextStep_1 \ "nameToShow").asOpt[String] === Some("S2")
      (nextStep_1 \ "components").asOpt[List[JsValue]].get.size === 1
      (nextStep_1 \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      ((nextStep_1 \ "components")(0) \ "nameToShow").asOpt[String] === Some("C21")
      ((nextStep_1 \ "components")(0) \ "componentId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      val nextStep_2 = nextStep_1 \ "nextStep"
      (nextStep_2 \ "nameToShow").asOpt[String] === Some("S3")
      (nextStep_2 \ "components").asOpt[List[JsValue]].get.size === 1
      (nextStep_2 \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      ((nextStep_2 \ "components")(0) \ "nameToShow").asOpt[String] === Some("C33")
      ((nextStep_2 \ "components")(0) \ "componentId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      val nextStep_3 = nextStep_2 \ "nextStep"
      (nextStep_3 \ "nameToShow").asOpt[String] === Some("S5")
      (nextStep_3 \ "components").asOpt[List[JsValue]].get.size === 0
      (nextStep_3 \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)


      //TODO der Komponent C51 darf nicht in der CurrentConfig, da sie von der Komponent C21 ausgeschloßen war.
      // TODO Visualizierung ist auto automatisch aus der Curent Config entfernen.
    }
  }
}
