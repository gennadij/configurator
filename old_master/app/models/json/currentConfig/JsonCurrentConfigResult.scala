package models.json.currentConfig

import play.api.libs.json._
import models.json.common.JsonStep
import models.json.common.JsonComponent
import play.api.libs.functional.syntax._

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigResult (
    step: Option[JsonStepCurrentConfig]
)

object JsonCurrentConfigResult {
  import models.json.currentConfig.JsonStepCurrentConfig.jsonStepCurrentConfigWrites
  implicit val jsonCurrentConfigResultWrites: Writes[JsonCurrentConfigResult] = Json.writes[JsonCurrentConfigResult]
//    (
//      (JsPath \ "step").write(Writes.optionWithNull[JsonStepCurrentConfig])
//  )(unlift(JsonCurrentConfigResult.unapply))
}