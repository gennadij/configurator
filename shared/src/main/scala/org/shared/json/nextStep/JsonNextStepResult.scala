package org.shared.json.nextStep

import org.shared.json.common.{JsonStep, JsonStepStatus}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class JsonNextStepResult (
    step: JsonStep,
    status: JsonStepStatus
)

object JsonNextStepResult {
  implicit val format: Format[JsonNextStepResult] = (
      (JsPath \ "step").format(Format.of[JsonStep]) and
      (JsPath \ "status").format(Format.of[JsonStepStatus])
  )(JsonNextStepResult.apply, unlift(JsonNextStepResult.unapply))
}