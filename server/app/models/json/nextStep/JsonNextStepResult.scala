package models.json.nextStep

import models.json.common.JsonStep
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.json.common.JsonStepStatus

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
  implicit val writes: Writes[JsonNextStepResult] = (
      (JsPath \ "step").write(Writes.of[JsonStep]) and 
      (JsPath \ "status").write(Writes.of[JsonStepStatus])
  )(unlift(JsonNextStepResult.unapply))
}