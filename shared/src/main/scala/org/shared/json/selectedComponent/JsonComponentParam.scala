package org.shared.json.selectedComponent

import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentParam (
    componentId: String
)

object JsonComponentParam {
  implicit val format: Format[JsonComponentParam] = Json.format[JsonComponentParam]
}
