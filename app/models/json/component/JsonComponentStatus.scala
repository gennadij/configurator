package models.json.component

import models.json.common.JsonStatus
import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.01.2018
 */
case class JsonComponentStatus (
    selectionCriterium: JsonStatus,
    selectedComponent: JsonStatus,
    excludeDependency: JsonStatus,
    nextStepExistence: Boolean
)

object JsonComponentStatus {
  implicit val writerJsonComponentStatus = Json.writes[JsonComponentStatus]
}