package org.shared.json.selectedComponent

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonSelectedComponentIn(
                             json: String = JsonNames.SELECTED_COMPONENT,
                             params: JsonSelectedComponentParam
)

object JsonSelectedComponentIn{
  implicit val format: Format[JsonSelectedComponentIn] = Json.format[JsonSelectedComponentIn]
}