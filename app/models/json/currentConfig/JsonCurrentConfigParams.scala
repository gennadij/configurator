package models.json.currentConfig

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigParams (
    clientId: String
)
  
object JsonCurrentConfigParams {
  implicit val format = Json.reads[JsonCurrentConfigParams]
}