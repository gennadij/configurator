/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.startConfig

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann 23.12.2016
 * 
 * params : {configUri: http://config/test}
 */

case class StartConfigParams (
    configUrl: String,
    clientId: String
)

object StartConfigParams {
  implicit val format = Json.reads[StartConfigParams]
}