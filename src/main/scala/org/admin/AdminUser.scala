/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.admin

import play.api.libs.json.Json

case class AdminUser(  id: String,
                       name: String,
                       password: String,
                       authentication: Boolean)        //exist, not exist

object AdminUser {
  implicit val format = Json.format[AdminUser]
}