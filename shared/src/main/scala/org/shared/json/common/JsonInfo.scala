package org.shared.json.common

import play.api.libs.json.{Format, Json}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 25.01.2019
  */
case class JsonInfo (
                    message: String,
                    name: String,
                    code: String
                    )

object JsonInfo{
  implicit val format: Format[JsonInfo] = Json.format[JsonInfo]
}
