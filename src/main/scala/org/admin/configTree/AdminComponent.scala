package org.admin.configTree

case class AdminComponent(
                      id: String,
                      componentId: String,
                      adminId: String,
                      //immutable, mutable
                      kind: String,
                      nextSteps: String
                    )