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

case class JsonStartConfigOut (
    json: String = JsonNames.startConfig,
    result: JsonStartConfigResult
)

object JsonStartConfigOut {
  //TODO Impl Nullable Objects
  implicit val format = Json.writes[JsonStartConfigOut]
}