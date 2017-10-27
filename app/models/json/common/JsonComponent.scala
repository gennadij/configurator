package models.json.common

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann 23.12.2016
 * 
 * {id: #22:9, kind: immutable, nextStep: #27:11}
 */

case class JsonComponent (
    componentId: String,
    nameToShow: String
)

object JsonComponent {
  implicit val format = Json.writes[JsonComponent]
}