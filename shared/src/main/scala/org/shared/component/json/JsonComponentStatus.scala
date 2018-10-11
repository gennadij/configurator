package org.shared.component.json

import org.shared.common.json.JsonStatus
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.01.2018
 */
case class JsonComponentStatus (
    selectionCriterium: Option[JsonStatus],
    selectedComponent: Option[JsonStatus],
    excludeDependency: Option[JsonStatus],
    common: Option[JsonStatus],
    componentType: Option[JsonStatus]
)

object JsonComponentStatus {
  implicit val writerJsonComponentStatus: Format[JsonComponentStatus] = (
    (JsPath \ "selectionCriterium").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "selectedComponent").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "excludeDependency").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "common").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "componentType").format(Format.optionWithNull[JsonStatus])
  )(JsonComponentStatus.apply, unlift(JsonComponentStatus.unapply))
}