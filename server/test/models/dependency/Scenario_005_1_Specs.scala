package models.dependency

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.JsonNames
import org.shared.json.startConfig.JsonStartConfigOut
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.{JsValue, Json}
import util.CommonFunction

@RunWith(classOf[JUnitRunner])
class Scenario_005_1_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init

  def beforeAll: Unit = {}

  def afterAll: Unit = {}

  "Specification for scenario 1 (Dependency)" >> {
    "S1 - C11 -> S2 - C21 -> S3 - C33 -> S5 - C51" >> {

      val configUrl = "http://config/client_013"

      val startConfigOut = CommonFunction.firstStep(wC, configUrl)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut)

      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      CommonFunction.selectComponent(wC, componentIdC11)

      CommonFunction.currentCongig(wC)

      val jsonNextStepOut_1 = CommonFunction.nextStep(wC)

      val componentId21: String = (jsonNextStepOut_1 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C21")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      CommonFunction.selectComponent(wC, componentId21)

      val jsonNextStepOut_2 = CommonFunction.nextStep(wC)

      val componentId33: String = (jsonNextStepOut_2 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C33")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      CommonFunction.selectComponent(wC, componentId33)

      val jsonNextStepOut_3 = CommonFunction.nextStep(wC)

      val componentId51: String = (jsonNextStepOut_3 \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
        .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C51")
        .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      val componentOut4_21 = CommonFunction.selectComponent(wC, componentId51)




      "" === ""
    }
  }

}
