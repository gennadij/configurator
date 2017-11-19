package models.json.component

import play.api.libs.json.Json

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class JsonComponentResult (
    components: List[String]
)


object JsonComponentResult{
   implicit val format = Json.writes[JsonComponentResult]
}