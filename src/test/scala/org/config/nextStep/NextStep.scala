package org.config.nextStep

import org.specs2.mutable.Specification
import org.config.web.ConfigWeb
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue

class NextStep extends Specification with ConfigWeb{
  "Specs spezifiziert der Auswahl der Komponente und folgenden NextStep mit Komponente " >> {
    val nextStepCS = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "configId" -> "#21:13",
            "componentIds" -> Json.arr("#29:22")
        )
    )
    val nextStepSC: JsValue = handleMessage(nextStepCS)
    
    "NextStep" >> {
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
    	(nextStepSC \ "dto").asOpt[String].get === DTONames.nextSTep
    	"staus \\ kind" >> {
    	  (nextStepSC \ "status" \ "kind").asOpt[String].get === "ok"
    	}
    	"staus \\ id" >> {
    	  (nextStepSC \ "status" \ "id").asOpt[Int].get === 0
    	}
    	"status \\ message" >> {
    	  (nextStepSC \ "status" \ "message").asOpt[String].get === 
    	    "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
    	}
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