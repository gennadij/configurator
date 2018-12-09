package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.json.currentConfig.JsonStepCurrentConfig
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object CurrentConfig {

  def getCurrentConfigWindow(): JQuery = {
    val htmlCurrentConfig = "<div><center> <h3>Aktuelle Konfiguration</h3> </center></div>"
    val jQueryCurrentConfig = jQuery(htmlCurrentConfig)
    jQueryCurrentConfig.attr("id", HtmlElementText.currentConfigHtml)
    jQueryCurrentConfig.attr("class", HtmlElementText.currentConfigHtml)
  }

  def drawCurrentConfig(stepCurrentConfig: JsonStepCurrentConfig): Unit = {
    drawCurrentConfigRecursiv(stepCurrentConfig)
  }

  def drawCurrentConfigRecursiv(stepCurrentConfig: JsonStepCurrentConfig): Unit = {

    stepCurrentConfig.nextStep match {
      case Some(nextStep) => {
        drawCurrentStepWithComponents(stepCurrentConfig)
        drawCurrentConfigRecursiv(stepCurrentConfig.nextStep.get)
      }
      case None => {
        drawCurrentStepWithComponents(stepCurrentConfig)
      }
    }
  }

  def drawCurrentStepWithComponents(stepCurrentConfig: JsonStepCurrentConfig): Unit = {

    val currentConfigWindow = jQuery(HtmlElementText.currentConfigJQuery)

    val htmlStep = "<div></div>"
    val jQueryCurrentStep = jQuery(htmlStep)
    jQueryCurrentStep.attr("class", HtmlElementText.stepCurrentConfigHtml)

    jQueryCurrentStep.append(stepCurrentConfig.nameToShow)

    currentConfigWindow.append(jQueryCurrentStep)

    stepCurrentConfig.components.reverse foreach {component =>
      val htmlComponent = "<div></div>"
      val jQueryCurrentConfigComponent = jQuery(htmlComponent)
      jQueryCurrentConfigComponent.attr("class", HtmlElementText.componentCurrentConfigHtml)
      jQueryCurrentConfigComponent.append(component.nameToShow)
      jQueryCurrentStep.append(jQueryCurrentConfigComponent)
    }
  }

}
