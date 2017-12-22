package models.json.currentConfig

import play.api.libs.json.Json
import models.json.common.JsonStep
import models.json.common.JsonComponent

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigResult (
    step: Option[JsonStepCurrentConfig]
)

object JsonCurrentConfigResult {
  implicit val format = Json.writes[JsonCurrentConfigResult]
}