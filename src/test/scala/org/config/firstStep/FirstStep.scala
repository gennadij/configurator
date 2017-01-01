package org.config.firstStep

import org.specs2.mutable.Specification
import org.config.web.ConfigWeb
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import org.dto.startConfig.StartConfigSC
import play.api.libs.json.JsValue

class FirstStep extends Specification with ConfigWeb{
  
  "Specs spezifiziert der Start der Konfiguration" >> {
    val startConfigCS = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUri" -> "http://contig/test_4"
        )
    )
//    {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
    val startConfigSC: JsValue = handleMessage(startConfigCS)
    
//    FirstStep {"dtoId":1,"dto":"StartConfig","result":{"step":{"id":"#25:29","kind":"first","components":[{"id":"#29:22","kind":"immutable","nextStep":"#26:26"},{"id":"#30:20","kind":"immutable","nextStep":"#26:26"},{"id":"#31:20","kind":"immutable","nextStep":"#27:23"}]}}}
    println("FirstStep " + startConfigSC)
    "FirstStep" >> {
    	(startConfigSC \ "dtoId").asOpt[Int].get === DTOIds.startConfig
    	(startConfigSC \ "dtoId").asOpt[Int].get === DTOIds.startConfig
      "result \\ step \\ kind" >> {
        (startConfigSC \ "result" \ "step" \ "kind").asOpt[String].get === "first"
      }
      "result \\ step \\ components-> size" >> {
        (startConfigSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.size == 3
      }
      "result \\ step \\ components(0) \\ kind" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(0)) \ "kind").asOpt[String].get == "immutable"
      }
      "result \\ step \\ components(1) \\ kind" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(1)) \ "kind").asOpt[String].get == "immutable"
      }
      "result \\ step \\ components(2) \\ kind" >> {
        (((startConfigSC \ "result" \ "step" \ "components")(2)) \ "kind").asOpt[String].get == "immutable"
      }
    }
  }
}