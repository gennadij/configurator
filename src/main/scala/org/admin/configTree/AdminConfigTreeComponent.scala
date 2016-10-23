package org.admin.configTree

case class AdminConfigTreeComponent (
                      id: String,
                      componentId: String,
                      adminId: String,
                      //immutable, mutable
                      kind: String,
                      nextSteps: String
)