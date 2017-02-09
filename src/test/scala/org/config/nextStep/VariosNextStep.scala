package org.config.nextStep

import org.specs2.mutable.Specification
import org.config.web.Config
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue
import org.status.ErrorIds
import org.status.ErrorStrings

class VariosNextStep extends Specification with Config{
  "Specs spezifiziert zwei asgewaehlte Komponente die zu zwei verschiedenen Steps fuehren" >> {
    val nextStepCS = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "configId" -> "#21:13",
            "componentIds" -> Json.arr("#29:22", "#31:20")
        )
    )
//    {dtoId : 1, dto : StartConfig, params : {configUri: http://config/test}}
    val nextStepSC: JsValue = handleMessage(nextStepCS)
    
    println("NextStep " + nextStepSC)
    "FirstStep" >> {
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
    	"staus \\ kind" >> {
    	  (nextStepSC \ "status" \ "kind").asOpt[String].get === "error"
    	}
    	"staus \\ id" >> {
    	  (nextStepSC \ "status" \ "id").asOpt[Int].get === ErrorIds.selectedComponentsHasVariosNextStep
    	}
    	"status \\ message" >> {
    	  (nextStepSC \ "status" \ "message").asOpt[String].get === 
    	    ErrorStrings.selectedComponentsHasVariosNextStep
    	}
      "result \\ step \\ kind" >> {
        (nextStepSC \ "result" \ "step" \ "kind").asOpt[String].get === ""
      }
      "result \\ step \\ components-> size" >> {
        (nextStepSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.size == 0
      }
    }
  }
}