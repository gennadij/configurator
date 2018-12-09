package org.shared.json.nextStep

import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class JsonNextStepParams (
    componentIds: List[String]
)

object JsonNextStepParams {
  implicit val format: Format[JsonNextStepParams] = Json.format[JsonNextStepParams]
}