package org.oldClientScalaJs.org.main

import org.oldClientScalaJs.org.{CurrentConfig, SelectedComponent, Step}
import org.oldClientScalaJs.wrapper.{ComponentStatus, StepIn, Wrapper}
import org.scalajs.dom
import org.scalajs.jquery.{JQuery, jQuery}

import scala.scalajs.js.{Dynamic, JSON}
import scala.util.matching.Regex

//import scala.collection.JavaConverters._


//noinspection ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses,ScalaUnnecessaryParentheses
object Main {
  val url = ??? //"ws://localhost:9000/configurator"
		  val socket = new dom.WebSocket(url)
  val numPattern: Regex = "[0-9]+".r
  
  def main_(args: Array[String]): Unit = {
    
//    val startPage = new StartPage
    
//    startPage.setStartPage(socket)
    
    socket.onmessage = {
      (e: dom.MessageEvent) => {
        println(e.data.toString())
        val message: Dynamic = JSON.parse(e.data.toString())
        message.json.toString() match {
          case "StartConfig" => {
            Step.setFirstStep(socket, Wrapper.jsonToStepIn(message.result))
          }
          case "Component" => {
            SelectedComponent.setSelectedComponent(
                socket, Wrapper.jsonToComponentIn(message.result))
          }
          case "NextStep" => {
            val stepIn: StepIn = Wrapper.jsonToStepIn(message.result)
            
//            Step.setStep(socket, stepIn)
            
            updateStatus(stepIn.stepId, null)
          }
          case "CurrentConfig" => {
            CurrentConfig.setCurrentConfig(message.result)
          }
        }
      }
    }
    
    socket.onopen = { (e: dom.Event) => {
        println("Websocket open")
        
        val startConfigIn: String = """{"json":"StartConfig","params":{"configUrl":"http://contig1/user29_v016"}}"""
    
        socket.send(startConfigIn)
      }
    }
    
    socket.onerror = {
      (e: dom.Event) => {
        println("Websocket error")
      }
    }
  }
  
  def updateStatus(stepId: String, status: ComponentStatus): JQuery = {
//    println("updateStatus")
//    val html: String = s"<div id='message_component_$stepId'> </div>"
    
//    jQuery(s"#").appendTo(jQuery(s"#$stepId")) 
    jQuery(s"#message_component_1_$stepId").length match {
      case 0 => {
//        println(jQuery(s"#message_component_1_$stepId").length)
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_1_$stepId> SelectedComponent: -> " + status.selectedComponent.get.status + " " + status.selectedComponent.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_2_$stepId> SelectionCriterium: -> " + status.selectionCriterium.get.status + " " + status.selectionCriterium.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_3_$stepId> ExcludeDependency: -> " + status.excludeDependency.get.status + " " + status.excludeDependency.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_4_$stepId> Common: -> "  + status.common.get.status + " " + status.common.get.message + " </p>"))
      }
      case _ => {
//        println(jQuery(s"#message_component_1_$stepId").length)
        jQuery(s"#message_component_1_$stepId").remove()
        jQuery(s"#message_component_2_$stepId").remove()
        jQuery(s"#message_component_3_$stepId").remove()
        jQuery(s"#message_component_4_$stepId").remove()
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_1_$stepId> SelectedComponent: -> "  + status.selectedComponent.get.status + " " + status.selectedComponent.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_2_$stepId> SelectionCriterium: -> "  + status.selectionCriterium.get.status + " " + status.selectionCriterium.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_3_$stepId> ExcludeDependency: -> "  + status.excludeDependency.get.status + " " + status.excludeDependency.get.message + " </p>"))
        jQuery(s"#message_component_$stepId").append(jQuery(s"<p id=message_component_4_$stepId> Common: -> "  + status.common.get.status + " " + status.common.get.message + " </p>"))
      }
    }
  }
  
