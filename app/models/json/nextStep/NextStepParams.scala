package models.json.nextStep

import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */

case class NextStepParams (
    componentIds: List[String],
    clientId: String
)

object NextStepParams {
  implicit val format = Json.reads[NextStepParams]
}