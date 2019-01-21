package org.shared.json.step

import org.shared.json.JsonKey
import play.api.libs.json.{Format, JsPath, Json}
import play.api.libs.functional.syntax._

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
}
