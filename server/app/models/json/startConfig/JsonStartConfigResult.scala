package models.json.startConfig

import models.json.common.JsonStep
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.json.common.JsonStepStatus

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * result : {step: ...}
 */
case class JsonStartConfigResult (
    step: JsonStep,
    status: JsonStepStatus
)

object JsonStartConfigResult {
  implicit val writes: Writes[JsonStartConfigResult] = (
      (JsPath \ "step").write(Writes.of[JsonStep]) and 
      (JsPath \ "status").write(Writes.of[JsonStepStatus])
  )(unlift(JsonStartConfigResult.unapply))
}