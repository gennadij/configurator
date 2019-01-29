package org.shared.json.step

import play.api.libs.json.{Json, OFormat}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Crated by Gennadi Heimann 07.01.2019
  */
case class JsonSelectionCriterion (
                                  min: Int,
                                  max: Int
                                  )

object JsonSelectionCriterion{
  implicit val format: OFormat[JsonSelectionCriterion] = Json.format[JsonSelectionCriterion]
}
