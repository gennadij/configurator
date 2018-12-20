package org.shared.json.selectedComponent

import org.shared.json.common.JsonStatus
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.01.2018
 */
case class JsonComponentStatus (
                                 selectionCriterion: Option[JsonStatus],
                                 selectedComponent: Option[JsonStatus],
                                 excludeDependencyInternal: Option[JsonStatus],
                                 excludeDependencyExternal: Option[JsonStatus],
                                 common: Option[JsonStatus],
                                 componentType: Option[JsonStatus]
)

object JsonComponentStatus {
  implicit val writerJsonComponentStatus: Format[JsonComponentStatus] = (
    (JsPath \ "selectionCriterion").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "selectedComponent").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "excludeDependencyInternal").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "excludeDependencyExternal").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "common").format(Format.optionWithNull[JsonStatus]) and
    (JsPath \ "componentType").format(Format.optionWithNull[JsonStatus])
  )(JsonComponentStatus.apply, unlift(JsonComponentStatus.unapply))
}