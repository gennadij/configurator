package models.v002

import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import models.config.ConfigWeb
import org.specs2.specification.BeforeAfterAll
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.Logger
import play.api.libs.json.JsValue
import models.persistence.orientdb.PropertyKeys
import util.CommonFunction

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
@RunWith(classOf[JUnitRunner])
class Scenario_002_6_Specs extends Specification with ConfigWeb with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll = {}
  
  def afterAll = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1" >> {
      val configUrl = "http://contig1/user29_v016"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      //User hat ausgewaelt Schritt 1
      val componentIdC11: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_1_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_1: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_1 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_1 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_1 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      (componentOut_1 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_1 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_1: JsValue = CommonFunction.currentCongig(wC)
      
      val result_1 = (jsonCurrentConfigOut_1 \ "result")
      (jsonCurrentConfigOut_1 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_1 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_1 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_1 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_1_user29_v016")
    }
  }
}