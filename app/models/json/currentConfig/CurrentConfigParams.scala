package models.json.currentConfig

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class CurrentConfigParams (
    clientId: String
)
  
object CurrentConfigParams {
  implicit val format = Json.reads[CurrentConfigParams]
}