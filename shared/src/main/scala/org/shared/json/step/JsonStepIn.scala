package org.shared.json.step

import org.shared.json.JsonNames
import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 07.01.2019
 */
case class JsonStepIn (
    json: String = JsonNames.START_CONFIG,
    params: JsonStepParams
)

object JsonStepIn{
  implicit val format: OFormat[JsonStepIn] = Json.format[JsonStepIn]
}

