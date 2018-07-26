package org.shared.currentConfig.json

import org.shared.common.JsonNames
import play.api.libs.json.{Json, OWrites}

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigOut (
    json: String = JsonNames.CURRENT_CONFIG,
    result: JsonCurrentConfigResult
)

object JsonCurrentConfigOut {
  implicit val format: OWrites[JsonCurrentConfigOut] = Json.writes[JsonCurrentConfigOut]
}