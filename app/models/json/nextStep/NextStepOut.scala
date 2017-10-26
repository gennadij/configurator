package models.json.nextStep

import play.api.libs.json.Json
import models.json.JsonNames

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.20016
 */

case class NextStepOut (
    dto: String = JsonNames.nextSTep,
    status: String,
    result: NextStepResult
)

object NextStepOut {
  implicit val format = Json.writes[NextStepOut]
}