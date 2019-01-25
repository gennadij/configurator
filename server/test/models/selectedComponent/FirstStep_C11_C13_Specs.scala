package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.shared.status.common.Success
import org.shared.status.selectedComponent._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsValue, Json}


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 04.12.2017
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses

@RunWith(classOf[JUnitRunner])
class FirstStep_C11_C13_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component_1_1 und Componente_1_3  ausgewaelt" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)

//      val sCOut = Json.fromJson[JsonStartConfigOut](startConfigOut)
//
//      //User hat ausgewaelt
      val componentIdC11: String = ???
//sCOut.get.result.step.components.filter(comp => comp.nameToShow == "C11")
//        .map(_.componentId).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11" + componentIdC11)

      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.SELECTED_COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      
      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (jsonComponentOut_1 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0)) \ "visualization").asOpt[String].get === "undef"
      (((jsonComponentOut_1 \ "result" \ "excludeDependenciesOut")(0)) \ "nameToShow").asOpt[String].get === "(C11) ----> (C13)"
      val status_1 = (jsonComponentOut_1 \ "result" \ "status")
      (status_1 \JsonKey.selectionCriterion \ "status").asOpt[String].get === AllowNextComponent().status
      (status_1 \"selectedComponent" \ "status").asOpt[String].get === AddedComponent().status
      (status_1 \JsonKey.excludeDependencyInternal \ "status").asOpt[String].get === NotExcludedComponentInternal().status
      (status_1 \"common" \ "status").asOpt[String].get === Success().status
      (status_1 \"componentType" \ "status").asOpt[String].get === DefaultComponent().status

      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val sCOut_2 = ???
//        Json.fromJson[JsonStartConfigOut](startConfigOut)

      //User hat ausgewaelt
      val componentIdC13: String = ???
//        sCOut_2.get.result.step.components.filter(comp => comp.nameToShow == "C13")
//        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC13 " + componentIdC13)
      
      val jsonComponentIn_2: JsValue = Json.obj(
          "json" -> JsonNames.SELECTED_COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC13
           )
      )
      
      val jsonComponentOut_2: JsValue = wC.handleMessage(jsonComponentIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_2 " + jsonComponentIn_2)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_2 " + jsonComponentOut_2)
      
      (jsonComponentOut_2 \ "json").asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (jsonComponentOut_2 \ "result" \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 2
      val status_2 = (jsonComponentOut_2 \ "result" \ "status")
      (status_2 \JsonKey.selectionCriterion \ "status").asOpt[String].get === AllowNextComponent().status
      (status_2 \"selectedComponent" \ "status").asOpt[String].get === NotAllowedComponent().status
      (status_2 \JsonKey.excludeDependencyInternal \ "status").asOpt[String].get === ExcludedComponentInternal().status
      (status_2 \"common" \ "status").asOpt[String].get === Success().status
      (status_2 \"componentType" \ "status").asOpt[String].get === DefaultComponent().status
    }
  }
}