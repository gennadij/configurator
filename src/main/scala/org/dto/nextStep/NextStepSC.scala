/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.nextStep

import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 27.12.20016
 */

case class NextStepSC (
    dtoId: Int = DTOIds.nextStep,
    dto: String = DTONames.nextSTep,
    result: NextStepResult
)

object NextStepSC {
  implicit val format = Json.writes[NextStepSC]
}