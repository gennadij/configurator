package org.shared.json.step

import play.api.libs.json.{Json, OFormat}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.01.2019
  */
case class JsonComponent (
                         componentId: String,
                         nameToShow: String,
                         permissionToSelection: Boolean
                         )

object JsonComponent{
  implicit val format: OFormat[JsonComponent] = Json.format[JsonComponent]
}
