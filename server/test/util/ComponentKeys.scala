package util

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import models.genericConfig.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import models.json.JsonNames
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

@RunWith(classOf[JUnitRunner])
class ComponentKeys extends Specification with ConfigWeb with BeforeAfterAll {
  val wC = WebClient.init
  
  def beforeAll() = {
  }
  
  def afterAll() = {
  }
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "Es wird erster Step mit der Komponenten geladen und Component_1_1 und Componente_1_2  ausgewaelt" >> {
      val configUrl = "http://contig1/user29_v016"
      val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      
      //User hat ausgewaelt
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      val componentIds: List[String] = List(componentIdC11)
      
      val jsonComponentIn_1: JsValue = Json.obj(
          "json" -> JsonNames.COMPONENT
          ,"params" -> Json.obj(
               "componentId" -> componentIdC11
           )
      )
      
      val jsonComponentOut_1: JsValue = wC.handleMessage(jsonComponentIn_1)
      
      Logger.info(this.getClass.getSimpleName + ": ComponentIn_1 " + jsonComponentIn_1)
      Logger.info(this.getClass.getSimpleName + ": ComponentOut_1 " + jsonComponentOut_1)
      
      
      (jsonComponentOut_1).asOpt[JsObject].get.keys === Set("json", "result")
      
      (jsonComponentOut_1 \ "result").asOpt[JsObject].get.keys === 
        Set("selectedComponentId", "stepId", "status", "dependencies")
      
      (jsonComponentOut_1 \ "result"\ "dependencies").asOpt[List[JsObject]].get.map(_.keys) === 
        List(Set("outId", "inId", "dependencyType", "visualization", "nameToShow", "status", "message"))
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "selectionCriterium") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "selectionCriterium").asOpt[JsObject].get.keys === Set("status", "message")
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "componentType") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "componentType").asOpt[JsObject].get.keys === Set("status", "message")
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "common") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "common").asOpt[JsObject].get.keys === Set("status", "message")
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "selectedComponent") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "selectedComponent").asOpt[JsObject].get.keys === Set("status", "message")
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.exists(_ == "excludeDependency") === true
      (jsonComponentOut_1 \ "result"\ "status" \ "excludeDependency").asOpt[JsObject].get.keys === Set("status", "message")
      
      (jsonComponentOut_1 \ "result"\ "status").asOpt[JsObject].get.keys.size === 5
      
      (jsonComponentOut_1 \ "result"\ "stepId").asOpt[JsObject] === None
      
      (jsonComponentOut_1 \ "result"\ "selectedComponentId").asOpt[JsObject] === None
    }
  }
}
