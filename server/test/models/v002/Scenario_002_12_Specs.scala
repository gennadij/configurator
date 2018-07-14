package models.v002

import controllers.MessageHandler
import controllers.websocket.WebClient
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.Json
import play.api.Logger
import play.api.libs.json.JsValue
import models.persistence.orientdb.PropertyKeys
import org.shared.common.JsonNames
import util.CommonFunction

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
@RunWith(classOf[JUnitRunner])
class Scenario_002_12_Specs extends Specification with MessageHandler with BeforeAfterAll{

  val wC = WebClient.init
  
  def beforeAll = {}
  
  def afterAll = {}
  
  "Specification spezifiziert der NextStep der Konfiguration" >> {
    "S1 -> C1, C2, C3, C1, C2, C3, C1" >> {
      val configUrl = "http://contig1/user29_v016"
      
      val startConfigOut = CommonFunction.firstStep(wC, configUrl)
      
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
      
      val componentIdC12: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_2_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_2: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_2 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_2 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_2 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_2_user29_v016) ----> (C_1_3_user29_v016)"
      (componentOut_2 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_2 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_2 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_2: JsValue = CommonFunction.currentCongig(wC)
      
      val result_2 = (jsonCurrentConfigOut_2 \ "result")
      (jsonCurrentConfigOut_2 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_2 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_2 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_2 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_2_user29_v016")
      ((result_2 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C_1_1_user29_v016")
      
      val componentIdC13: String = (startConfigOut \ "result" \ "step" \ "components").asOpt[List[JsValue]].get
            .filter(comp => (comp \ "nameToShow").asOpt[String].get == "C_1_3_user29_v016")
            .map(comp => {(comp \ "componentId").asOpt[String].get}).head
      
      
      val componentOut_3: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_3 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_3 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 2
      (((componentOut_3 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_3 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_3 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_3_user29_v016) ----> (C_1_1_user29_v016)"
      (((componentOut_3 \ "result" \ "dependencies")(1)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_3 \ "result" \ "dependencies")(1)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_3 \ "result" \ "dependencies")(1)) \ "nameToShow").asOpt[String].get === "(C_1_3_user29_v016) ----> (C_1_2_user29_v016)"
      (componentOut_3 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_3 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "EXCLUDED_COMPONENT"
      (componentOut_3 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_3: JsValue = CommonFunction.currentCongig(wC)
      
      val result_3 = (jsonCurrentConfigOut_3 \ "result")
      (jsonCurrentConfigOut_3 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_3 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_3 \ "step" \ "components").asOpt[List[JsValue]].get.size === 2
      ((result_3 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_2_user29_v016")
      ((result_3 \ "step" \ "components")(1) \ "nameToShow").asOpt[String] === Some("C_1_1_user29_v016")
      
      val componentOut_4: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_4 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_4 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_4 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_4 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_4 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
      (componentOut_4 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "ALLOW_NEXT_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_4 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_4: JsValue = CommonFunction.currentCongig(wC)
      
      val result_4 = (jsonCurrentConfigOut_4 \ "result")
      (jsonCurrentConfigOut_4 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_4 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_4 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_4 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_2_user29_v016")
      
      
      val componentOut_5: JsValue = CommonFunction.selectComponent(wC, componentIdC12)
      
      (componentOut_5 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_5 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_5 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_5 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_5 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_2_user29_v016) ----> (C_1_3_user29_v016)"
      (componentOut_5 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "REMOVED_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_5 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_5: JsValue = CommonFunction.currentCongig(wC)
      
      val result_5 = (jsonCurrentConfigOut_5 \ "result")
      (jsonCurrentConfigOut_5 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_5 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_5 \ "step" \ "components").asOpt[List[JsValue]].get.size === 0
//      ((result_5 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_2_user29_v016")
      
      val componentOut_6: JsValue = CommonFunction.selectComponent(wC, componentIdC13)
      
      (componentOut_6 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_6 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 2
      (((componentOut_6 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_6 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_6 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_3_user29_v016) ----> (C_1_1_user29_v016)"
      (((componentOut_6 \ "result" \ "dependencies")(1)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_6 \ "result" \ "dependencies")(1)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_6 \ "result" \ "dependencies")(1)) \ "nameToShow").asOpt[String].get === "(C_1_3_user29_v016) ----> (C_1_2_user29_v016)"
      (componentOut_6 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_6 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "ADDED_COMPONENT"
      (componentOut_6 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_6 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "NOT_EXCLUDED_COMPONENT"
      (componentOut_6 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_6: JsValue = CommonFunction.currentCongig(wC)
      
      val result_6 = (jsonCurrentConfigOut_6 \ "result")
      (jsonCurrentConfigOut_6 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_6 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_6 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_6 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_3_user29_v016")
      
      val componentOut_7: JsValue = CommonFunction.selectComponent(wC, componentIdC11)
      
      (componentOut_7 \ "json").asOpt[String].get === JsonNames.COMPONENT
      (componentOut_7 \ "result" \ "dependencies").asOpt[List[JsValue]].get.size === 1
      (((componentOut_7 \ "result" \ "dependencies")(0)) \ "dependencyType").asOpt[String].get === "exclude"
      (((componentOut_7 \ "result" \ "dependencies")(0)) \ "visualization").asOpt[String].get === "remove"
      (((componentOut_7 \ "result" \ "dependencies")(0)) \ "nameToShow").asOpt[String].get === "(C_1_1_user29_v016) ----> (C_1_3_user29_v016)"
//      (((componentOut_7 \ "result" \ "dependencies")(1)) \ "dependencyType").asOpt[String].get === "exclude"
//      (((componentOut_7 \ "result" \ "dependencies")(1)) \ "visualization").asOpt[String].get === "remove"
//      (((componentOut_7 \ "result" \ "dependencies")(1)) \ "nameToShow").asOpt[String].get === "(C_1_3_user29_v016) ----> (C_1_2_user29_v016)"
      (componentOut_7 \ "result" \ "status" \"componentType" \ "status").asOpt[String].get === "DEFAULT_COMPONENT"
      (componentOut_7 \ "result" \ "status" \"selectedComponent" \ "status").asOpt[String].get === "NOT_ALLOWED_COMPONENT"
      (componentOut_7 \ "result" \ "status" \"selectionCriterium" \ "status").asOpt[String].get === "REQUIRE_NEXT_STEP"
      (componentOut_7 \ "result" \ "status" \"excludeDependency" \ "status").asOpt[String].get === "EXCLUDED_COMPONENT"
      (componentOut_7 \ "result" \ "status" \"common" \ "status").asOpt[String].get === "SUCCESS"
      
      val jsonCurrentConfigOut_7: JsValue = CommonFunction.currentCongig(wC)
      
      val result_7 = (jsonCurrentConfigOut_7 \ "result")
      (jsonCurrentConfigOut_7 \ "json").asOpt[String] === Some(JsonNames.CURRENT_CONFIG)
      (result_7 \ "step" \ "nameToShow").asOpt[String] === Some("S1_user29_v016")
      (result_7 \ "step" \ "components").asOpt[List[JsValue]].get.size === 1
      ((result_7 \ "step" \ "components")(0) \ "nameToShow").asOpt[String] === Some("C_1_3_user29_v016")
    }
  }
}