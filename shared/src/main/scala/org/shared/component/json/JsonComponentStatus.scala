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
  implicit val writerJsonComponentStatus: Writes[JsonComponentStatus] = (
    (JsPath \ "selectionCriterium").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "selectedComponent").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "excludeDependency").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "common").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "componentType").write(Writes.optionWithNull[JsonStatus])
  )(unlift(JsonComponentStatus.unapply))
}