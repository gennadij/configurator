package org.admin

import play.api.libs.json.JsValue
import org.admin.configTree.AdminStep
import play.api.libs.json.Json
import play.api.libs.json.Writes
import org.admin.configTree.AdminComponent

trait AdminWeb {
  
  
  /**
   * 1. register => register Admin
   *   Server <- Client
   *   {"jsonId": 1, "method": "register", "params": {"username": "test", "password": "test"}}
   *   Server -> Client
   *   {"jsonId": 1, "method": "register", result": {"adminId": "AU#40:0", "username": "test"}}
   * 2. => authenticate Admin
   *   Server <- Client
   *   {"jsonId": 2, "method": "autheticate", params: {"username": "test", "password": "test"}}
   *   Server -> Client
   *   {"jsonId": 2, "method": "autheticate", result: {"authentication": true, "username": "test", "password": "test"}}
   * 3. => configTree
   *   Server <- Client
   *   {"jsonId": 3, "method": "configTree", params: {}}
   *   Server -> Client
   *   {"jsonId": 3, "method": "configTree", result: { TODO definieren}}
   * 4. => addFirstStep
   *   Server <- Client
   *   {"jsonId": 4, "method": "addFirstStep", "params": {"adminId": "AU#40:0", "kind": "immutable"}}
   *   Server -> Client
   *   {"jsonId": 4, "method": "addFirstStep", 
   *     "result": {"id": "#12.1", "stepId": "S#12.1", "adminId": "AU#40:0", "kind": "first" }} 
   * 5. => addComponent and connect Step with Component
   *   Server <- Client
   *   {"jsonId": 5, "method": "addComponent", "params": {"adminId": "AU#40:0", "kind": "immutable", "stepId": "#12:1"}
   *    Server -> Client
   *    {"jsonId": 5, "method": "addComponent", 
   *      "result": {"id": "#13:1", "componentId": "C#13:1", "adminId": "AU#40:0", "kind": "immutable"}}
   * 6. => addNextStep add connect Component with NextStep
   *   Server <- Client
   *   {"jsonId": 6, "method": "addNextStep", "params": {"adminId": "AU#40:0", "kind": "default", "componentId": "#13:1"}
   *   Server -> Client
   *    {"jsonId": 6, "method": "addNextStep", 
   *      "result": {"id": "#14:1", "stepId": "S#14:1", "adminId": "AU#40:0", "kind": "default"}}
   * 7. => updateStep update Step
   * 8. => deleteStep delete Step and its hasComponent  (brauche zugehörige Step, was soll mit der weiterer ConfigTree passieren)
   * 9. => updateComponent 
   * 10. => deleteComponen
   */
  
  def handelMessage(receivedMessage: JsValue): JsValue = {
    (receivedMessage \ "method").toString() match {
      case "autheticate" => autheticate(receivedMessage)
      case "addFirstStep" => addFirstStep(receivedMessage)
      case "addComponent" => addComponent(receivedMessage)
      case "addNextStep" => addNextStep(receivedMessage)
    }
  }
  
  private def autheticate(receivedMessage: JsValue): JsValue = {
    val username = (receivedMessage \ "username").toString()
    val password = (receivedMessage \ "password").toString()
    val adminId = Admin.authenticate(username, password)
    Json.toJson(Json.obj(""->""))
  }
  private def addFirstStep(receivedMessage: JsValue): JsValue = {
    //TODO impl Reads with validation show 
    // https://www.playframework.com/documentation/2.4.x/ScalaJsonCombinators
    val adminId = (receivedMessage \ "adminId").toString()
    val kind = (receivedMessage \ "kind").toString()
    val step = Admin.addStep(new AdminStep("", "", adminId, kind))
    Json.toJson(step)
  }
  
  private def addComponent(receivedMessage: JsValue): JsValue = {
    val adminId = (receivedMessage \ "adminId").toString()
    val kind = (receivedMessage \ "kind").toString()
    val stepId = (receivedMessage \ "stepId").toString()
    val component = Admin.addComponent(new AdminComponent("", "", adminId, kind))
    val hasComponent = Admin.addHasComponent(adminId, stepId, component.id)
    Json.toJson(component)
  }
  
  private def addNextStep(receivedMessage: JsValue): JsValue = {
    val adminId = (receivedMessage \ "adminId").toString()
    val kind = (receivedMessage \ "kind").toString()
    val componentId = (receivedMessage \ "componentId").toString()
    val step = Admin.addStep(new AdminStep("", "", adminId, kind))
    val nextStep = Admin.addNextStep(adminId, componentId, step.id)
    Json.toJson(step)
  }
  
  implicit val adminStepWrites = new Writes[AdminStep] {
    def writes(adminStep: AdminStep) = Json.obj(
        "id" -> adminStep.id,
        "stepId" -> adminStep.stepId
        ,"adminId" -> adminStep.adminId
        ,"kind" -> adminStep.kind
      )
  }
  
  implicit val adminComponentWrites = new Writes[AdminComponent] {
    def writes(adminComponent: AdminComponent) = Json.obj(
        "id" -> adminComponent.id,
        "componentId" -> adminComponent.componentId
        ,"adminId" -> adminComponent.adminId
        ,"kind" -> adminComponent.kind
      )
  }
}