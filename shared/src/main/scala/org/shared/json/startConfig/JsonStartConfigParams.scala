package org.shared.json.startConfig

import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * params : {configUri: http://config/test}
 */

case class JsonStartConfigParams (
    configUrl: String
)

object JsonStartConfigParams {
  implicit val format: OFormat[JsonStartConfigParams] = Json.format[JsonStartConfigParams]
}