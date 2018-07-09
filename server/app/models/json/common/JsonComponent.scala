package models.json.common

import play.api.libs.json._
import play.api.libs.functional.syntax._

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
  implicit val jsonComponentWrites: Writes[JsonComponent] = Json.writes[JsonComponent]
//    (
//      (JsPath \ "componentId").write[String] and
//      (JsPath \ "nameToShow").write[String]
//  )(unlift(JsonComponent.unapply))
}