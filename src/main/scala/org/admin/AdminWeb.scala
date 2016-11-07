package org.admin

import play.api.libs.json.JsValue
import org.admin.configTree.AdminStep
import play.api.libs.json.Json
import play.api.libs.json.Writes
import org.admin.configTree.AdminComponent

trait AdminWeb {
  
  
  /**
   * 001 => register Admin 
   * 002 => authenticate Admin
   * 003 => configTree
   * 004 => addStep
   * 005 => addComponent and connect Step with Component
   * 006 => add NextStep add connect Component with NextStep
   * 
   */
  
  def handelMessage(receivedMessage: JsValue): JsValue = {
    (receivedMessage \ "method").toString() match {
      case "004" => addStep(receivedMessage)
      case "002" => addComponent(receivedMessage)
    }
  }
  
  
  private def addStep(receivedMessage: JsValue): JsValue = {
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