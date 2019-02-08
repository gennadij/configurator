package models.currentConfig

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

@RunWith(classOf[JUnitRunner])
class ConvertRidToHashInCurrentConfig extends Specification with MessageHandler with BeforeAfterAll {

  val wC: WebClient = WebClient.init

  def beforeAll(): Unit = {
  }

  def afterAll(): Unit = {
  }

  "Specification spezifiziert die kovertierung der Rid in Hash und umgekehrt" >> {
    "Die Komponente C11, C12 werden ausgewaelt" >> {

      val configUrl = "http://config/client_013"

      val startConfigIn = Json.obj(
        JsonKey.json -> JsonNames.STEP
        ,JsonKey.params-> Json.obj(
          JsonKey.params -> configUrl
        )
      )

      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfigContainerBO)

      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)

      //User hat ausgewaelt Component 1
      val componentIdC11: String = (startConfigOut \ JsonKey.result \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == "C11")
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)

      val componentIn_1 = Json.obj(
        JsonKey.json -> JsonNames.SELECTED_COMPONENT
        ,JsonKey.params-> Json.obj(
          JsonKey.componentId -> componentIdC11
        )
      )
      Logger.info("componentIn_1 " + componentIn_1)

      val componentOut_1: JsValue = wC.handleMessage(componentIn_1, wC.currentConfigContainerBO)
      Logger.info("componentOut_1 " + componentOut_1)

      (componentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

//      val statusSelectionCriterium = AllowNextComponent()
//      (componentOut_1 \ JsonKey.result \ "status" \ JsonKey.selectionCriterion \ "status").asOpt[String].get === statusSelectionCriterium.status
//      (componentOut_1 \ JsonKey.result \ "status" \ JsonKey.selectionCriterion \ "message").asOpt[String].get === statusSelectionCriterium.message
//
//      val statusSelectedComponent = AddedComponent()
//
//      (componentOut_1 \ JsonKey.result \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent.status
//      (componentOut_1 \ JsonKey.result \ "status" \ "selectedComponent" \ "message").asOpt[String].get === statusSelectedComponent.message
//
//      val statusExcludeDependency = NotExcludedComponentInternal()
//
//      (componentOut_1 \ JsonKey.result \ "status" \ JsonKey.excludeDependencyInternal \ "status").asOpt[String].get === statusExcludeDependency.status
//      (componentOut_1 \ JsonKey.result \ "status" \ JsonKey.excludeDependencyInternal \ "message").asOpt[String].get === statusExcludeDependency.message
//
//      val statusCommon = Success()
//
//      (componentOut_1 \ JsonKey.result \ "status" \ "common" \ "status").asOpt[String].get === statusCommon.status
//      (componentOut_1 \ JsonKey.result \ "status" \ "common" \ "message").asOpt[String].get === statusCommon.message

      val jsonCurrentConfigIn_1 : JsValue = Json.obj(
        JsonKey.json -> JsonNames.CURRENT_CONFIG
      )

      val jsonCurrentConfigOut_1: JsValue = wC.handleMessage(jsonCurrentConfigIn_1, wC.currentConfigContainerBO)

      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_1)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_1)

      val result_1 = (jsonCurrentConfigOut_1 \ JsonKey.result)
      (jsonCurrentConfigOut_1 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_1 \ JsonKey.step \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      (result_1 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 1
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      ((result_1 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.componentId).asOpt[String].get.size must be_<=(32) and be_>=(30)

      Logger.info(this.getClass.getSimpleName + ": =================================================")

      //User hat ausgewaelt Component 2
      val componentIdC12: String = (startConfigOut \ JsonKey.result \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get
        .filter(comp => (comp \ JsonKey.nameToShow).asOpt[String].get == "C12")
        .map(comp => {(comp \ JsonKey.componentId).asOpt[String].get}).head

      val componentIn_2 = Json.obj(
        JsonKey.json -> JsonNames.SELECTED_COMPONENT
        ,JsonKey.params-> Json.obj(
          JsonKey.componentId -> componentIdC12
        )
      )
      Logger.info("componentIn_2 " + componentIn_2)

      val componentOut_2: JsValue = wC.handleMessage(componentIn_2, wC.currentConfigContainerBO)

      Logger.info("componentOut_2 " + componentOut_2)

      (componentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT

//      val statusSelectionCriterium_2 = RequireNextStep()
//      (componentOut_2 \ JsonKey.result \ "status" \ JsonKey.selectionCriterion \ "status").asOpt[String].get === statusSelectionCriterium_2.status
//      (componentOut_2 \ JsonKey.result \ "status" \ JsonKey.selectionCriterion \ "message").asOpt[String].get === statusSelectionCriterium_2.message
//
//      val statusSelectedComponent_2 = AddedComponent()
//
//      (componentOut_2 \ JsonKey.result \ "status" \ "selectedComponent" \ "status").asOpt[String].get === statusSelectedComponent_2.status
//      (componentOut_2 \ JsonKey.result \ "status" \ "selectedComponent" \ "message").asOpt[String].get === statusSelectedComponent_2.message
//
//      val statusExcludeDependency_2 = NotExcludedComponentInternal()
//
//      (componentOut_2 \ JsonKey.result \ "status" \ JsonKey.excludeDependencyInternal \ "status").asOpt[String].get === statusExcludeDependency_2.status
//      (componentOut_2 \ JsonKey.result \ "status" \ JsonKey.excludeDependencyInternal \ "message").asOpt[String].get === statusExcludeDependency_2.message
//
//      val statusCommon_2 = Success()

//      (componentOut_2 \ JsonKey.result \ "status" \ "common" \ "status").asOpt[String].get === statusCommon_2.status
//      (componentOut_2 \ JsonKey.result \ "status" \ "common" \ "message").asOpt[String].get === statusCommon_2.message


      val jsonCurrentConfigIn_2 : JsValue = Json.obj(
        JsonKey.json -> JsonNames.CURRENT_CONFIG
      )

      val jsonCurrentConfigOut_2: JsValue = wC.handleMessage(jsonCurrentConfigIn_2, wC.currentConfigContainerBO)

      Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn_2)
      Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut_2)

      val result_2 = (jsonCurrentConfigOut_2 \ JsonKey.result)
      (jsonCurrentConfigOut_2 \ JsonKey.json).asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ JsonKey.step \ JsonKey.nameToShow).asOpt[String] === Some("S1")
      (result_2 \ JsonKey.step \ JsonKey.components).asOpt[List[JsValue]].get.size === 2
      (result_2 \ JsonKey.step \ "stepId").asOpt[String].get.size must be_<=(32) and be_>=(30)
      ((result_2 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.nameToShow).asOpt[String] === Some("C12")
      ((result_2 \ JsonKey.step \ JsonKey.components)(0) \ JsonKey.componentId).asOpt[String].get.size must be_<=(32) and be_>=(30)
      ((result_2 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.nameToShow).asOpt[String] === Some("C11")
      ((result_2 \ JsonKey.step \ JsonKey.components)(1) \ JsonKey.componentId).asOpt[String].get.size must be_<=(32) and be_>=(30)
    }
  }
}
