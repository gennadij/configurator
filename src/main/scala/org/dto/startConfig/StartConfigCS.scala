package org.dto.startConfig

import org.dto.DTOIds
import org.dto.DTONames
import com.sun.org.glassfish.external.arc.Stability
import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 23.12.2016
 * 
 * {dtoId : 1, dto : StartConfig, params : ...}
 */
case class StartConfigCS (
    dtoId: Int = DTOIds.startConfig,
    dto: String = DTONames.startConfig,
    params: StartConfigParams
)

object StartConfigCS {
  implicit val format = Json.reads[StartConfigCS]
}