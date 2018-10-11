package org.shared.common.json

import play.api.libs.json._

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
  implicit val jsonComponentFormat: Format[JsonComponent] = Json.format[JsonComponent]
//    (
//      (JsPath \ "componentId").write[String] and
//      (JsPath \ "nameToShow").write[String]
//  )(unlift(JsonComponent.unapply))
}