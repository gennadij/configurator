package org.dto.currentConfig

import play.api.libs.json.Json
import org.dto.Step

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class CurrentConfigResult (
    steps: List[Step]
)

object CurrentConfigResult {
  implicit val format = Json.writes[CurrentConfigResult]
}