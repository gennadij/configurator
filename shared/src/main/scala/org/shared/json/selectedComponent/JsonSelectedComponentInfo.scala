package org.shared.json.selectedComponent

import org.shared.json.common.JsonInfo
import play.api.libs.json.{Format, Json}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 25.01.2019
  */
case class JsonSelectedComponentInfo (
                                     selectionCriterion: Option[JsonInfo] = None
                                     )

object JsonSelectedComponentInfo {
  implicit val format : Format[JsonSelectedComponentInfo] = Json.format[JsonSelectedComponentInfo]
}