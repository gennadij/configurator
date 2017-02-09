/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.startConfig

import play.api.libs.json.Json
import org.dto.Step

/**
 * Created by Gennadi Heimann 23.12.2016
 * 
 * result : {step: ...}
 */
case class StartConfigResult (
    status: Boolean,
    message: String,
    step: Step
)

object StartConfigResult {
  implicit val format = Json.writes[StartConfigResult]
}