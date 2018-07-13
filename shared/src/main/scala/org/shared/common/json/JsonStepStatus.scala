package org.shared.common.json

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Json}

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
  implicit val formatJsonStepStatus: Format[JsonStepStatus] = (
    (JsPath \ "firstStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "nextStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "fatherStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "common").format(Format.optionWithNull[JsonStatus])
  )(JsonStepStatus.apply, unlift(JsonStepStatus.unapply))
}