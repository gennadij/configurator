package org.shared.currentConfig.json

import org.shared.common.JsonNames
import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class JsonCurrentConfigIn (
    json: String = JsonNames.CURRENT_CONFIG
)

object JsonCurrentConfigIn {
  implicit val format = Json.reads[JsonCurrentConfigIn]
}