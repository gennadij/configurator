package org.shared.json.selectedComponent

import org.shared.json.common.JsonWarning
import play.api.libs.json.{Format, Json}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 25.01.2019
  */
case class JsonSelectedComponentWarning (
                                          excludedComponentExternal: Option[JsonWarning] = None,
                                          excludedComponentInternal: Option[JsonWarning] = None,
                                          excludeComponentExternal: Option[JsonWarning] = None,
                                          excludeComponentInternal: Option[JsonWarning] = None
                                        )

object JsonSelectedComponentWarning {
  implicit val formatter: Format[JsonSelectedComponentWarning] = Json.format[JsonSelectedComponentWarning]
}

