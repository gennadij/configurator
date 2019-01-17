package org.shared.json.step

import org.shared.json.JsonNames
import play.api.libs.json.{Json, OFormat}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonStepOut (
                         json: String = JsonNames.STEP,
                         result: JsonStepResult
)

object JsonStepOut {
  implicit val format: OFormat[JsonStepOut] = Json.format[JsonStepOut]
}