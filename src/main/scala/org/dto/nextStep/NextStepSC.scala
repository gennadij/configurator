/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.nextStep

import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json
import org.dto.Status

/**
 * Created by Gennadi Heimann on 27.12.20016
 */

case class NextStepSC (
    dtoId: Int = DTOIds.nextStep,
    dto: String = DTONames.nextSTep,
    status: Status,
    result: NextStepResult
)

object NextStepSC {
  implicit val format = Json.writes[NextStepSC]
}