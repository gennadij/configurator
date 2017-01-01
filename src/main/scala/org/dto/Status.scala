/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 31.12.2016
 * 
 * 
 */

case class Status (
    kind: String,
    id: Int,
    message: String
)

object Status {
  implicit val format = Json.writes[Status]
}