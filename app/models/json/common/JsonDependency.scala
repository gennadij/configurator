package models.json.common

import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */
case class JsonDependency (
    fromId: String,
    toId: String,
    dependencyId: String,
    dependencyType: String,
    visualization: String,
    nameToShow: String,
    status: String,
    message: String
)

object JsonDependency {
  implicit val format = Json.writes[JsonDependency]
}