package org.admin.configTree

case class AdminComponent(
                      status: Boolean,
                      id: String,
                      componentId: String,
                      adminId: String,
                      //immutable, mutable
                      kind: String
                    )