package org.shared.json.step

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 07.01.2019
 */
case class JsonStepIn (
                        json: String = JsonNames.STEP, // TODO Option
                        params: JsonStepParams
)

object JsonStepIn{
  implicit val format: Format[JsonStepIn] = Json.format[JsonStepIn]
}

