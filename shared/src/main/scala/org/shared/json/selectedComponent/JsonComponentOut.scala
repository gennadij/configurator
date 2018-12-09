package org.shared.json.selectedComponent

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentOut (
    json: String = JsonNames.COMPONENT,
    result: JsonComponentResult
)

object JsonComponentOut {
  implicit val format: Format[JsonComponentOut] = Json.format[JsonComponentOut]
}