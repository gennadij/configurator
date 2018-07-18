package org.oldClientScalaJs.org

import org.oldClientScalaJs.util.CommonFunction
import org.oldClientScalaJs.wrapper.ComponentIn
import org.scalajs.dom.raw.WebSocket
import org.scalajs.jquery.jQuery

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.03.2018
 */

object SelectedComponent{
  def setSelectedComponent(socket: WebSocket, selectedComponentIn: ComponentIn) = {
//    new SelectedComponent(socket, selectedComponentIn).setSelectedComponent
    new SelectedComponent(socket, selectedComponentIn).setSelectedComponent
  }
}

class SelectedComponent(socket: WebSocket, selectedComponentIn: ComponentIn) {
  
  println(selectedComponentIn)
  
  private val statusSelectionCriterium: String = CommonFunction.getStatus(selectedComponentIn.status.selectionCriterium)
  private val statusSelectedComponent: String = CommonFunction.getStatus(selectedComponentIn.status.selectedComponent)
  private val statusExcludeDependency: String = CommonFunction.getStatus(selectedComponentIn.status.excludeDependency)
  private val statusCommon: String = CommonFunction.getStatus(selectedComponentIn.status.common)
  private val statusComponentType: String = CommonFunction.getStatus(selectedComponentIn.status.componentType)
  
  private val stepId: String = selectedComponentIn.stepId
  private val componentId: String = selectedComponentIn.selectedComponentId
  
  private val excludeComponents: List[String] = selectedComponentIn.dependencies.toList map (_.inId)
  
  
  private def setSelectedComponent1 = {
    
    //            SelectedComponent         SelectionCriterium      ExcludeDependency      statusCommon      ComponentType
		val case1 = ("ADDED_COMPONENT",       "ALLOW_NEXT_COMPONENT", "NOT_EXCLUDED_COMPONENT", "SUCCESS",   "DEFAULT_COMPONENT")
    val case2 = ("ADDED_COMPONENT",       "REQUIRE_NEXT_STEP",    "NOT_EXCLUDED_COMPONENT", "SUCCESS",   "DEFAULT_COMPONENT")
    val case3 = ("NOT_ALLOWED_COMPONENT", "REQUIRE_NEXT_STEP",    "EXCLUDED_COMPONENT",     "SUCCESS",   "DEFAULT_COMPONENT")
    val case4 = ("REMOVED_COMPONENT",     "REQUIRE_NEXT_STEP",    "NOT_EXCLUDED_COMPONENT", "SUCCESS",   "DEFAULT_COMPONENT")
		
    (statusSelectedComponent, 
        statusSelectionCriterium, 
        statusExcludeDependency, 
        statusCommon, statusComponentType) match {
      case `case1` => setCase1; println("case1" + case1)
      case `case2` => setCase2; println("case2" + case2)
      case `case3` => println("case3" + case3)
      case `case4` => println("case3" + case3)
      case _ => ""
    }
  }
  
  private def setCase1 = {
    updateHtmlNextStepButton
    updateColorByExcludeComponents("#FF69B4")
    updateColorByComponent("#00FA9A")
    updateHtmlStatus
  }
    
  private def setCase2 = {
    updateHtmlNextStepButton
    updateColorByExcludeComponents("#FF69B4")
    updateColorByComponent("#00FA9A")
    updateHtmlStatus
  }
  
  private def setCase3 = {
    updateHtmlStatus
  }
  
  private def setCase4 = {
    updateHtmlStatus
    updateColorByComponent("#FFFFFF")
    updateColorByExcludeComponents("#FF69B4")
  }
  
