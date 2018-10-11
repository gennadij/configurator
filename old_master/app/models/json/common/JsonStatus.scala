package models.json.common

import play.api.libs.json.Json

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
  implicit val writerJsonStatus = Json.writes[JsonStatus]
}