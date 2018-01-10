package models.json.component

import models.json.common.JsonStatus
import play.api.libs.json._
import play.api.libs.functional.syntax._

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
    nextStepExistence: Option[Boolean]
)

object JsonComponentStatus {
  import models.json.common.JsonStatus.writerJsonStatus
  implicit val writerJsonComponentStatus: Writes[JsonComponentStatus] = (
    (JsPath \ "selectionCriterium").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "selectedComponent").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "excludeDependency").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "common").write(Writes.optionWithNull[JsonStatus]) and
    (JsPath \ "nextStepExistence").writeNullable[Boolean]
  )(unlift(JsonComponentStatus.unapply))
}