package models.json.nextStep

import play.api.libs.json.Json
import models.json.JsonNames

/**
 * Created by Gennadi Heimann on 27.12.2016
 * 
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */
case class JsonNextStepIn (
    dto: String = JsonNames.nextSTep,
    params: JsonNextStepParams
)

object JsonNextStepIn {
  implicit val format = Json.reads[JsonNextStepIn]
}