package org.config.firstStep

import org.specs2.mutable.Specification
import org.config.web.Config
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import org.dto.startConfig.StartConfigSC
import play.api.libs.json.JsValue
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.persistence.GlobalConfigForDB

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 25.12.2016
 */

@RunWith(classOf[JUnitRunner])
class SpecsFirstStep extends Specification with Config{
  
  "Specs spezifiziert der Start der Konfiguration" >> {
    val startConfigCS = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> GlobalConfigForDB.activeUrl,
            "clientId" -> ""
        )
    )
//    {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
    val startConfigSC: JsValue = handleMessage(startConfigCS)
    
//    FirstStep {"dtoId":1,"dto":"StartConfig","result":{"step":{"id":"#25:29","kind":"first","components":[{"id":"#29:22","kind":"immutable","nextStep":"#26:26"},{"id":"#30:20","kind":"immutable","nextStep":"#26:26"},{"id":"#31:20","kind":"immutable","nextStep":"#27:23"}]}}}
    println("FirstStep " + startConfigSC)
    "FirstStep" >> {
    	(startConfigSC \ "dtoId").asOpt[Int].get === DTOIds.startConfig
    	(startConfigSC \ "dtoId").asOpt[Int].get === DTOIds.startConfig
      "result \\ status" >> {
        (startConfigSC \ "result" \ "status").asOpt[Boolean].get === true
      }
      "result \\ message" >> {
        (startConfigSC \ "result" \ "message").asOpt[String].get === "FirstStep"
      }
      "result \\ step \\ nameToShow" >> {
        (startConfigSC \ "result" \ "step" \ "nameToShow" ).asOpt[String].get === "S_1"
      }
      "result \\ step \\ components-> size" >> {
        (startConfigSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.size == 3
      }
      "result \\ step \\ components(0) \\ nameToShow" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(0)) \ "nameToShow").asOpt[String].get == "C_1_1"
      }
      "result \\ step \\ components(1) \\ nameToShow" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(1)) \ "nameToShow").asOpt[String].get == "C_1_2"
      }
      "result \\ step \\ components(2) \\ nameToShow" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(2)) \ "nameToShow").asOpt[String].get == "C_1_3"
      }
    }
  }
}