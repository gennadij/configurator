package org.dto.nextStep

import org.dto.Step
import play.api.libs.json.Json
import org.dto.CurrentConfig

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class NextStepResult (
    step: Step,
    currentConfig: CurrentConfig
)

object NextStepResult {
  implicit val format = Json.writes[NextStepResult]
}