package org.shared.json.selectedComponent

import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonSelectedComponentParam(
    componentId: String
)

object JsonSelectedComponentParam {
  implicit val format: Format[JsonSelectedComponentParam] = Json.format[JsonSelectedComponentParam]
}
