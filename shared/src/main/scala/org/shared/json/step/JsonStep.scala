package org.shared.json.step

import play.api.libs.json.{Json, OFormat}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonStep (
                    stepId: String,
                    nameToShow: String,
                    selectionCriterion: JsonSelectionCriterion
                    )

object JsonStep {
  implicit val format: OFormat[JsonStep] = Json.format[JsonStep]
}