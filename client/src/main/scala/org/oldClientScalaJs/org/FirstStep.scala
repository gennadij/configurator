//package org
//
//import org.scalajs.dom.raw.WebSocket
//import wrapper.StepIn
//import wrapper.StepStatus
//import wrapper.Status
//import org.scalajs.jquery.jQuery
//import org.scalajs.jquery.JQuery
//import wrapper.Component
//
///**
// * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
// * 
// * Created by Gennadi Heimann 03.03.2018
// */
//
//object FirstStep{
//  
//  def setFirstStep(socket: WebSocket, step: StepIn) = {
//    new FirstStep(socket, step).setStepArea
//  }
//}
//
//
//class FirstStep(socket: WebSocket, stepIn: StepIn) {
//  
//  private val s: WebSocket = socket
//  
//  private val classStep: String = "'step'"
//  private val numPattern = "[0-9]+".r
//  
//  private val stepId: String = stepIn.stepId
//  private val nameToShow: String = stepIn.nameToShow
//  private val components: List[Component] = stepIn.components
//  
//  private val statusFirstStep = getStatus(stepIn.status.firstStep)
//  private val statusNextStep = getStatus(stepIn.status.nextStep)
//  private val statusFatherStep = getStatus(stepIn.status.fatherStep)
//  private val statusCommon = getStatus(stepIn.status.common)
//  
//  private def setStepArea = {
//    val stepArea: JQuery = jQuery(createStepAreaHtmlDev).appendTo(jQuery("section"))
//    components.foreach(c => {
//	    val componentId: String = c.componentId
//	    val nameToShow: String = c.nameToShow
//      jQuery(createComponentHtml(componentId, nameToShow)).appendTo(stepArea)
//	    jQuery(s"#$componentId").on("click", () => selectComponent(socket, c.componentIdRow))
//    })
//  }
//  
//  private def createStepAreaHtmlDev: String =  {
//    s"<div id=$stepId class=$classStep> " + 
//      s" <p id=nameToShow_$stepId> ID: $nameToShow </p>" + 
//      s" <p id=message_$stepId> <b>StatusStep FirstStep:</b> $statusFirstStep </p>" +
//      s" <p id=message_$stepId> <b>StatusStep NextStep:</b> $statusNextStep </p>" +
//      s" <p id=message_$stepId> <b>StatusStep FatherStep:</b> $statusFatherStep </p>" +
//      s" <p id=message_$stepId> <b>StatusStep Common:</b> $statusCommon </p>" +
//      s"<div id=message_component_$stepId>" + 
//        s"<p id=message_component_1_$stepId> <b>Component Status SelectedComponent:</b> </p>" +
//        s"<p id=message_component_2_$stepId> <b>Component Status SelectionCriterium:</b> </p>" +
//        s"<p id=message_component_3_$stepId> <b>Component Status ExcludeDependency:</b> </p>" +
//        s"<p id=message_component_4_$stepId> <b>Component Status Common:</b> </p>" +
//      "</div>" +
//    s"</div> "
//  }
//  
//  private def getStatus(status: Option[Status]): String = status match {
//    case Some(status) => status.status
//    case None => ""
//  }
//  private def createComponentHtml(divId: String, nameToShow: String): String = {
//    s"<div id=$divId class='component'> <p> $nameToShow <p> </div>"
//  }
//  
//  private def selectComponent(socket: WebSocket, componentId: String) = {
//    val json: String = "{\"json\":\"Component\",\"params\":{\"componentId\":\"" + componentId + "\"}}"
//    println("Send => " + json)
//    socket.send(json)
//    
//    val jsonCurrentConfig = "{\"json\":\"CurrentConfig\"}"
//    println("Send => " + jsonCurrentConfig)
//    socket.send(jsonCurrentConfig)
//  }
//}