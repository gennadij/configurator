package models.json

import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 23.12.2016
 * 
 * step: {id: #21:9, kind: "first" components: [... , ...]}
 * 
 */

case class Step (
    stepId: String,
    nameToShow: String,
    components: List[Component]
)

object Step {
  implicit val format = Json.writes[Step]
}