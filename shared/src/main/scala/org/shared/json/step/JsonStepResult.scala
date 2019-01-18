package org.shared.json.step

import org.shared.json.JsonKey
import org.shared.json.common.{JsonError, JsonWarning}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonStepResult(
                           step: Option[JsonStep] = None, // TODO Option with Standard
                           componentsForSelection: Option[List[JsonComponent]] = None,
                           errors: Option[List[JsonError]] = None,
                           warnings: Option[List[JsonWarning]] = None
                         )

object JsonStepResult {
  implicit val format: Format[JsonStepResult] = (
    (JsPath \ JsonKey.step).format(Format.optionWithNull[JsonStep]) and
    (JsPath \ JsonKey.componentsForSelection).format(Format.optionWithNull[List[JsonComponent]]) and
    (JsPath \ JsonKey.errors).format(Format.optionWithNull[List[JsonError]]) and
    (JsPath \ JsonKey.warnings).format(Format.optionWithNull[List[JsonWarning]])
  )(JsonStepResult.apply, unlift(JsonStepResult.unapply))
}
