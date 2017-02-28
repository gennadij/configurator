package org.currentConfig

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.config.web.Config
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import org.specs2.specification.BeforeAll


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.02.2017
 */

@RunWith(classOf[JUnitRunner])
class SpecsCurrentConfigMutable extends Specification with Config with BeforeAll{
  
  val clientId_1: String = java.util.UUID.randomUUID.toString
  val clientId_2: String = java.util.UUID.randomUUID.toString
  def beforeAll = {
    println("before")
    val startConfigCSForClient_1 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10",
            "clientId" -> clientId_1
        )
    )
    
//    println(startConfigCSForClient_1)
    
    val startConfigSCForClient_1: JsValue = handleMessage(startConfigCSForClient_1)
    
//    println(startConfigSCForClient_1)
    
    val startConfigCSForClient_2 = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10",
            "clientId" -> clientId_2
        )
    )
    
//    println(startConfigCSForClient_2)
    
    val startConfigSCForClient_2: JsValue = handleMessage(startConfigCSForClient_2)
    
//    println(startConfigSCForClient_2)
    
  }
  
  "Diese Specifikation spezifiziert die aktuelle Konfiguration" >> {
    "CurrentConfig after StartConfiguration Client 1" >> {
      val currentConfigCSAfterStartConfig_1 = Json.obj(
          "dtoId" -> DTOIds.CURRENT_CONFIG,
          "dto" -> DTONames.CURRENT_CONFIG,
          "params" -> Json.obj(
              "clientId" -> clientId_1
          )
      )
      
//      println(currentConfigCSAfterStartConfig_1)
      
      val currentConfigSCAfterStartConfig_1 = handleMessage(currentConfigCSAfterStartConfig_1)
      
//      println(currentConfigSCAfterStartConfig_1)
      
      (currentConfigSCAfterStartConfig_1 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_1 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("FirstStep")
      (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 0
      (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow").asOpt[String] === None
    }
    "CurrentConfig after StartConfiguration Client 2" >> {
      val currentConfigCSAfterStartConfig_2 = Json.obj(
          "dtoId" -> DTOIds.CURRENT_CONFIG,
          "dto" -> DTONames.CURRENT_CONFIG,
          "params" -> Json.obj(
              "clientId" -> clientId_2
          )
      )
      
  //    println(currentConfigCSAfterStartConfig_2)
      
      val currentConfigSCAfterStartConfig_2 = handleMessage(currentConfigCSAfterStartConfig_2)
      
  //    println(currentConfigSCAfterStartConfig_2)
      
      (currentConfigSCAfterStartConfig_2 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
      (currentConfigSCAfterStartConfig_2 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("FirstStep")
      (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")
          .asOpt[Seq[JsValue]].get.size === 0
      (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow").asOpt[String] === 
            None
    }
  }
}