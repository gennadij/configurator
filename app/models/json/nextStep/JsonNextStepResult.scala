package models.json.nextStep

import models.json.common.JsonStep
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class JsonNextStepResult (
    status: String,
    message: String,
    step: Option[JsonStep]
)

object JsonNextStepResult {
  implicit val writes: Writes[JsonNextStepResult] = (
      (JsPath \ "status").write[String] and
      (JsPath \ "message").write[String] and
      (JsPath \ "step").write(Writes.optionWithNull[JsonStep])
  )(unlift(JsonNextStepResult.unapply))
}