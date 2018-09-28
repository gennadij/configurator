package org.shared.component.json

import org.shared.common.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentIn (
    json: String = JsonNames.COMPONENT,
    params: JsonComponentParam
)

object JsonComponentIn{
  implicit val format: Format[JsonComponentIn] = Json.format[JsonComponentIn]
}