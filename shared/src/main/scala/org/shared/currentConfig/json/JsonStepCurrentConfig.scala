package org.shared.currentConfig.json

import org.shared.common.json.JsonComponent
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
case class JsonStepCurrentConfig (
    stepId: String,
    nameToShow: String,
    components: List[JsonComponent],
    nextStep: Option[JsonStepCurrentConfig]
)

object JsonStepCurrentConfig {
  import org.shared.common.json.JsonComponent.jsonComponentFormat
  implicit val jsonStepCurrentConfigWrites: Writes[JsonStepCurrentConfig] = (
    (JsPath \ "stepId").write[String] and
    (JsPath \ "nameToShow").write[String] and
    (JsPath \ "components").write(Writes.list[JsonComponent](jsonComponentFormat)) and
    (JsPath \ "nextStep").lazyWrite(Writes.optionWithNull[JsonStepCurrentConfig](jsonStepCurrentConfigWrites))
  )(unlift(JsonStepCurrentConfig.unapply))

  implicit val jsonStepCurrentConfigReads: Reads[JsonStepCurrentConfig] = (
    (JsPath \ "stepId").read[String] and
      (JsPath \ "nameToShow").read[String] and
      (JsPath \ "components").read(Reads.list[JsonComponent](jsonComponentFormat)) and
      (JsPath \ "nextStep").lazyRead(Reads.optionWithNull[JsonStepCurrentConfig](jsonStepCurrentConfigReads))
    )(JsonStepCurrentConfig.apply _)
}