package org.shared.json.common

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 01.03.2018
 */
//TODO TO DELETE
case class JsonStepStatus (
    firstStep: Option[JsonStatus] = None,
    nextStep: Option[JsonStatus] = None,
    currentStep: Option[JsonStatus] = None,
    currentConfig: Option[JsonStatus] = None,
    common: Option[JsonStatus] = None
)

object JsonStepStatus {
  implicit val formatJsonStepStatus: Format[JsonStepStatus] = (
    (JsPath \ "firstStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "nextStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "currentStep").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "currentConfig").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "common").format(Format.optionWithNull[JsonStatus])
  )(JsonStepStatus.apply, unlift(JsonStepStatus.unapply))
}