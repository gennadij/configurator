package org.oldClientScalaJs.wrapper

import scala.scalajs.js.Dynamic
import scala.scalajs._
import scala.util.matching.Regex

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
object Wrapper {
  
  val numPattern: Regex = "[0-9]+".r
  
  def jsonToStepIn(jsonResult: Dynamic): StepIn = {
    val components: js.Array[Component] = 
      jsonResult.step.components.asInstanceOf[js.Array[Dynamic]] map (c => {
        val componentId = numPattern.findAllIn(c.componentId.toString).toArray.mkString
        
        Component(componentId, c.componentId.toString, c.nameToShow.toString)
    })
    
    val stepId: String = numPattern.findAllIn(jsonResult.step.stepId.toString).toArray.mkString
    
    val status = StepStatus(
        jsonResult.status.firstStep match {
          case _ : Dynamic => Some(Status(
              jsonResult.status.firstStep.status.toString,
              jsonResult.status.firstStep.message.toString
          ))
          case null => None
        },
        jsonResult.status.nextStep match {
          case _ : Dynamic => Some(Status(
              jsonResult.status.nextStep.status.toString,
              jsonResult.status.nextStep.message.toString
          ))
          case null => None
        },
        jsonResult.status.fatherStep match {
          case _ : Dynamic => Some(Status(
              jsonResult.status.fatherStep.status.toString,
              jsonResult.status.fatherStep.message.toString
          ))
          case null => None
          
        },
        jsonResult.status.common match {
          case _ : Dynamic=> Some(Status(
              jsonResult.status.common.status.toString,
              jsonResult.status.common.message.toString
          ))
          case null => None
          
        }
    )
    StepIn(
        status,
        stepId,
        jsonResult.step.stepId.toString,
        jsonResult.step.nameToShow.toString,
        components.toList
    )
    
  }
  
  def ComponentOutToJson(componentOut: ComponentOut): String = {
    "{\"json\":\"Component\",\"params\":{\"componentId\":\"" + componentOut.componentId + "\"}}"
  }
  
  def jsonToComponentIn(jsonResult: Dynamic): ComponentIn = {
    
    val selectedComponentId: String = numPattern.findAllIn(jsonResult.selectedComponentId.toString).toArray.mkString
    
    val stepId: String = numPattern.findAllIn(jsonResult.stepId.toString).toArray.mkString
    
    val dependencies: js.Array[Dependency] = 
      jsonResult.dependencies.asInstanceOf[js.Array[Dynamic]] map (d => {
        val outId: String = numPattern.findAllIn(d.outId.toString).toArray.mkString
        val inId: String = numPattern.findAllIn(d.inId.toString).toArray.mkString
        Dependency(
            outId,
            d.outId.toString,
            inId,
            d.inId.toString,
            d.dependencyType.toString,
            d.visualization.toString,
            d.nameToShow.toString
        )
    })
    
    ComponentIn(
        selectedComponentId,
        stepId,
        jsonToStatusIn(jsonResult.status),
        dependencies.toList
    )
  }
  
  def requireNextStep: String = {
    "{\"json\":\"NextStep\",\"params\":{\"componentIds\":\"[]\"}}"
  }
  
  def jsonToCurrentConfigIn(jsonResult: Dynamic): CurrentConfigIn = {
    
    CurrentConfigIn(
        step = jsonResult.step match {
          case _: Dynamic => {
            getJsonCurrentStepRecursiv(jsonResult.step)
          }
          case null => None
        }
    )
  }
  
  
  def getJsonCurrentStepRecursiv(step: Dynamic): Option[StepCurrentConfig] = {
    step match {
      case nextStep: Dynamic => {
        Some(StepCurrentConfig(
            nextStep.stepId.toString,
            nextStep.nameToShow.toString,
            (nextStep.components.asInstanceOf[js.Array[Dynamic]] map (c => {
              Component(
                  c.componentId.toString,
                  "",
                  c.nameToShow.toString
              )
            })).toList,
            getJsonCurrentStepRecursiv(nextStep.nextStep)
        ))
      }
      case null => None
    }
  }
  
  def jsonToStatusIn(status: Dynamic): ComponentStatus = {
    ComponentStatus(
        status.selectionCriterium match {
          case _ : Dynamic => Some(Status(
              status.selectionCriterium.status.toString,
              status.selectionCriterium.message.toString
          ))
          case null => None
        },
        status.selectedComponent match {
          case _ : Dynamic => Some(Status(
              status.selectedComponent.status.toString,
              status.selectedComponent.message.toString
          ))
          case null => None
        },
        status.excludeDependency match {
          case _ : Dynamic => Some(Status(
              status.excludeDependency.status.toString,
              status.excludeDependency.message.toString
          ))
          case null => None
        },
        status.common match {
          case _ : Dynamic => Some(Status(
              status.common.status.toString,
              status.common.message.toString
          ))
          case null => None
        },
        status.componentType match {
          case _ : Dynamic => Some(Status(
              status.componentType.status.toString,
              status.componentType.message.toString
          ))
          case null => None
        }
    )
  }
}