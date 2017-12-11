package models.json.startConfig

import models.json.common.JsonStep
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * result : {step: ...}
 */
case class JsonStartConfigResult (
    status: String,
    message: String,
    step: Option[JsonStep]
)

object JsonStartConfigResult {
  import models.json.common.JsonStep.format
  implicit val writes: Writes[JsonStartConfigResult] = (
      (JsPath \ "status").write[String] and
      (JsPath \ "message").write[String] and
      (JsPath \ "step").write(Writes.optionWithNull[JsonStep])
  )(unlift(JsonStartConfigResult.unapply))
}