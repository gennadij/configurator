//package org.admin.configTree
//
//import play.api.libs.json.Json
//
//case class AdminConfigTreeStep (
//    id: String,
//    stepId: String,
//    adminId: String,
//    kind: String,
//    components: List[AdminConfigTreeComponent]
//)
//
//
//object AdminConfigTreeStep {
//  implicit val format = Json.format[AdminConfigTreeStep]
//}