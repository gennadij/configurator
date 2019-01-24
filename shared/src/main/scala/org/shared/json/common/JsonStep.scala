package org.shared.json.common

import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 */

//TODO TO DELETE

case class JsonStep (
    stepId: String,
    nameToShow: String,
    components: List[JsonComponent]
)

object JsonStep {
  implicit val format: OFormat[JsonStep] = Json.format[JsonStep]
}