package org.shared.json.common

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
//      (JsPath \ JsonKey.componentId).write[String] and
//      (JsPath \ JsonKey.nameToShow).write[String]
//  )(unlift(JsonComponent.unapply))
}