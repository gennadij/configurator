package org.shared.currentConfig.json

import play.api.libs.json._

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigResult (
    step: Option[JsonStepCurrentConfig]
)

object JsonCurrentConfigResult {
  implicit val jsonCurrentConfigResultWrites: Writes[JsonCurrentConfigResult] = Json.writes[JsonCurrentConfigResult]
//    (
//      (JsPath \ "step").write(Writes.optionWithNull[JsonStepCurrentConfig])
//  )(unlift(JsonCurrentConfigResult.unapply))
}