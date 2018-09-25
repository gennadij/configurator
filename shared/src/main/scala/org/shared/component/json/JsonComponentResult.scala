package org.shared.component.json

import org.shared.common.json.JsonDependency
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

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
   implicit val writerJsonComponentResult: Writes[JsonComponentResult] = (
     (JsPath \ "selectedComponentId").write[String] and
       (JsPath \ "stepId").write[String] and
       (JsPath \ "status").write[JsonComponentStatus] and
       (JsPath \ "excludeDependenciesOut").write(Writes.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "excludeDependenciesIn").write(Writes.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "requireDependenciesOut").write(Writes.optionWithNull[List[JsonDependency]]) and
       (JsPath \ "requireDependenciesIn").write(Writes.optionWithNull[List[JsonDependency]])
     )(unlift(JsonComponentResult.unapply))
}