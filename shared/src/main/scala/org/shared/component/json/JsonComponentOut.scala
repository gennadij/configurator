package org.shared.component.json

import org.shared.common.JsonNames
import play.api.libs.json.{Json, OWrites}

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
  implicit val format: OWrites[JsonComponentOut] = Json.writes[JsonComponentOut]
}