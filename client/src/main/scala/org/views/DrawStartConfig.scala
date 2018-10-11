package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.common.json.{JsonComponent, JsonStep, JsonStepStatus}
import org.util.HtmlElementIds

import scala.collection.mutable

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 28.09.2018
  */
class DrawStartConfig(jsonStep: JsonStep, jsonStepStatus: JsonStepStatus) {

  var componentIds: mutable.ListBuffer[String] = scala.collection.mutable.ListBuffer()

  def drawStartConfig(): JQuery = {

    val htmlMain =
      "<div id='main' class='main'> " +
        "<p>Test - Konfigurator</p>" +
        "</div>"

    drawNewMain(htmlMain)

    val stepsHtml = drawStep(jsonStep)

    jQuery(stepsHtml).appendTo(jQuery(HtmlElementIds.mainJQuery))
  }

  def drawStatus = {
    val htmlHeader =
      s"<dev id='status' class='status'>" +
        jsonStepStatus.firstStep.get.status +
        " , " +
        jsonStepStatus.common.get.status +
        "</dev>"

    jQuery("#status").remove()
    jQuery(htmlHeader).appendTo(jQuery("header"))
  }

  private def drawNewMain(html: String) = {
    jQuery(html).appendTo(jQuery(HtmlElementIds.section))
  }

  private def drawStep(step: JsonStep) = {
    val htmlComponents = drawComponents(step.components)

    "<div id='" + step.stepId + "' class='step'>" +
      "ID: " + step.stepId.subSequence(0, 6) +
      "&emsp; || &emsp;" +
      "nameToShow: " + step.nameToShow +
      "</br>" +
      htmlComponents +
      "</div>"
  }

  private def drawComponents(components: List[JsonComponent]): String = {

    var htmlComponents = ""

    components foreach { component =>
      val componentId = component.componentId

      this.componentIds += componentId

      htmlComponents = htmlComponents +
        "<div id='" + componentId + "' class='component'>" +
        "ID " + component.componentId.subSequence(0, 6) +
        "&emsp; || &emsp;" +
        "nameToShow: " + component.nameToShow +
        "&emsp; || &emsp;" +
        "</br>" +
        "</div>"
    }

    htmlComponents
  }
}
