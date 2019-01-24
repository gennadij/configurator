package org.shared.json.step

import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 07.01.2019
 */

case class JsonStepParams (
    configUrl: Option[String] = None
)

object JsonStepParams{
  implicit val format: Format[JsonStepParams] = Json.format[JsonStepParams]
//  implicit val format2: Format[JsonStepParams] = (
//    (JsPath \ JsonKey.configUrl).format(Format.optionWithNull[String])
//  )(JsonStepParams.apply, unlift(JsonStepParams.unapply))
}
