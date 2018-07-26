package org.shared.component.json

import org.shared.common.JsonNames
import play.api.libs.json.{Json, Reads}

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
  implicit val format: Reads[JsonComponentIn] = Json.reads[JsonComponentIn]
}