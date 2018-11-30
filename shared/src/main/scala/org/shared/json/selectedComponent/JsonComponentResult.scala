package org.shared.json.selectedComponent

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
    status: JsonComponentStatus,
    excludeDependenciesOut: Option[List[JsonDependency]] = None,
    excludeDependenciesIn: Option[List[JsonDependency]] = None,
    requireDependenciesOut: Option[List[JsonDependency]] = None,
    requireDependenciesIn: Option[List[JsonDependency]] = None
)


object JsonComponentResult{
   implicit val writerJsonComponentResult: Format[JsonComponentResult] = (
     (JsPath \ "selectedComponentId").format[String] and
       (JsPath \ "stepId").format[String] and
       (JsPath \ "status").format[JsonComponentStatus] and
       (JsPath \ "excludeDependenciesOut").format(Format.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "excludeDependenciesIn").format(Format.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "requireDependenciesOut").format(Format.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "requireDependenciesIn").format(Format.optionWithNull[List[JsonDependency]])
     )(JsonComponentResult.apply, unlift(JsonComponentResult.unapply))
}