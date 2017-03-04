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
 * Created by Gennadi Heimann on 01.03.2017
 */

@RunWith(classOf[JUnitRunner])
class SpecsCurrentConfigNextStep extends Specification with Config with BeforeAfterAll{
  
  private val clientId_1: String = "623dd31e-e808-48c7-b493-46435b687820"
  private val clientId_2: String = "8f91f784-c60d-40d7-9641-abc02cc88eb2"
//  private val clientId_2: String = java.util.UUID.randomUUID.toString
  
  def afterAll(): Unit = {
    CurrentConfig.getCuttentConfig.clear()
    require(CurrentConfig.getCuttentConfig.size == 0, "Size is " + CurrentConfig.getCuttentConfig.size)
  }
  
  def beforeAll(): Unit = {
    val startConfigCSForClient_1 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10",
            "clientId" -> clientId_1
        )
    )
    
    println(startConfigCSForClient_1)
    
    val startConfigSCForClient_1: JsValue = handleMessage(startConfigCSForClient_1)
    
    println(startConfigSCForClient_1)
    
    val selectedComponentForClient_1 = 
      (((startConfigSCForClient_1 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
    
    val nextStepCSForClient_1 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponentForClient_1),
            "clientId" -> clientId_1
        )
    )
    
    println(nextStepCSForClient_1)
    
    val nextStepSCForClient_1 = handleMessage(nextStepCSForClient_1)
    
    println(nextStepSCForClient_1)
    
    // CLIENT 2
    
    val startConfigCSForClient_2 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10",
            "clientId" -> clientId_2
        )
    )
    
    println(startConfigCSForClient_2)
    
    val startConfigSCForClient_2: JsValue = handleMessage(startConfigCSForClient_2)
    
    println(startConfigSCForClient_2)
    
    val selectedComponentForClient_2 = 
      (((startConfigSCForClient_2 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
    
    val nextStepCSForClient_2 = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> List(selectedComponentForClient_2),
            "clientId" -> clientId_2
        )
    )
    
    println(nextStepCSForClient_2)
    
    val nextStepSCForClient_2 = handleMessage(nextStepCSForClient_2)
    
    println(nextStepSCForClient_2)
    
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
      
      println(currentConfigCSAfterStartConfig_1)
      
      val currentConfigSCAfterStartConfig_1 = handleMessage(currentConfigCSAfterStartConfig_1)
      
      println(currentConfigSCAfterStartConfig_1)
      
      (currentConfigSCAfterStartConfig_1 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "nameToShow")
        .asOpt[String] === Some("Step#25:55")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow")
      .asOpt[String] === Some("Component#29:41")
    }
    "CurrentConfig after StartConfiguration Client 2" >> {
      val currentConfigCSAfterStartConfig_2 = Json.obj(
          "dtoId" -> DTOIds.CURRENT_CONFIG,
          "dto" -> DTONames.CURRENT_CONFIG,
          "params" -> Json.obj(
              "clientId" -> clientId_2
          )
      )
      
      println(currentConfigCSAfterStartConfig_2)
      
      val currentConfigSCAfterStartConfig_2 = handleMessage(currentConfigCSAfterStartConfig_2)
      
      println(currentConfigSCAfterStartConfig_2)
      
      (currentConfigSCAfterStartConfig_2 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("Step#25:55")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")
        .asOpt[Seq[JsValue]].get.size === 1
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow")
        .asOpt[String] === Some("Component#29:41")
    }
  }
}