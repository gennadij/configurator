package org.oldClientScalaJs.org

import org.oldClientScalaJs.util.CommonFunction
import org.oldClientScalaJs.wrapper.{Component, StepIn}
import org.scalajs.dom.raw.WebSocket
import org.scalajs.jquery.{JQuery, jQuery}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 18.12.2017
 */
object Step {
  
  def setFirstStep(socket: WebSocket, step: StepIn): Unit = {
    new Step(socket, step).setStepArea
  }
//  val classStep: String = "'step'"
//  val numPattern = "[0-9]+".r
//  
//  def setStep(socket: WebSocket, stepIn: StepIn) = {
    
//    val area: JQuery = addStep(stepIn)
//    
//    stepIn.components.foreach(c => {
//	    val componentId: String = c.componentId
//	    val nameToShow: String = c.nameToShow
//      jQuery(createComponentHtml(componentId, nameToShow)).appendTo(area)
//	    jQuery(s"#$componentId").on("click", () => selectComponent(socket, c.componentIdRow))
//	  })
    
//  }
  
//  def addStep(stepIn: StepIn): JQuery = {
//	  jQuery(createStepHtml(stepIn)).appendTo(jQuery("section"))
//  }
  
//  def createStepHtml(stepIn: StepIn): String = {
//    val stepId: String = stepIn.stepId
//    val nameToShow: String = stepIn.nameToShow
//    val message: String = stepIn.message
//    s"<div id=$stepId class=$classStep> " + 
//      s" <p id=nameToShow_$stepId> ID: $nameToShow </p>" + 
//      s" <p id=message_$stepId> StatusStep: $message </p>" +
//      s"<div id=message_component_$stepId>" + 
//        s"<p id=message_component_1_$stepId> SelectedComponent: </p>" +
//        s"<p id=message_component_2_$stepId> SelectionCriterium: </p>" +
//        s"<p id=message_component_3_$stepId> ExcludeDependency: </p>" +
//        s"<p id=message_component_4_$stepId> Common: </p>" +
//      "</div>" +
//    s"</div> "
//  }
  
//  def createComponentHtml(divId: String, nameToShow: String): String = {
//    s"<div id=$divId class='component'> <p> $nameToShow <p> </div>"
//  }
//  
//  def selectComponent(socket: WebSocket, componentId: String) = {
//    val json: String = "{\"json\":\"Component\",\"params\":{\"componentId\":\"" + componentId + "\"}}"
//    println("Send => " + json)
//    socket.send(json)
//    
//    val jsonCurrentConfig = "{\"json\":\"CurrentConfig\"}"
//    println("Send => " + jsonCurrentConfig)
//    socket.send(jsonCurrentConfig)
//  }
}

class Step(socket: WebSocket, stepIn: StepIn) {
  
  private val s: WebSocket = socket
  
  private val classStep: String = "'step'"
  private val numPattern = "[0-9]+".r
  
  private val stepId: String = stepIn.stepId
  private val nameToShow: String = stepIn.nameToShow
  private val components: List[Component] = stepIn.components
  
  private val statusFirstStep = CommonFunction.getStatus(stepIn.status.firstStep)
  private val statusNextStep = CommonFunction.getStatus(stepIn.status.nextStep)
  private val statusFatherStep = CommonFunction.getStatus(stepIn.status.fatherStep)
  private val statusCommon = CommonFunction.getStatus(stepIn.status.common)
  
  private def setStepArea(): Unit = {
    val stepArea: JQuery = jQuery(createStepAreaHtmlDev).appendTo(jQuery("section"))
    components.foreach(c => {
	    val componentId: String = c.componentId
	    val nameToShow: String = c.nameToShow
      jQuery(createComponentHtml(componentId, nameToShow)).appendTo(stepArea)
	    jQuery(s"#$componentId").on("click", () => selectComponent(socket, c.componentIdRow))
    })
  }
  
  private def createStepAreaHtmlDev: String =  {
    s"<div id=$stepId class=$classStep> " + 
      s" <p id=nameToShow_$stepId> ID: $nameToShow </p>" + 
      s" <p id=message_$stepId> <b>StatusStep FirstStep:</b> $statusFirstStep </p>" +
      s" <p id=message_$stepId> <b>StatusStep NextStep:</b> $statusNextStep </p>" +
      s" <p id=message_$stepId> <b>StatusStep FatherStep:</b> $statusFatherStep </p>" +
      s" <p id=message_$stepId> <b>StatusStep Common:</b> $statusCommon </p>" +
      s"<div id=message_component_$stepId>" + 
        s"<p id=message_component_1_$stepId> <b>Component Status SelectedComponent:</b> </p>" +
        s"<p id=message_component_2_$stepId> <b>Component Status SelectionCriterium:</b> </p>" +
        s"<p id=message_component_3_$stepId> <b>Component Status ExcludeDependency:</b> </p>" +
        s"<p id=message_component_4_$stepId> <b>Component Status Common:</b> </p>" +
      "</div>" +
    s"</div> "
  }
  

  
  private def createComponentHtml(divId: String, nameToShow: String): String = {
    s"<div id=$divId class='component'> <p> $nameToShow <p> </div>"
  }
  
  private def selectComponent(socket: WebSocket, componentId: String): Unit = {
    val json: String = "{\"json\":\"Component\",\"params\":{\"componentId\":\"" + componentId + "\"}}"
    println("Send => " + json)
    socket.send(json)
    
    val jsonCurrentConfig = "{\"json\":\"CurrentConfig\"}"
    println("Send => " + jsonCurrentConfig)
    socket.send(jsonCurrentConfig)
  }
}
