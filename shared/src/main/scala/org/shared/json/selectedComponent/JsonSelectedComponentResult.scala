package org.shared.json.selectedComponent

import org.shared.json.JsonKey
import org.shared.json.common.JsonError
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonSelectedComponentResult(
                                        selectedComponentId: String,
                                        stepId: String,
                                        lastComponent: Boolean,
                                        addedComponent: Boolean,
                                        excludeDependenciesOut: Option[List[JsonDependency]] = None,
                                        excludeDependenciesIn: Option[List[JsonDependency]] = None,
                                        requireDependenciesOut: Option[List[JsonDependency]] = None,
                                        requireDependenciesIn: Option[List[JsonDependency]] = None,
                                        info: Option[JsonSelectedComponentInfo] = None,
                                        errors: Option[List[JsonError]] = None,
                                        warning: Option[JsonSelectedComponentWarning] = None
)


object JsonSelectedComponentResult{
   implicit val formatJsonComponentResult: Format[JsonSelectedComponentResult] = (
     (JsPath \ JsonKey.selectedComponentId).format[String] and
     (JsPath \ JsonKey.stepId).format[String] and
     (JsPath \ JsonKey.lastComponent).format[Boolean] and
     (JsPath \ JsonKey.addedComponent).format[Boolean] and
     (JsPath \ JsonKey.excludeDependenciesOut).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.excludeDependenciesIn).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.requireDependenciesOut).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.requireDependenciesIn).format(Format.optionWithNull[List[JsonDependency]]) and
     (JsPath \ JsonKey.info).format(Format.optionWithNull[JsonSelectedComponentInfo]) and
     (JsPath \ JsonKey.error).format(Format.optionWithNull[List[JsonError]]) and
     (JsPath \ JsonKey.warning).format(Format.optionWithNull[JsonSelectedComponentWarning])
   )(JsonSelectedComponentResult.apply, unlift(JsonSelectedComponentResult.unapply))
}