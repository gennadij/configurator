package org.shared.json.step

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
                           componentsForSelection: JsonComponent,
                           errors: List[String],
                           warnings: List[String]
                         )

object JsonStepResult {
  implicit val writes: Format[JsonStartConfigResult] = (
    (JsPath \ "step").format(Writes.of[JsonStep]) and
    (JsPath \ "componentsForSelection").format(Writes.of[JsonComponent]) and
    (JsPath \ "errors").format(Writes.of[List[String]]) and
    (JsPath \ "warnings").format(Writes.of[List[String]])
  )(JsonStartConfigResult.apply, unlift(JsonStartConfigResult.unapply))
}
