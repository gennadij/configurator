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
  
  "Diese Specifikation spezifiziert die aktuelle KOnfiguration" >> {
    val startConfigCS = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10"
        )
    )
    val startConfigSC: JsValue = handleMessage(startConfigCS)
    
    println(startConfigSC)
    
    "result \\ currentConfig.size" >> {
        (startConfigSC \ "result" \ "currentConfig" ).asOpt[Seq[JsValue]].get.size === 1
    }
    "result \\ currentConfig(0) \\ nameToShow" >> {
      (((startConfigSC \ "result" \ "currentConfig")(0)) \ "nameToShow").asOpt[String].get === "First Step"
    }
    "result \\ currentConfig(0) \\ components.size" >> {
      (((startConfigSC \ "result" \ "currentConfig")(0)) \ "components").asOpt[Seq[JsValue]].get.size === 0
    }
  }
  
}