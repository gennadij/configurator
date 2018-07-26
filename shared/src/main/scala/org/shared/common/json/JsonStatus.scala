package org.shared.common.json

import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.01.2018
 */
case class JsonStatus (
    status: String,
    message: String
)

object JsonStatus {
  implicit val formatJsonStatus: OFormat[JsonStatus] = Json.format[JsonStatus]
}