package org.config.nextStep

import org.specs2.mutable.Specification
import org.config.web.Config
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue
//import org.junit.runner.RunWith
//import org.specs2.runner.JUnitRunner

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 11.02.2017
 */

//@RunWith(classOf[JUnitRunner])
class SpecsNextStep extends Specification with Config{
  "Specs spezifiziert der Auswahl der Komponente und folgenden NextStep mit Komponente " >> {
    val componentIds: List[String] = getComponentIds
    val nextStepCS = Json.obj(
        "dtoId" -> DTOIds.nextStep,
        "dto" -> DTONames.nextSTep,
        "params" -> Json.obj(
            "componentIds" -> Json.arr(componentIds(0)),
            "clientId" -> ""
        )
    )
    println(nextStepCS)
    val nextStepSC: JsValue = handleMessage(nextStepCS)
    println(nextStepSC)
    "NextStep" >> {
    	(nextStepSC \ "dtoId").asOpt[Int].get === DTOIds.nextStep
    	(nextStepSC \ "dto").asOpt[String].get === DTONames.nextSTep
    	"staus \\ kind" >> {
    	  (nextStepSC \ "status" \ "kind").asOpt[String].get === "ok"
    	}
    	"staus \\ id" >> {
    	  (nextStepSC \ "status" \ "id").asOpt[Int].get === 1
    	}
    	"status \\ message" >> {
    	  (nextStepSC \ "status" \ "message").asOpt[String].get === 
    	    "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
    	}
    	"result \\ step \\ stepId" >> {
    	  (nextStepSC \ "result" \ "step" \ "nameToShow").asOpt[String].get === "Next Step"
    	}
      "result \\ step \\ components-> size" >> {
        (nextStepSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.size == 2
      }
      "result \\ step \\ components(0) \\ kind" >> {
        (((nextStepSC \ "result" \ "step" \ "components")(0)) \ "nameToShow").asOpt[String].get == "Component"
      }
      "result \\ step \\ components(1) \\ kind" >> {
        (((nextStepSC \ "result" \ "step" \ "components")(1)) \ "nameToShow").asOpt[String].get == "Component"
      }
    }
  }
  
  private def getComponentIds: List[String] = {
    val startConfigCS = Json.obj(
        "dtoId" -> DTOIds.startConfig,
        "dto" -> DTONames.startConfig,
        "params" -> Json.obj(
            "configUrl" -> "http://contig/user10"
        )
    )
    println(startConfigCS)
    val startConfigSC: JsValue = handleMessage(startConfigCS)
    println(startConfigSC)
    (startConfigSC \ "result" \ "step" \ "components").asOpt[List[JsValue]].get.map(c => {
      (c \ "componentId").asOpt[String].get
    })
  }
}