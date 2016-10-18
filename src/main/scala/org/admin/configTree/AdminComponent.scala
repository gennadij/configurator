package org.admin.configTree

case class AdminComponent(
                      id: String,
                      adminId: String,
                      //immutable, mutable
                      kind: String
                    )