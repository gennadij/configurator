/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.admin

case class AdminUser(  id: String,
                       name: String,
                       password: String,
                       status: String)        //exist, not exist