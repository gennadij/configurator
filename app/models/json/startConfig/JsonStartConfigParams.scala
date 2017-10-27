package models.json.startConfig

import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * params : {configUri: http://config/test}
 */

case class JsonStartConfigParams (
    configUrl: String,
    clientId: String
)

object JsonStartConfigParams {
  implicit val format = Json.reads[JsonStartConfigParams]
}