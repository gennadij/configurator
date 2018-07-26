package org.shared.component.json

import play.api.libs.json.{Json, Reads}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentParam (
    componentId: String
)

object JsonComponentParam {
  implicit val format: Reads[JsonComponentParam] = Json.reads[JsonComponentParam]
}
