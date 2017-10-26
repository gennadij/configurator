package models.json.nextStep

import play.api.libs.json.Json
import models.json.Step

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class NextStepResult (
    step: Step
)

object NextStepResult {
  implicit val format = Json.writes[NextStepResult]
}