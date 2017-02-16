package org.currentConfig

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.config.web.Config
import play.api.libs.json.Json
import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.JsValue

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
    
    println(startConfigCS)
    
    "result \\ currentConfig" >> {
        (startConfigSC \ "result" \ "currentConfig" ).asOpt[Seq[JsValue]].get.size === 1
    }
  }
  
}