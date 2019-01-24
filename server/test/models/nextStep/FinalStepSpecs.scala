package models.nextStep

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.step.JsonStepOut
import org.shared.json.{JsonKey, JsonNames}
import org.shared.status.selectedComponent.NotExcludedComponentInternal
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfterAll
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}
import util.CommonFunction


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 10.11.2017
 */
//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
@RunWith(classOf[JUnitRunner])
class FinalStepSpecs extends Specification with MessageHandler with BeforeAfterAll{
  
  val wC: WebClient = WebClient.init
  
  def beforeAll(): Unit = {
  }
  
  def afterAll(): Unit = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component 1 ausgewaelt" >> {
      val configUrl = "http://config/client_013"
      val startConfigIn = Json.obj(
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params -> Json.obj(
               JsonKey.configUrl -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      
      Logger.info("IN " + startConfigIn)
      Logger.info("OUT" + startConfigOut)

      val sCOut = Json.fromJson[JsonStepOut](startConfigOut)

//      //User hat ausgewaelt
//
      val componentIdC11: String = sCOut.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C11")
        .map(_.componentId).head

      val componentIn_1 = Json.obj(
          JsonKey.json -> JsonNames.COMPONENT
          ,JsonKey.params -> Json.obj(
               JsonKey.componentId -> componentIdC11
           )
      )

      val jsonComponentOut_1: JsValue = wC.handleMessage(componentIn_1, wC.currentConfig)
      
      Logger.info("OUT " + jsonComponentOut_1)
      
      (jsonComponentOut_1 \ JsonKey.json).asOpt[String].get === JsonNames.COMPONENT
      (jsonComponentOut_1 \ JsonKey.result \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      ((jsonComponentOut_1 \ JsonKey.result \ "excludeDependenciesOut")(0) \ "dependencyType").asOpt[String].get === "exclude"
      ((jsonComponentOut_1 \ JsonKey.result \ "excludeDependenciesOut")(0) \ "visualization").asOpt[String].get === "undef"
      ((jsonComponentOut_1 \ JsonKey.result \ "excludeDependenciesOut")(0) \ JsonKey.nameToShow).asOpt[String].get === "(C11) ----> (C13)"
      (jsonComponentOut_1 \ JsonKey.result \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (jsonComponentOut_1 \ JsonKey.result \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (jsonComponentOut_1 \ JsonKey.result \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (jsonComponentOut_1 \ JsonKey.result \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (jsonComponentOut_1 \ JsonKey.result \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"

      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      Logger.info(this.getClass.getSimpleName + ": currentConfig " + CommonFunction.currentCongig(wC))
      
      val jsonNextStepIn_2 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_2 = wC.handleMessage(jsonNextStepIn_2, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_2 " + jsonNextStepIn_2)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_2 " + jsonNextStepOut_2)

      (jsonNextStepOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      val nextStep_2 = (jsonNextStepOut_2 \ JsonKey.result \ JsonKey.step)
      (nextStep_2 \ JsonKey.nameToShow).asOpt[String].get === "S2"
      val components_2 = (jsonNextStepOut_2 \ JsonKey.result \ JsonKey.componentsForSelection)
      (components_2).asOpt[Set[JsValue]].get.size === 2
      ((components_2 (0)) \ JsonKey.nameToShow).asOpt[String].get === "C21"
      ((components_2 (1)) \ JsonKey.nameToShow).asOpt[String].get === "C22"
      (jsonNextStepOut_2 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None
      (jsonNextStepOut_2 \ JsonKey.result \ JsonKey.warnings).asOpt[JsObject] === None

      Logger.info(this.getClass.getSimpleName + ": =================================================")


      val jNSOut = Json.fromJson[JsonStepOut](jsonNextStepOut_2)

      //User hat ausgewaelt Schritt 2
      val componentIdC21: String = jNSOut.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C21")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC21 " + componentIdC21)
      
      val componentIn_2 = Json.obj(
          JsonKey.json -> JsonNames.COMPONENT
          ,JsonKey.params -> Json.obj(
               JsonKey.componentId -> componentIdC21
           )
      )
      Logger.info("componentIn_2 " + componentIn_2)
      
      val componentOut_2: JsValue = wC.handleMessage(componentIn_2, wC.currentConfig)
      
      Logger.info("componentOut_2 " + componentOut_2)
      
      (componentOut_2 \ JsonKey.json).asOpt[String].get === JsonNames.COMPONENT
      (componentOut_2 \ JsonKey.result \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 1
      (componentOut_2 \ JsonKey.result \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ JsonKey.result \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ JsonKey.result \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_2 \ JsonKey.result \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_2 \ JsonKey.result \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"

      
      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_3 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_3 = wC.handleMessage(jsonNextStepIn_3, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_3 " + jsonNextStepIn_3)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_3 " + jsonNextStepOut_3)


      (jsonNextStepOut_3 \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      val nextStep_3 = (jsonNextStepOut_3 \ JsonKey.result \ JsonKey.step)
      (nextStep_3 \ JsonKey.nameToShow).asOpt[String].get === "S3"
      val components_3 = (jsonNextStepOut_3 \ JsonKey.result \ JsonKey.componentsForSelection)
      (components_3).asOpt[Set[JsValue]].get.size === 4
      ((components_3 (0)) \ JsonKey.nameToShow).asOpt[String].get === "C32"
      ((components_3 (1)) \ JsonKey.nameToShow).asOpt[String].get === "C34"
      ((components_3 (2)) \ JsonKey.nameToShow).asOpt[String].get === "C33"
      ((components_3 (3)) \ JsonKey.nameToShow).asOpt[String].get === "C31"
      (jsonNextStepOut_3 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None
      (jsonNextStepOut_3 \ JsonKey.result \ JsonKey.warnings).asOpt[JsObject] === None
      
      Logger.info(this.getClass.getSimpleName + ": =================================================")

      val jNSOut_3 = Json.fromJson[JsonStepOut](jsonNextStepOut_3)

      //User hat ausgewaelt Schritt 2
      val componentIdC31: String = jNSOut_3.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C31")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC31 " + componentIdC31)
      
      val componentIn_3 = Json.obj(
          JsonKey.json -> JsonNames.COMPONENT
          ,JsonKey.params -> Json.obj(
               JsonKey.componentId -> componentIdC31
           )
      )
      Logger.info("componentIn_3 " + componentIn_3)
      
      val componentOut_3: JsValue = wC.handleMessage(componentIn_3, wC.currentConfig)
      
      Logger.info("componentOut_3 " + componentOut_3)
      
      (componentOut_3 \ JsonKey.json).asOpt[String].get === JsonNames.COMPONENT
      (componentOut_3 \ JsonKey.result \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 0
      (componentOut_3 \ JsonKey.result \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ JsonKey.result \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_3 \ JsonKey.result \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
      (componentOut_3 \ JsonKey.result \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      (componentOut_3 \ JsonKey.result \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"

      Logger.info(this.getClass.getSimpleName + ": =================================================")
      
      val jsonNextStepIn_4 : JsValue = Json.obj(
          JsonKey.json -> JsonNames.STEP
      )
      
      val jsonNextStepOut_4 = wC.handleMessage(jsonNextStepIn_4, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": nextStepIn_4 " + jsonNextStepIn_4)
      Logger.info(this.getClass.getSimpleName + ": nextStepOut_4 " + jsonNextStepOut_4)
      
      (jsonNextStepOut_4 \ JsonKey.json).asOpt[String].get === JsonNames.STEP


      (jsonNextStepOut_4 \ JsonKey.json).asOpt[String].get === JsonNames.STEP
      val nextStep_4 = (jsonNextStepOut_4 \ JsonKey.result \ JsonKey.step)
      (nextStep_4 \ JsonKey.nameToShow).asOpt[String].get === "S4"
      val components_4 = (jsonNextStepOut_4 \ JsonKey.result \ JsonKey.componentsForSelection)
      (components_4).asOpt[Set[JsValue]].get.size === 2
      ((components_4 (0)) \ JsonKey.nameToShow).asOpt[String].get === "C41"
      ((components_4 (1)) \ JsonKey.nameToShow).asOpt[String].get === "c42"
      (jsonNextStepOut_4 \ JsonKey.result \ JsonKey.errors ).asOpt[JsObject] === None
      (jsonNextStepOut_4 \ JsonKey.result \ JsonKey.warnings).asOpt[JsObject] === None

      Logger.info(this.getClass.getSimpleName + ": =================================================")

      val jNSOut_4 = Json.fromJson[JsonStepOut](jsonNextStepOut_4)

      //User hat ausgewaelt Schritt 4
      val componentIdC41: String = jNSOut_4.get.result.componentsForSelection.get.filter(comp => comp.nameToShow == "C41")
        .map(_.componentId).head

      Logger.info(this.getClass.getSimpleName + ": componentIdC41 " + componentIdC41)

      val componentIn_4 = Json.obj(
        JsonKey.json -> JsonNames.COMPONENT
        ,JsonKey.params -> Json.obj(
          JsonKey.componentId -> componentIdC31
        )
      )
      Logger.info("componentIn_4 " + componentIn_4)

      val componentOut_4: JsValue = wC.handleMessage(componentIn_4, wC.currentConfig)

      Logger.info("componentOut_4 " + componentOut_4)
      "" === ""
//      (componentOut_4 \ JsonKey.json).asOpt[String].get === JsonNames.COMPONENT
//      (componentOut_4 \ JsonKey.result \ "excludeDependenciesOut").asOpt[List[JsValue]].get.size === 0
//      (componentOut_4 \ JsonKey.result \ "status" \JsonKey.selectionCriterion \ "status").asOpt[String].get === "REQUIRE_COMPONENT"
//      (componentOut_4 \ JsonKey.result \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
//      (componentOut_4 \ JsonKey.result \ "status" \JsonKey.excludeDependencyInternal \ "status").asOpt[String] === Some(NotExcludedComponentInternal().status)
//      (componentOut_4 \ JsonKey.result \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
//      (componentOut_4 \ JsonKey.result \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      // TODO FIXMI erstelle Ablauf bis zu letzte Komponente
    }
  }
}