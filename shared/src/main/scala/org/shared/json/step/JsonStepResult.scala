package org.shared.json.step

import org.shared.json.common.{JsonError, JsonWarning}
import org.shared.json.startConfig.JsonStartConfigResult
import play.api.libs.json.{Format, JsPath, Writes}
import play.api.libs.functional.syntax._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonStepResult(
                           step: JsonStep,
                           componentsForSelection: Set[JsonComponent],
                           errors: Set[JsonError],
                           warnings: Set[JsonWarning]
                         )

object JsonStepResult {
  implicit val writes: Format[JsonStartConfigResult] = (
    (JsPath \ "step").format(Writes.of[JsonStep]) and
    (JsPath \ "componentsForSelection").format(Writes.of[Set[JsonComponent]]) and
    (JsPath \ "errors").format(Writes.of[Set[JsonError]]) and
    (JsPath \ "warnings").format(Writes.of[Set[JsonWarning]])
  )(JsonStartConfigResult.apply, unlift(JsonStartConfigResult.unapply))
}
