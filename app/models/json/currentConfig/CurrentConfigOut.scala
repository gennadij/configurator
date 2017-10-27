package models.json.currentConfig

import play.api.libs.json.Json
import models.json.JsonNames

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class CurrentConfigOut (
    dto: String = JsonNames.CURRENT_CONFIG,
    result: CurrentConfigResult
)

object CurrentConfigOut {
  implicit val format = Json.writes[CurrentConfigOut]
}