//  def updateColorByComponent(componentId: String, color: String) = {
//    println("Farbe aendern - > " + color + "  " + componentId)
//    jQuery(s"#$componentId").css("background-color", color)
//  }
//  
//  def updateColorByExcludeComponents(componentIds: List[String], color: String) = {
//    componentIds foreach (c => {
//      jQuery(s"#$c").css("background-color", color)
//    })
//  }
  
  def requireNextStep(): Unit = {
    
    val json: String = "{\"json\":\"NextStep\",\"params\":{\"componentIds\":\"[]\"}}"
    println("Sende: " + json)
    socket.send(json)
  }
}




























//            status.selectedComponent.get.status match {
//              case "ERROR_COMPONENT" => {
//                updateStatus(componentIn.stepId, status)
//              }
//              case "REMOVED_COMPONENT" => {
//                updateStatus(componentIn.stepId, status)
//                println("Farbe aendern - > " + componentIn.selectedComponentId)
//                updateColorByComponent(componentIn.selectedComponentId, "#FFFFFF")
//              }
//              case "ADDED_COMPONENT" => {
//                status.selectionCriterium.get.status match {
//                  case "ALLOW_NEXT_COMPONENT" => {
//                    val stepId: String = componentIn.stepId
////                    status.nextStepExistence.get match {
////                      case true => {
////                        if (jQuery(s"#nextStep_$stepId").length == 0) {
////                          jQuery(s"<button id='nextStep_$stepId' type='button'>Naechsten Schritt laden</button>").appendTo(s"#$stepId")
////                          jQuery(s"#nextStep_$stepId").on("click", () => requireNextStep())
////                        }
////                      }
////                      case false => {
//////                        updateStatus(message.result.stepId.toString, "Die Konfiguration ist abgeschlossen")
////                        updateStatus(message.result.stepId.toString, status)
////                      }
////                    }
//                    
//                    val excludeComponents: List[String] = componentIn.dependencies.toList map (_.inId)
//                    
//                    updateColorByExcludeComponents(excludeComponents, "#FF69B4")
//                    
//                    updateColorByComponent(componentIn.selectedComponentId, "#00FA9A".toString)
//                    
//                    updateStatus(stepId, status)
//                  }
//                  case "REQUIRE_NEXT_STEP" => {
//                    val stepId: String = componentIn.stepId
////                    status.nextStepExistence.get match {
////                      case true => {
////                        if (jQuery(s"#nextStep_$stepId").length == 0) {
////                          jQuery(s"<button id='nextStep_$stepId' type='button'>Naechsten Schritt laden</button>").appendTo(s"#$stepId")
////                          jQuery(s"#nextStep_$stepId").on("click", () => requireNextStep())
////                        }
////                      }
////                      case false => {
//////                        updateStatus(message.result.stepId.toString, "Die Konfiguration ist abgeschlossen")
////                        updateStatus(message.result.stepId.toString, status)
////                      }
////                    }
//                    
//                    val selectedComponentId: String = numPattern.findAllIn(message.result.selectedComponentId.toString).toArray.mkString
//                    
//                    val excludeComponents: List[String] = componentIn.dependencies.toList map (_.inId)
//                    
//                    updateColorByExcludeComponents(excludeComponents, "#FF69B4")
//                    
//                    updateColorByComponent(selectedComponentId, "#00FA9A")
//                    
//                    updateStatus(stepId, status)
//                  }
////                  case "ERROR_COMPONENT" => {
////                    val stepId: String = componentIn.stepId
////                    updateStatus(stepId, status)
////                  }
//                  case "EXCLUDE_COMPONENT" => {
//                    val stepId: String = componentIn.stepId
//                    updateStatus(stepId, status)
//                  }
//                  case "FINAL_COMPONENT" => {
//                    val stepId: String = componentIn.stepId
//                    
//                    val selectedComponentId: String = numPattern.findAllIn(message.result.selectedComponentId.toString).toArray.mkString
//                    
//                    val excludeComponents: List[String] = componentIn.dependencies.toList map (_.inId)
//                    
//                    updateColorByExcludeComponents(excludeComponents, "#FF69B4")
//                    
//                    updateColorByComponent(selectedComponentId, "#00FA9A".toString)
//                    
//                    updateStatus(stepId, status)
//                  }
//                }
//              }
//            }