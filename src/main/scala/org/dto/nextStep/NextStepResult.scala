/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
package org.dto.nextStep

import org.dto.Step
import play.api.libs.json.Json

/**
 * Created by Gennadi Heimann on 27.12.2016
 */

case class NextStepResult (
    configId: String,
    step: Step
)

object NextStepResult {
  implicit val format = Json.writes[NextStepResult]
}