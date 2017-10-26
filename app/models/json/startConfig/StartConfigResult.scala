package models.json.startConfig

import play.api.libs.json.Json
import models.json.Step

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
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