package org.shared.json.selectedComponent

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonSelectedComponentOut(
                              json: String = JsonNames.SELECTED_COMPONENT,
                              result: JsonSelectedComponentResult
)

object JsonSelectedComponentOut {
  implicit val format: Format[JsonSelectedComponentOut] = Json.format[JsonSelectedComponentOut]
}