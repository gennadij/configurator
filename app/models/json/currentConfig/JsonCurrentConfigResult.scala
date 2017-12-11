package models.json.currentConfig

import play.api.libs.json.Json
import models.json.common.JsonStep

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigResult (
    steps: List[JsonStep]
)

object JsonCurrentConfigResult {
  implicit val format = Json.writes[JsonCurrentConfigResult]
}