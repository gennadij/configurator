package org.admin.configTree

case class AdminConfigTreeStep (
    id: String,
    stepId: String,
    adminId: String,
    kind: String,
    components: List[AdminComponent]
)