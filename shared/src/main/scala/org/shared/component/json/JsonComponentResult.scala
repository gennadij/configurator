package org.shared.component.json

import org.shared.common.json.JsonDependency
import play.api.libs.json.{Json, OWrites}

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
   implicit val format: OWrites[JsonComponentResult] = Json.writes[JsonComponentResult]
}