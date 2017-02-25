package org.currentConfig

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.config.web.Config
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 16.02.2017
 */

@RunWith(classOf[JUnitRunner])
class SpecsCurrentConfig extends Specification with BeforeAfterAll with Config{
  
  def afterAll(): Unit = {}
  def beforeAll(): Unit = {}
  
  "Diese Specifikation spezifiziert die aktuelle Konfiguration" >> {
    "Clobal Settings" >> {
      
      val clientId_1: String = java.util.UUID.randomUUID.toString
      val clientId_2: String = java.util.UUID.randomUUID.toString
      
      "Current Config after StartConfig for Client 1" >> {
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
        
        "dtoId">> {
          (currentConfigSCAfterStartConfig_1 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
        }
        "dto" >> {
          (currentConfigSCAfterStartConfig_1 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
        }
        "result \\ steps.size" >> {
          (currentConfigSCAfterStartConfig_1 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
        }
        "result \\ steps(0) \\ nameToShow" >> {
          (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("FirstStep")
        }
        "result \\ steps(0) \\ components.size" >> {
          (((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 1
        }
        "result \\ steps(0) \\ components(0) \\ nameToShow" >> {
          (((((currentConfigSCAfterStartConfig_1 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow").asOpt[String] === 
            Some("Component 1")
        }
        
        "Current Config after StartConfig for Client 2" >> {
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
          
          "dtoId">> {
            (currentConfigSCAfterStartConfig_2 \ "dtoId").asOpt[Int] === Some(DTOIds.CURRENT_CONFIG)
          }
          "dto" >> {
            (currentConfigSCAfterStartConfig_2 \ "dto").asOpt[String] === Some(DTONames.CURRENT_CONFIG)
          }
          "result \\ steps.size" >> {
            (currentConfigSCAfterStartConfig_2 \ "result" \ "steps").asOpt[Seq[JsValue]].get.size === 1
          }
          "result \\ steps(0) \\ nameToShow" >> {
            (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "nameToShow").asOpt[String] === Some("FirstStep")
          }
          "result \\ steps(0) \\ components.size" >> {
            (((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 1
          }
          "result \\ steps(0) \\ components(0) \\ nameToShow" >> {
            (((((currentConfigSCAfterStartConfig_2 \ "result" \ "steps")(0)) \ "components")(0)) \ "nameToShow").asOpt[String] === 
              Some("Component 1")
          }
        }
        
        
        
        "result \\ currentConfig.size" >> {
            (startConfigSCForClient_1 \ "result" \ "currentConfig" ).asOpt[Seq[JsValue]].get.size === 1
        }
        "result \\ currentConfig(0) \\ nameToShow" >> {
          (((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "nameToShow").asOpt[String].get === "First Step"
        }
        "result \\ currentConfig(0) \\ components.size" >> {
          (((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 0
        }
        val selectedComponent = (((startConfigSCForClient_1 \ "result" \ "step" \ "components")(0)) \ "componentId").asOpt[String].get
        "2. Step -> selectedComponent " + selectedComponent >> {
          val nextStepCS = Json.obj(
              "dtoId" -> DTOIds.nextStep,
              "dto" -> DTONames.nextSTep,
              "params" -> Json.obj(
                  "componentIds" -> Json.arr(selectedComponent)
              )
          )
          println(nextStepCS)
          val nextStepSC: JsValue = handleMessage(nextStepCS)
          println(nextStepSC)
          "result \\ currentConfig.size" >> {
            (startConfigSCForClient_1 \ "result" \ "currentConfig" ).asOpt[Seq[JsValue]].get.size === 2
          }
          "result \\ currentConfig(0) \\ nameToShow" >> {
            (((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "nameToShow").asOpt[String].get === "First Step"
          }
          "result \\ currentConfig(0) \\ components.size" >> {
            (((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 1
          }
          "result \\ currentConfig(0) \\ components(0) \\ componentId" >> {
            ((((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "components")(0) \ "componentId").asOpt[String].get === 
              selectedComponent
          }
          "result \\ currentConfig(0) \\ components(0) \\ nameToShow" >> {
            ((((startConfigSCForClient_1 \ "result" \ "currentConfig")(0)) \ "components")(0) \ "nameToShow").asOpt[String].get === 
              "Component"
          }
          "result \\ currentConfig(1) \\ nameToShow" >> {
            (((startConfigSCForClient_1 \ "result" \ "currentConfig")(1)) \ "nameToShow").asOpt[String].get === "Next Step"
          }
          "result \\ currentConfig(1) \\ components.size" >> {
            (((startConfigSCForClient_1 \ "result" \ "currentConfig")(1)) \ "components").asOpt[Seq[JsValue]].get.size === 0
          }
          
        }
      }
    }
  }
}