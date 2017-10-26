package models.json.startConfig

import com.sun.org.glassfish.external.arc.Stability
import play.api.libs.json.Json
import models.json.JsonNames
/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, params : ...}
 */
case class StartConfigIn (
    json: String = JsonNames.startConfig,
    params: StartConfigParams
)

object StartConfigIn {
  implicit val format = Json.reads[StartConfigIn]
}