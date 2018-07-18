package org.oldClientScalaJs.org

import org.oldClientScalaJs.wrapper.{CurrentConfigIn, StepCurrentConfig, Wrapper}
import org.scalajs.jquery.jQuery

import scala.scalajs.js.Dynamic

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 02.01.2018
 */
object CurrentConfig {
  
  
  def setCurrentConfig(jsonResult: Dynamic) = {
    val currentConfigIn: CurrentConfigIn = Wrapper.jsonToCurrentConfigIn(jsonResult)
    if (jQuery("#currentConfig").length != 0) {
      jQuery("#currentConfig").remove()
    }
    jQuery("<div id=\"currentConfig\" class=\"currentConfig\"> <p id=\"config\">Aktuelle Konfiguration</p> </div>").appendTo(jQuery("header"))
    printCurrentConfig(currentConfigIn)
  }
  
  def printCurrentConfig(currentConfig: CurrentConfigIn): Unit = {
    getNextStep(currentConfig.step)
  }
  
  def getNextStep(step: Option[StepCurrentConfig]): Unit = {
     
    step.get.nextStep match {
      case Some(nextStep) => {
        jQuery("<p>" + step.get.nameToShow + "</p>").appendTo("#currentConfig")
        step.get.components.reverse foreach {
          component => jQuery("<p id=\"config\"> ==== " + component.nameToShow + "</p>").appendTo("#currentConfig")
        }
        getNextStep(step.get.nextStep)
      }
      case None => {
        jQuery("<p>" + step.get.nameToShow + "</p>").appendTo(jQuery("#currentConfig"))
        step.get.components.reverse foreach {component =>
          jQuery("<p> ==== " + component.nameToShow + "</p>").appendTo(jQuery("#currentConfig"))
        }
      }
    }
  }
}