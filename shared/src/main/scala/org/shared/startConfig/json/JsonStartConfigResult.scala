package org.shared.startConfig.json

import org.shared.common.json.{JsonStep, JsonStepStatus}
import play.api.libs.functional.syntax._
import play.api.libs.json._

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
  implicit val writes: Format[JsonStartConfigResult] = (
      (JsPath \ "step").format(Writes.of[JsonStep]) and
      (JsPath \ "status").format(Writes.of[JsonStepStatus])
  )(JsonStartConfigResult.apply, unlift(JsonStartConfigResult.unapply))
}