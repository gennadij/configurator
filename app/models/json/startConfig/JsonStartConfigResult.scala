package models.json.startConfig

import play.api.libs.json.Json
import models.json.common.JsonStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * result : {step: ...}
 */
case class JsonStartConfigResult (
    status: String,
    message: String,
    step: JsonStep
)

object JsonStartConfigResult {
  implicit val format = Json.writes[JsonStartConfigResult]
}