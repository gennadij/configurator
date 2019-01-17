package org.shared.json.common

import play.api.libs.json.{Format, Json}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonError (
                     message: String,
                     name: String,
                     code: String
                     )

object JsonError{
  implicit val format: Format[JsonError] = Json.format[JsonError]
}