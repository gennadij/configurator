package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.currentConfig.json.JsonStepCurrentConfig
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawCurrentConfig {

  def drawCurrentConfig: JQuery = {
    val htmlCurrentConfig = "<dev id='currentConfig'><center> Test-Aktuelle Konfiguration </center></dev>"

    jQuery(htmlCurrentConfig).appendTo(jQuery(HtmlElementIds.section))
  }

  def updateCurrentConfig(jsonStepCurrentConfig: JsonStepCurrentConfig)= {

    if (jQuery("#currentConfig").length != 0) {
      jQuery("#currentConfig").remove()
    }
    jQuery("<div id=\"currentConfig\" class=\"currentConfig\"> <p id=\"config\">Aktuelle Konfiguration</p> </div>").appendTo(jQuery("header"))

    printCurrentConfig(jsonStepCurrentConfig)
  }


  def printCurrentConfig(step: JsonStepCurrentConfig): Unit = {
    getNextStep(step)
  }

  def getNextStep(step: JsonStepCurrentConfig): Unit = {

    step.nextStep match {
      case Some(nextStep) => {
        jQuery("<p>" + "  " + step.nameToShow + "</p>").appendTo("#currentConfig")
        step.components.reverse foreach {
          component => jQuery("<p id=\"config\">   ==== " + component.nameToShow + "</p>").appendTo("#currentConfig")
        }
        getNextStep(step.nextStep.get)
      }
      case None => {
        jQuery("<p>" + step.nameToShow + "</p>").appendTo(jQuery("#currentConfig"))
        step.components.reverse foreach {component =>
          jQuery("<p>   ==== " + component.nameToShow + "</p>").appendTo(jQuery("#currentConfig"))
        }
      }
    }
  }
}