  private def setSelectedComponent = {
    statusCommon match {
      case "SUCCESS" => {
        statusSelectedComponent match {
          case "REMOVED_COMPONENT" => {
            updateHtmlStatus
            updateColorByComponent("#FFFFFF")
            updateColorByExcludeComponents("#FF69B4")
          }
          case "ADDED_COMPONENT" => {
            statusSelectionCriterium match {
              case "ALLOW_NEXT_COMPONENT" => setComponentType
              case "EXCLUDE_COMPONENT" => updateHtmlStatus
              case "REQUIRE_COMPONENT" => updateHtmlStatus
              case "REQUIRE_NEXT_STEP" => setComponentType
            }
          }
          case "ERROR_COMPONENT" => updateHtmlStatus
          case "NOT_ALLOWED_COMPONENT" => updateHtmlStatus
        }
      }
      case "ERROR" => updateHtmlStatus
      case "CLASS_CAST_ERROR" => updateHtmlStatus
      case "ODB_READE_ERROR" => updateHtmlStatus
    }
  }
  

  
  
  private def setComponentType = {
    statusComponentType match {
      case "DEFAULT_COMPONENT" => {
        updateHtmlNextStepButton
        updateColorByExcludeComponents("#FF69B4")
        updateColorByComponent("#00FA9A")
        updateHtmlStatus
      }
      case "FINAL_COMPONENT" => {
        updateColorByExcludeComponents("#FF69B4")
        updateColorByComponent("#00FA9A")
        updateHtmlStatus
      }
      case "ERROR_COMPONENT" => {
        updateHtmlStatus
      }
      case _ => println("match statuscomponentType undefined")
    }
  }
  
  private def updateHtmlStatus = {
    jQuery(s"#message_component_1_$stepId").length match {
      case 0 => {
        createHtmlComponentStatus
      }
      case _ => {
        deleteHtmlComponentStatus
        createHtmlComponentStatus
      }
    }
  }
  
  private def createHtmlComponentStatus = {
    jQuery(s"#message_component_$stepId").
      append(jQuery(s"<p id=message_component_1_$stepId> <b>Component Status ComponentType:</b> " + statusComponentType + "</p>"))
    jQuery(s"#message_component_$stepId").
      append(jQuery(s"<p id=message_component_2_$stepId> <b>Component Status SelectedComponent:</b> " + statusSelectedComponent + "</p>"))
    jQuery(s"#message_component_$stepId").
      append(jQuery(s"<p id=message_component_3_$stepId> <b>Component Status SelectionCriterium:</b> " + statusSelectionCriterium + " </p>"))
    jQuery(s"#message_component_$stepId").
      append(jQuery(s"<p id=message_component_4_$stepId> <b>Component Status ExcludeDependency:</b> " + statusExcludeDependency + " </p>"))
    jQuery(s"#message_component_$stepId")
      .append(jQuery(s"<p id=message_component_5_$stepId> <b>Component Status Common:</b> "  + statusCommon + " </p>"))
  }
  
  private def deleteHtmlComponentStatus = {
    jQuery(s"#message_component_1_$stepId").remove()
    jQuery(s"#message_component_2_$stepId").remove()
    jQuery(s"#message_component_3_$stepId").remove()
    jQuery(s"#message_component_4_$stepId").remove()
    jQuery(s"#message_component_5_$stepId").remove()
  }
  
  def updateColorByComponent(color: String) = {
    jQuery(s"#$componentId").css("background-color", color)
  }
  
  def updateColorByExcludeComponents(color: String) = {
    excludeComponents foreach (c => {
      jQuery(s"#$c").css("background-color", color)
    })
  }
  
  def updateHtmlNextStepButton = {
    if (jQuery(s"#nextStep_$stepId").length == 0) {
      jQuery(s"<button id='nextStep_$stepId' type='button'>Naechsten Schritt laden</button>").appendTo(s"#$stepId")
      jQuery(s"#nextStep_$stepId").on("click", () => requireNextStep())
    }
  }
  
  def requireNextStep() = {
    val json: String = "{\"json\":\"NextStep\",\"params\":{}}"
    println("Sende: " + json)
    socket.send(json)
  }
  
}