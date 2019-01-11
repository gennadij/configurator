package org.shared.json.step

import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 07.01.2019
 */

case class JsonStepParams (
    configUrl: String,
    componentId: String
)

object JsonStepParams{
  implicit val format: OFormat[JsonStepParams] = Json.format[JsonStepParams]
}


