package org.dto

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann 23.12.2016
 * 
 * {id: #22:9, kind: immutable, nextStep: #27:11}
 */

case class Component (
    id: String,
    kind: String,
    nextStep: String
)

object Component {
  implicit val format = Json.writes[Component]
}