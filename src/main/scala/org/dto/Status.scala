package org.dto

import play.api.libs.json.Json

case class Status (
    kind: String,
    id: Int,
    message: String
)

object Status {
  implicit val format = Json.writes[Status]
}