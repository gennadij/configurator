//package org.admin
//
//import play.api.libs.json._
//import play.api.libs.functional.syntax._
//import org.admin.configTree.AdminStep
//import org.admin.configTree.AdminComponent
//import org.persistence.db.orientdb.AdminUserVertex
//import org.admin.configTree.AdminConfigTree
//import org.admin.configTree.AdminConfigTreeStep
//import scala.collection.immutable.Seq
//
//trait AdminWeb {
//  
//  
//  /**
//   * 1. register => register Admin
//   *   Server <- Client
//   *   {"jsonId": 1, "method": "register", "params": {"username": "test", "password": "test"}}
//   *   Server -> Client
//   *   {"jsonId": 1, "method": "register", result": {"adminId": "AU#40:0", "username": "test"}}
//   * 2. => authenticate Admin
//   *   Server <- Client
//   *   {"jsonId": 2, "method": "autheticate", params: {"username": "test", "password": "test"}}
//   *   Server -> Client
//   *   {"jsonId": 2, "method": "autheticate", 
//   *       result: {"id": "AU#40:0", "username": "test", "password": "test", "authentication": true}}
//   * 3. => configTree
//   *   Server <- Client
//   *   {"jsonId": 3, "method": "configTree", params: {"adminId": "AU#40:0", "authentication": true}}
//   *   Server -> Client
//   *   {"jsonId": 3, "method": "configTree", result: {"steps":
//                  	[
//                  		{
//                  			"id":"#19:1","stepId":"#19:1","adminId":"AU#37:0","kind":"first",
//                  			"components":
//                  				[
//                  					{
//                  						"id":"#21:0","componentId":"C#21:0","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#17:2"
//                  					},
//                  					{
//                  						"id":"#22:0","componentId":"C#22:0","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#17:2"
//                  					},
//                  					{
//                  						"id":"#23:0","componentId":"C#23:0","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#17:2"
//                  					}
//                  				]
//                  		},
//                  		{
//                  			"id":"#17:2","stepId":"#17:2","adminId":"AU#37:0","kind":"default",
//                  			"components":
//                  				[
//                  					{
//                  						"id":"#23:1","componentId":"C#23:1","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#18:2"
//                  					},
//                  					{
//                  						"id":"#22:1","componentId":"C#22:1","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#18:2"
//                  					},
//                  					{
//                  						"id":"#21:1","componentId":"C#21:1","adminId":"AU#37:0","kind":"immutable","nextSteps":"NS#18:2"
//                  					}
//                  				]
//                  		}
//                  	]
//                  }}
//   * 4. => addFirstStep
//   *   Server <- Client
//   *   {"jsonId": 4, "method": "addFirstStep", "params": {"adminId": "AU#40:0", "kind": "immutable"}}
//   *   Server -> Client
//   *   {"jsonId": 4, "method": "addFirstStep", 
//   *     "result": {"id": "#12.1", "stepId": "S#12.1", "adminId": "AU#40:0", "kind": "first" }} 
//   * 5. => addComponent and connect Step with Component
//   *   Server <- Client
//   *   {"jsonId": 5, "method": "addComponent", "params": {"adminId": "AU#40:0", "kind": "immutable", "stepId": "#12:1"}
//   *    Server -> Client
//   *    {"jsonId": 5, "method": "addComponent", 
//   *      "result": {"id": "#13:1", "componentId": "C#13:1", "adminId": "AU#40:0", "kind": "immutable"}}
//   * 6. => addNextStep add connect Component with NextStep
//   *   Server <- Client
//   *   {"jsonId": 6, "method": "addNextStep", "params": {"adminId": "AU#40:0", "kind": "default", "componentId": "#13:1"}
//   *   Server -> Client
//   *    {"jsonId": 6, "method": "addNextStep", 
//   *      "result": {"id": "#14:1", "stepId": "S#14:1", "adminId": "AU#40:0", "kind": "default"}}
//   * 7. => updateStep update Step
//   * 8. => deleteStep delete Step and its hasComponent  (brauche zugehörige Step, was soll mit der weiterer ConfigTree passieren)
//   * 9. => updateComponent 
//   * 10. => deleteComponen
//   */
//  
//  def handelMessage(receivedMessage: JsValue): JsValue = {
//    (receivedMessage \ "method").toString() match {
//      case "autheticate" => autheticate(receivedMessage)
//      case "addFirstStep" => addFirstStep(receivedMessage)
//      case "configTree" => configTree(receivedMessage)
//      case "addComponent" => addComponent(receivedMessage)
//      case "addNextStep" => addNextStep(receivedMessage)
//    }
//  }
//  
//  private def autheticate(receivedMessage: JsValue): JsValue = {
//    val username = (receivedMessage \ "username").toString()
//    val password = (receivedMessage \ "password").toString()
//    val adminId = Admin.authenticate(username, password)
//    //TODO impl autentification
//    val adminUser = new AdminUser(adminId, username, password, true)
//    Json.obj(
//        "jsonId"-> 2, 
//        "method" -> "autheticate"
//        ,"result" -> Json.toJson(adminUser))
//  }
//  private def addFirstStep(receivedMessage: JsValue): JsValue = {
//    //TODO impl Reads with validation show 
//    // https://www.playframework.com/documentation/2.4.x/ScalaJsonCombinators
//    val adminId = (receivedMessage \ "adminId").toString()
//    val kind = (receivedMessage \ "kind").toString()
//    val step = Admin.addStep(new AdminStep("", "", adminId, kind))
//    Json.obj(
//        "jsonId"-> 2, 
//        "method" -> "autheticate"
//        ,"result" -> Json.toJson(step)
//        )
//  }
//  
//  private def addComponent(receivedMessage: JsValue): JsValue = {
//    val adminId = (receivedMessage \ "adminId").toString()
//    val kind = (receivedMessage \ "kind").toString()
//    val stepId = (receivedMessage \ "stepId").toString()
//    val component = Admin.addComponent(new AdminComponent("", "", adminId, kind))
//    val hasComponent = Admin.addHasComponent(adminId, stepId, component.id)
//    Json.toJson(component)
//  }
//  
//  private def addNextStep(receivedMessage: JsValue): JsValue = {
//    val adminId = (receivedMessage \ "adminId").toString()
//    val kind = (receivedMessage \ "kind").toString()
//    val componentId = (receivedMessage \ "componentId").toString()
//    val step = Admin.addStep(new AdminStep("", "", adminId, kind))
//    val nextStep = Admin.addNextStep(adminId, componentId, step.id)
//    Json.toJson(step)
//  }
//  
//  private def configTree(receivedMessage: JsValue): JsValue = {
//    val adminId = (receivedMessage \ "params" \ "adminId").toString()
//    val authentication = (receivedMessage \ "params" \ "authentication").toString().toBoolean
//    val steps = Admin.configTree(adminId)
//    Json.toJson(steps)
//  }
//  
////  implicit val adminStepWrites = new Writes[AdminStep] {
////    def writes(adminStep: AdminStep) = Json.obj(
////        "id" -> adminStep.id,
////        "stepId" -> adminStep.stepId
////        ,"adminId" -> adminStep.adminId
////        ,"kind" -> adminStep.kind
////      )
////  }
//  
////  implicit val adminComponentWrites = new Writes[AdminComponent] {
////    def writes(adminComponent: AdminComponent) = Json.obj(
////        "id" -> adminComponent.id,
////        "componentId" -> adminComponent.componentId
////        ,"adminId" -> adminComponent.adminId
////        ,"kind" -> adminComponent.kind
////      )
////  }
//  
////  implicit val adminUserWrites = new Writes[AdminUser] {
////    def writes(adminUser: AdminUser) = Json.obj(
////        "id" -> adminUser.id,
////        "username" -> adminUser.name
////        ,"password" -> adminUser.password
////        ,"status" -> adminUser.authentication
////      )
////  }
//}