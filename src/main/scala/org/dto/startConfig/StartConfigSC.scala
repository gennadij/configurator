/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.startConfig

import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, result : ...}
 */

case class StartConfigSC (
    dtoId: Int = DTOIds.startConfig,
    dto: String = DTONames.startConfig,
    result: StartConfigResult
)

object StartConfigSC {
  implicit val format = Json.writes[StartConfigSC]
}