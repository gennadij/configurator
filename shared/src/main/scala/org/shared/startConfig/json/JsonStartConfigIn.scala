package org.shared.startConfig.json

import org.shared.common.JsonNames
import play.api.libs.json.Json
/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, params : ...}
 */
case class JsonStartConfigIn (
    json: String = JsonNames.START_CONFIG,
    params: JsonStartConfigParams
)

object JsonStartConfigIn {
  implicit val format = Json.format[JsonStartConfigIn]
}