package org.shared.json.common

import play.api.libs.json.{Json, OFormat}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * step: {id: #21:9, kind: "first" components: [... , ...]}
 * 
 */

case class JsonStep (
    stepId: String,
    nameToShow: String,
    components: List[JsonComponent]
)

object JsonStep {
  implicit val format: OFormat[JsonStep] = Json.format[JsonStep]
}