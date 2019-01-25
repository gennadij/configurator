package util

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.junit.runner.RunWith
import org.shared.json.{JsonKey, JsonNames}
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.Json
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.JsObject
import play.api.libs.json.JsLookupResult.jsLookupResultToJsLookup
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json.toJsFieldJsValueWrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 23.02.2018
 */
//noinspection ExistsEquals,ScalaUnnecessaryParentheses

@RunWith(classOf[JUnitRunner])
class ComponentKeys extends Specification with MessageHandler with BeforeAfterAll {
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

      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)

      //User hat ausgewaelt
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C11")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head

      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.SELECTED_COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )

      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1, wC.currentConfig)

      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)


      //noinspection ScalaUnnecessaryParentheses
      (jsonComponentOut_1).asOpt[JsObject].get.keys === Set("json", "result")

      (jsonComponentOut_1 \ "result").asOpt[JsObject].get.keys ===
        Set("selectedComponentId", "stepId", "status",
          "excludeDependenciesOut", "excludeDependenciesIn", "requireDependenciesOut", "requireDependenciesIn")

      //noinspection ExistsEquals
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == JsonKey.selectionCriterion) === true
      (jsonComponentOut_1 \ "result"\ "status" \ JsonKey.selectionCriterion).asOpt[JsObject].get.keys === Set("status", "message")

      (jsonComponentOut_1 \ "result" \ "status").asOpt[JsObject].get.keys.contains("componentType") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "componentType").asOpt[JsObject].get.keys === Set("status", "message")

      (jsonComponentOut_1 \ "result" \ "status").asOpt[JsObject].get.keys.contains("common") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "common").asOpt[JsObject].get.keys === Set("status", "message")

      //noinspection ExistsEquals
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "selectedComponent") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "selectedComponent").asOpt[JsObject].get.keys === Set("status", "message")

      (jsonComponentOut_1 \ "result" \ "status").asOpt[JsObject].get.keys.contains(JsonKey.excludeDependencyInternal) === true
      (jsonComponentOut_1 \ "result"\ "status" \ JsonKey.excludeDependencyInternal).asOpt[JsObject].get.keys === Set("status", "message")

      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.size === 6

      (jsonComponentOut_1 \ "result"\ "stepId").asOpt[JsObject] === None

      (jsonComponentOut_1 \ "result"\ "selectedComponentId").asOpt[JsObject] === None
    }
  }
}
