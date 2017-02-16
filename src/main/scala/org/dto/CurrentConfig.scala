package org.dto

import play.api.libs.json.Json

case class CurrentConfig (
    steps: Set[Step]
)

object CurrentConfig {
  implicit val format = Json.writes[CurrentConfig]
}