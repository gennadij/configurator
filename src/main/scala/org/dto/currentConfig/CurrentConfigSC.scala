package org.dto.currentConfig

import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 24.02.2017
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

case class CurrentConfigSC (
    dtoId: Int = DTOIds.CURRENT_CONFIG,
    dto: String = DTONames.CURRENT_CONFIG,
    result: CurrentConfigResult
)

object CurrentConfigSC {
  implicit val format = Json.writes[CurrentConfigSC]
}