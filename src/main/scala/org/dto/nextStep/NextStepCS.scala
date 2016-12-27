/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.nextStep

import org.dto.DTOIds
import org.dto.DTONames
import play.api.libs.json.Json
/**
 * Created by Gennadi Heimann on 27.12.2016
 * 
 * 
 */
case class NextStepCS (
    dtoId: Int = DTOIds.nextStep,
    dto: String = DTONames.nextSTep,
    params: NextStepParams
)

object NextStepCS {
  implicit val format = Json.reads[NextStepCS]
}