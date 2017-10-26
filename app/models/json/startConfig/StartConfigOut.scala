package models.json.startConfig

import play.api.libs.json.Json
import models.json.JsonNames

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, result : ...}
 */

case class StartConfigOut (
    dto: String = JsonNames.startConfig,
    result: StartConfigResult
)

object StartConfigOut {
  implicit val format = Json.writes[StartConfigOut]
}