package org.currentConfig

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.config.web.Config
import org.specs2.specification.BeforeAfterAll
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 04.03.2017
 * 
 * Schema 1
 * 
 * Client 1
 * 
 * Step 1 -> Component S1C1
 * Step 2 -> Component S2C2
 * Step 3 -> Component S3C3
 * Step 4 -> Component S4C2
 * Step 5 -> Component S5C3
 * Step 6 -> Component S6C1
 * 
 * Client 2
 * 
 * Step 1 -> Component S1C2
 * Step 2 -> Component S2C1
 * Step 3 -> Component S3C2
 * Step 4 -> Component S4C1
 * Step 5 -> Component S5C4
 * Step 6 -> Component S6C2
 */

@RunWith(classOf[JUnitRunner])
class SpecsCurrentConfigBySchema1 extends Specification with Config with BeforeAfterAll{
  val clientId_2: String = "8f91f784-c60d-40d7-9641-abc02cc88eb2"
  val clientId_1: String = "623dd31e-e808-48c7-b493-46435b687820"
//  private val clientId_2: String = java.util.UUID.randomUUID.toString
  
  def afterAll(): Unit = {
    CurrentConfig.getCuttentConfig.clear()
    require(CurrentConfig.getCuttentConfig.size == 0, "Size is " + CurrentConfig.getCuttentConfig.size)
  }
  
  def beforeAll(): Unit = {
    prepareClient1
    prepareClient2
  }

  "Diese Specifikation spezifiziert die aktuelle Konfiguration nach dem 2, Step" >> {
    "CurrentConfig after 2. Step Client 1" >> {
      val currentConfigCSAfterStartConfig_1 = Json.obj(
          "dtoId" -> DTOIds.CURRENT_CONFIG,
          "dto" -> DTONames.CURRENT_CONFIG,
          "params" -> Json.obj(
              "clientId" -> clientId_1
          )
      )
      
//      println("Client 1 " + currentConfigCSAfterStartConfig_1)
      
      val currentConfigSCAfterStartConfig_1 = handleMessage(currentConfigCSAfterStartConfig_1)
      
//      println("Client 1 " + currentConfigSCAfterStartConfig_1)
      
      (currentConfigSCAfterStartConfig_1 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 6
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "nameToShow")
        .asOpt[String] === Some("S_1")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_1_1")
      
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(1)) \ "nameToShow")
        .asOpt[String] === Some("S_2")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(1)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(1)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_2_1")
      
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(2)) \ "nameToShow")
        .asOpt[String] === Some("S_3")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(2)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(2)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_3_3")
        
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(3)) \ "nameToShow")
        .asOpt[String] === Some("S_4")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(3)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(3)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_4_2")
        
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(4)) \ "nameToShow")
        .asOpt[String] === Some("S_5")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(4)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(4)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_5_3")
      
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(5)) \ "nameToShow")
        .asOpt[String] === Some("S_8")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(5)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(5)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_8_1")
    }
    
    "CurrentConfig after StartConfiguration Client 2" >> {
      val currentConfigCSAfterStartConfig_2 = Json.obj(
          "dtoId" -> DTOIds.CURRENT_CONFIG,
          "dto" -> DTONames.CURRENT_CONFIG,
          "params" -> Json.obj(
              "clientId" -> clientId_2
          )
      )
      
//      println("Client 2 " + currentConfigCSAfterStartConfig_2)
      
      val currentConfigSCAfterStartConfig_2 = handleMessage(currentConfigCSAfterStartConfig_2)
      
//      println("Client 2 " + currentConfigSCAfterStartConfig_2)
      
      (currentConfigSCAfterStartConfig_2 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 6
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("S_1")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_1_2")
        
        (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(1)) \ "nameToShow").asOpt[String] === Some("S_2")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(1)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(1)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_2_1")
        
        (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(2)) \ "nameToShow").asOpt[String] === Some("S_3")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(2)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(2)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_3_2")
        
        (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(3)) \ "nameToShow").asOpt[String] === Some("S_4")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(3)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(3)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_4_1")
        
        (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(4)) \ "nameToShow").asOpt[String] === Some("S_5")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(4)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(4)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_5_4")
        
        (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(5)) \ "nameToShow").asOpt[String] === Some("S_7")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(5)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(5)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("C_7_2")
    }
  }
  
  def prepareClient1 = {
    
    val startConfigCSForClient_1 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user12",
            "clientId" -> clientId_1
        )
    )
    
//    println(startConfigCSForClient_1)
    
    val startConfigSCForClient_1: JsValue = handleMessage(startConfigCSForClient_1)
    
