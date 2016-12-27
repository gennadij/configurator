/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.nextStep

import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 27.12.2016
 */

case class NextStepParams (
    componentIds: List[String]
)

object NextStepParams {
  implicit val format = Json.reads[NextStepParams]
}