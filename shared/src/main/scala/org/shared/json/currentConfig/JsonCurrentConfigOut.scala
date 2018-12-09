package org.shared.json.currentConfig

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

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
  implicit val format: Format[JsonCurrentConfigOut] = Json.format[JsonCurrentConfigOut]
}