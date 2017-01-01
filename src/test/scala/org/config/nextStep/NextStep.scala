package org.config.nextStep

import org.specs2.mutable.Specification
import org.config.web.ConfigWeb
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue

class NextStep extends Specification with ConfigWeb{
  "Specs spezifiziert der Start der Konfiguration" >> {
    val nextStepCS = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "configId" -> "#21:13",
            "componentIds" -> Json.arr("#29:22")
        )
    )
//    {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
    val nextStepSC: JsValue = handleMessage(nextStepCS)
    
    println("NextStep " + nextStepSC)
    "FirstStep" >> {
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
      "result \\ step \\ kind" >> {
        (nextStepSC \ "result" \ "step" \ "kind").asOpt[String].get === "default"
      }
      "result \\ step \\ components-> size" >> {
        (nextStepSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.size == 2
      }
      "result \\ step \\ components(0) \\ kind" >> {
        (((nextStepSC \ "result" \ "step" \ "components")(0)) \ "kind").asOpt[String].get == "immutable"
      }
      "result \\ step \\ components(1) \\ kind" >> {
        (((nextStepSC \ "result" \ "step" \ "components")(1)) \ "kind").asOpt[String].get == "immutable"
      }
    }
  }
}