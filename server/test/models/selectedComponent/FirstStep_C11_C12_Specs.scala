package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.common.JsonNames
import org.shared.common.status.Success
import org.shared.component.status._
import org.shared.startConfig.json.JsonStartConfigOut
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */
@RunWith(classOf[JUnitRunner])
class FirstStep_C11_C12_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component_1_1 und Componente_1_2  ausgewaelt" >> {

      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )

      Logger.info("StartConfigIn " + startConfigIn)

      val startConfigOut: JsValue = wC.handleClientMessage(startConfigIn)

      Logger.info("StartConfigOut " + startConfigOut)

      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut)

      //User hat ausgewaelt
      val componentIdC11: String = sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
            .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC11 " + componentIdC11)

      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )

      val jsonComponentOut_1: JsValue = wC.handleClientMessage(jsonComponentIn_1)

      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)

      (jsonComponentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_1 \ "result" \ "selectedComponentId").asOpt[String].get.size must be_>=(30)
      (jsonComponentOut_1 \ "result" \ "stepId").asOpt[String].get.size must be_>=(30)
      (jsonComponentOut_1 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "visualization").asOpt[String].get === "undef"
      ((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0) \ "nameToShow").asOpt[String].get ===
        "(C11) ----> (C13)"

      val status = (jsonComponentOut_1 \ "result" \ "status")
      (status \ "selectionCriterium" \ "status").asOpt[String].get === AllowNextComponent().status
      (status \"selectedComponent" \ "status").asOpt[String].get === AddedComponent().status
      (status \"excludeDependency" \ "status").asOpt[String].get === NotExcludedComponent().status
      (status \"common" \ "status").asOpt[String].get === Success().status
      (status \"componentType" \ "status").asOpt[String].get === DefaultComponent().status


      Logger.info(this.getClass.getSimpleName + ": =================================================")

      //User hat ausgewaelt
      val componentIdC12: String =
        (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C12")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head


      Logger.info(this.getClass.getSimpleName + ": componentIdC12 " + componentIdC12)

      val jsonComponentIn_2: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC12
           )
      )

      val jsonComponentOut_2: JsValue = wC.handleClientMessage(jsonComponentIn_2)

      Logger.info(this.getClass.getSimpleName + ": ComponentIn_2 " + jsonComponentIn_2)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_2 " + jsonComponentOut_2)

      (jsonComponentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT

      (jsonComponentOut_2 \ "result" \ "selectedComponentId").asOpt[String].get.size must be_>=(30)
      (jsonComponentOut_2 \ "result" \ "stepId").asOpt[String].get.size must be_>=(30)

      (jsonComponentOut_2 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      ((jsonComponentOut_2 \ "result" \ "excludeDependenciesOut")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((jsonComponentOut_2 \ "result" \ "excludeDependenciesOut")(0) \ "visualization").asOpt[String].get === "undef"
      ((jsonComponentOut_2 \ "result" \ "excludeDependenciesOut")(0) \ "nameToShow").asOpt[String].get === "(C12) ----> (C13)"
      val status_2 = (jsonComponentOut_2 \ "result" \ "status")
      (status_2 \ "selectionCriterium" \ "status").asOpt[String].get === RequireNextStep().status
      (status_2 \"selectedComponent" \ "status").asOpt[String].get === AddedComponent().status
      (status_2 \"excludeDependency" \ "status").asOpt[String].get === NotExcludedComponent().status
      (status_2 \"common" \ "status").asOpt[String].get === Success().status
      (status_2 \"componentType" \ "status").asOpt[String].get === DefaultComponent().status
    }
  }
  
}