package models.selectedComponent

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.info.AllowNextComponent
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.warning.{ExcludeComponentInternal, ExcludedComponentInternal}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}


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
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params-> Json.obj(
               JsonKey.configUrl -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfigContainerBO)
      
      Logger.info("StartConfigIn " + startConfigIn)
      Logger.info("StartConfigOut " + startConfigOut)

      val sCOut = Json.fromJson[JsonStepOut](startConfigOut)

      //User hat ausgewaelt
      val componentIdC11: String = sCOut.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head
      
      Logger.info(this.getClass.getSimpleName + ": componentIdC11" + componentIdC11)

      val jsonComponentIn_1: JsValue = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC11
           )
      )
      
      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1, wC.currentConfigContainerBO)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 1
      (((jsonComponentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0)) \ JsonKey.dependencyType).asOpt[String].get === "exclude"
      (((jsonComponentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0)) \ JsonKey.visualization).asOpt[String].get === "undef"
      (((jsonComponentOut_1 \ JsonKey.result \ JsonKey.excludeDependenciesOut)(0)) \ JsonKey.nameToShow).asOpt[String].get === "(C11) ----> (C13)"
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === true
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.warning \ JsonKey.excludeComponentInternal \ JsonKey.name).asOpt[String] ===
        Some(ExcludeComponentInternal().name)
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.warning \ JsonKey.excludeComponentExternal).asOpt[String] === None
      (jsonComponentOut_1 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None

      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      //User hat ausgewaelt
      val componentIdC13: String = sCOut.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C13")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC13 " + componentIdC13)
      
      val jsonComponentIn_2: JsValue = Json.obj(
          JsonKey.json -> JsonNames.SELECTED_COMPONENT
          ,JsonKey.params-> Json.obj(
               JsonKey.componentId -> componentIdC13
           )
      )
      
      val jsonComponentOut_2: JsValue = wC.handleMessage(jsonComponentIn_2, wC.currentConfigContainerBO)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_2 " + jsonComponentIn_2)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_2 " + jsonComponentOut_2)
      
      (jsonComponentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.SELECTED_COMPONENT
      (jsonComponentOut_2 \ JsonKey.result \ JsonKey.excludeDependenciesOut).asOpt[List[JsValue]].get.size === 2
      (jsonComponentOut_2 \ JsonKey.result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (jsonComponentOut_2 \ JsonKey.result \ JsonKey.lastComponent ).asOpt[Boolean].get === false
      (jsonComponentOut_2 \ JsonKey.result \ JsonKey.addedComponent ).asOpt[Boolean].get === false
      val result = jsonComponentOut_2 \ JsonKey.result
      (result \ JsonKey.info \ JsonKey.selectionCriterion \ JsonKey.name).asOpt[String] === Some(AllowNextComponent().name)
      (result \ JsonKey.warning \ JsonKey.excludedComponentInternal \ JsonKey.name).asOpt[String] === Some(ExcludedComponentInternal().name)
      (result \ JsonKey.errors ).asOpt[JsObject] === None
    }
  }
}