//    println(startConfigSCForClient_1)
    
    //=================================================================================================================
    
    val selectedComponent_1_ForClient_1 = 
      (((startConfigSCForClient_1 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
    
    val nextStepCS_2_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_1_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(nextStepCS_2_ForClient_1)
    
    val nextStepSC_2_ForClient_1 = handleMessage(nextStepCS_2_ForClient_1)
    
//    println(nextStepSC_2_ForClient_1)
    
    //================================================================================================================
    
    val selectedComponent_2_ForClient_1 = 
      (((nextStepSC_2_ForClient_1 \ "result" \ "step" \ "components")(1)) \ "componentId").asOpt[String].get
    
    val nextStepCS_3_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_2_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(nextStepCS_3_ForClient_1)
    
    val nextStepSC_3_ForClient_1 = handleMessage(nextStepCS_3_ForClient_1)
    
//    println(nextStepSC_3_ForClient_1)
    
    //================================================================================================================
    
    val selectedComponent_3_ForClient_1 = 
      (((nextStepSC_3_ForClient_1 \ "result" \ "step" \ "components")(2)) \ "componentId").asOpt[String].get
      
    val nextStepCS_4_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_3_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(nextStepCS_4_ForClient_1)
    
    val nextStepSC_4_ForClient_1 = handleMessage(nextStepCS_4_ForClient_1)
    
//    println(nextStepSC_4_ForClient_1)
    
    //================================================================================================================
    
    val selectedComponent_4_ForClient_1 = 
      (((nextStepSC_4_ForClient_1 \ "result" \ "step" \ "components")(1)) \ "componentId").asOpt[String].get
    
    val nextStepCS_5_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_4_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(nextStepCS_5_ForClient_1)
    
    val nextStepSC_5_ForClient_1 = handleMessage(nextStepCS_5_ForClient_1)
    
//    println(nextStepSC_5_ForClient_1)
    
    //================================================================================================================
    
    val selectedComponent_5_ForClient_1 = 
      (((nextStepSC_5_ForClient_1 \ "result" \ "step" \ "components")(2)) \ "componentId").asOpt[String].get
    
    val nextStepCS_6_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_5_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(nextStepCS_6_ForClient_1)
    
    val nextStepSC_6_ForClient_1 = handleMessage(nextStepCS_6_ForClient_1)
    
//    println(nextStepSC_6_ForClient_1)
    
    //================================================================================================================
    
    val selectedComponent_6_ForClient_1 = 
      (((nextStepSC_6_ForClient_1 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
      
       val finalStepCS_ForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_6_ForClient_1),
            "clientId" -> clientId_1
        )
    )
    
//    println(finalStepCS_ForClient_1)
    
    val finalStepSC_ForClient_1 = handleMessage(finalStepCS_ForClient_1)
    
//    println(finalStepSC_ForClient_1)
  }
  
  def prepareClient2 = {
    
    
    
    val startConfigCSForClient_2 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user12",
            "clientId" -> clientId_2
        )
    )
    
//    println(startConfigCSForClient_2)
    
    val startConfigSCForClient_2: JsValue = handleMessage(startConfigCSForClient_2)
    
//    println(startConfigSCForClient_2)
    
    val selectedComponent_1_ForClient_2 = 
      (((startConfigSCForClient_2 \ "result" \ "step" \ "components")(1)) \ "componentId").asOpt[String].get
    
    val nextStepCS_2_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_1_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(nextStepCS_2_ForClient_2)
    
    val nextStepSC_2_ForClient_2 = handleMessage(nextStepCS_2_ForClient_2)
    
//    println(nextStepSC_2_ForClient_2)
    
    //================================================================================================================
    
    val selectedComponent_2_ForClient_2 = 
      (((nextStepSC_2_ForClient_2 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
    
    val nextStepCS_3_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_2_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(nextStepCS_3_ForClient_2)
    
    val nextStepSC_3_ForClient_2 = handleMessage(nextStepCS_3_ForClient_2)
    
//    println(nextStepSC_3_ForClient_2)
    
    //================================================================================================================
    
    val selectedComponent_3_ForClient_2 = 
      (((nextStepSC_3_ForClient_2 \ "result" \ "step" \ "components")(1)) \ "componentId").asOpt[String].get
      
    val nextStepCS_4_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_3_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(nextStepCS_4_ForClient_2)
    
    val nextStepSC_4_ForClient_2 = handleMessage(nextStepCS_4_ForClient_2)
    
//    println(nextStepSC_4_ForClient_2)
    
    //================================================================================================================
    
    val selectedComponent_4_ForClient_2 = 
      (((nextStepSC_4_ForClient_2 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
    
    val nextStepCS_5_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_4_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(nextStepCS_5_ForClient_2)
    
    val nextStepSC_5_ForClient_2 = handleMessage(nextStepCS_5_ForClient_2)
    
//    println(nextStepSC_5_ForClient_2)
    
    //================================================================================================================
    
    val selectedComponent_5_ForClient_2 = 
      (((nextStepSC_5_ForClient_2 \ "result" \ "step" \ "components")(3)) \ "componentId").asOpt[String].get
    
    val nextStepCS_6_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_5_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(nextStepCS_6_ForClient_2)
    
    val nextStepSC_6_ForClient_2 = handleMessage(nextStepCS_6_ForClient_2)
    
//    println(nextStepSC_6_ForClient_2)
    
    val selectedComponent_6_ForClient_2 = 
      (((nextStepSC_6_ForClient_2 \ "result" \ "step" \ "components")(1)) \ "componentId").asOpt[String].get
    
    val finalStepCS_ForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponent_6_ForClient_2),
            "clientId" -> clientId_2
        )
    )
    
//    println(finalStepCS_ForClient_2)
    
    val finalStepSC_ForClient_2 = handleMessage(finalStepCS_ForClient_2)
    
//    println(finalStepSC_ForClient_2)
    
  }
}