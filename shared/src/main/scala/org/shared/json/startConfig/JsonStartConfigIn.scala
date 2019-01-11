package org.shared.json.startConfig

import org.shared.json.{JsonNames, step}
import play.api.libs.json.{Json, OFormat}
/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 23.12.2016
 */
case class JsonStartConfigIn (
    json: String = JsonNames.START_CONFIG,
    params: JsonStartConfigParams
)

object JsonStartConfigIn {
  implicit val format: OFormat[step.JsonStartConfigIn] = Json.format[step.JsonStartConfigIn]
}