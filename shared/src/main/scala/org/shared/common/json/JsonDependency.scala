package org.shared.common.json

import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */
case class JsonDependency (
    outId: String,
    inId: String,
    dependencyType: String,
    visualization: String,
    nameToShow: String
)

object JsonDependency {
  implicit val format: Format[JsonDependency] = Json.format[JsonDependency]
}