package models.json.component

import play.api.libs.json.Json
import models.json.common.JsonDependency

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentResult (
    selectedComponentId: String,
    stepId: String,
    status: JsonComponentStatus,
    dependencies: List[JsonDependency]
)


object JsonComponentResult{
   implicit val format = Json.writes[JsonComponentResult]
}