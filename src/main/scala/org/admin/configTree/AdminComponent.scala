/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.admin.configTree

case class AdminComponent(
                      id: String,
                      componentId: String,
                      adminId: String,
                      //immutable, mutable
                      kind: String
                    )