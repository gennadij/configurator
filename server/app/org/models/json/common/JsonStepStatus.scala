package models.json.common

import play.api.libs.json.Writes
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 01.03.2018
 */
case class JsonStepStatus (
    firstStep: Option[JsonStatus] = None,
    nextStep: Option[JsonStatus] = None,
    fatherStep: Option[JsonStatus] = None,
    common: Option[JsonStatus] = None
)

object JsonStepStatus {
  import models.json.common.JsonStatus.writerJsonStatus
  implicit val writerJsonStepStatus: Writes[JsonStepStatus] = (
    (JsPath \ "firstStep").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "nextStep").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "fatherStep").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "common").write(Writes.optionWithNull[JsonStatus])
  )(unlift(JsonStepStatus.unapply))
}