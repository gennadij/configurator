package org.shared.nextStep.json

import play.api.libs.json.{Json, Reads}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class JsonNextStepParams (
    componentIds: List[String]
)

object JsonNextStepParams {
  implicit val format: Reads[JsonNextStepParams] = Json.reads[JsonNextStepParams]
}