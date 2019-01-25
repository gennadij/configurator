package org.shared.json.selectedComponent

import org.shared.json.JsonKey
import org.shared.json.common.{JsonError, JsonWarning}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentResult (
    selectedComponentId: String,
    stepId: String,
    lastComponent: Boolean,
    status: JsonComponentStatus,
    excludeDependenciesOut: Option[List[JsonDependency]] = None,
    excludeDependenciesIn: Option[List[JsonDependency]] = None,
    requireDependenciesOut: Option[List[JsonDependency]] = None,
    requireDependenciesIn: Option[List[JsonDependency]] = None,
    info: Option[JsonSelectedComponentInfo],
    errors: Option[List[JsonError]] = None,
    warnings: Option[List[JsonWarning]] = None
)


object JsonComponentResult{
   implicit val writerJsonComponentResult: Format[JsonComponentResult] = (
     (JsPath \ JsonKey.selectedComponentId).format[String] and
     (JsPath \ JsonKey.stepId).format[String] and
     (JsPath \ JsonKey.lastComponent).format[Boolean] and
     (JsPath \ "status").format[JsonComponentStatus] and
     (JsPath \ JsonKey.excludeDependenciesOut).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.excludeDependenciesIn).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.requireDependenciesOut).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.requireDependenciesIn).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.info).format(Format.optionWithNull[JsonSelectedComponentInfo]) and
     (JsPath \ JsonKey.error).format(Format.optionWithNull[List[JsonError]]) and
     (JsPath \ JsonKey.warning).format(Format.optionWithNull[List[JsonWarning]])
   )(JsonComponentResult.apply, unlift(JsonComponentResult.unapply))
}