/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.admin

case class AdminUser(  id: String,
                       name: String,
                       password: String,
                       authentication: Boolean)        //exist, not exist