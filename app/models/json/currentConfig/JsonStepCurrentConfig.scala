package models.json.currentConfig

import models.json.common.JsonComponent
import play.api.libs.json._
import play.api.libs.functional.syntax._

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
  import models.json.common.JsonComponent.jsonComponentWrites
  implicit val jsonStepCurrentConfigWrites: Writes[JsonStepCurrentConfig] = (
    (JsPath \ "stepId").write[String] and
    (JsPath \ "nameToShow").write[String] and
    (JsPath \ "components").write(Writes.list[JsonComponent](jsonComponentWrites)) and
    (JsPath \ "nextStep").lazyWrite(Writes.optionWithNull[JsonStepCurrentConfig](jsonStepCurrentConfigWrites))
  )(unlift(JsonStepCurrentConfig.unapply))
}