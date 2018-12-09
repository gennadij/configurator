package org.shared.json.nextStep

import org.shared.json.JsonNames
import play.api.libs.json.{Format, Json}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 27.12.2016
 */
case class JsonNextStepIn (
    json: String = JsonNames.NEXT_STEP
//    params: JsonNextStepParams
)

object JsonNextStepIn {
  implicit val format: Format[JsonNextStepIn] = Json.format[JsonNextStepIn]
}