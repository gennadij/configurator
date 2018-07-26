package org.shared.nextStep.json

import org.shared.common.JsonNames
import play.api.libs.json.{Json, OWrites}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.20016
 */

case class JsonNextStepOut (
    json: String = JsonNames.NEXT_STEP,
    result: JsonNextStepResult
)

object JsonNextStepOut {
  implicit val format: OWrites[JsonNextStepOut] = Json.writes[JsonNextStepOut]
}