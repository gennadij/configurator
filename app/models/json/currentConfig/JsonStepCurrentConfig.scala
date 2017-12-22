package models.json.currentConfig

import models.json.common.JsonComponent
import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
case class JsonStepCurrentConfig (
    stepId: String,
    nameToShow: String,
    components: List[JsonComponent],
    nextStep: Option[JsonStepCurrentConfig]
)

object JsonStepCurrentConfig {
  implicit val format = Json.writes[JsonStepCurrentConfig]
}