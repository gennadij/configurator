package org.shared.startConfig.json

import org.shared.common.JsonNames
import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, result : ...}
 */

case class JsonStartConfigOut (
    json: String = JsonNames.START_CONFIG,
    result: JsonStartConfigResult
)

object JsonStartConfigOut {
  implicit val format = Json.format[JsonStartConfigOut]
}