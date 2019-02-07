package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.json.common.{JsonInfo, JsonWarning}
import org.shared.json.selectedComponent.JsonSelectedComponentResult
import org.shared.json.step.JsonStepResult
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object StatusWindow {

  def drawStepStatusWindow(jsonStepOutResult: JsonStepResult) :JQuery = {

    jQuery(HtmlElementText.statusJQuery).remove()

    val jQueryDiv = jQuery(HtmlElementText.statusDiv)

    jQueryDiv.attr(HtmlElementText.id, HtmlElementText.statusHtml)
    jQueryDiv.attr(HtmlElementText.clazz, HtmlElementText.statusHtml)

    val textErrors: String = jsonStepOutResult.errors match {
      case Some(errors) => (errors map(_.name)).toString
      case None => "No errors"
    }

    val textWarnings: String = jsonStepOutResult.warnings match {
      case Some(warnings) => (warnings map(_.name)).toString
      case None => "No warnings"
    }
    val htmlStatus = textErrors + " || " + textWarnings

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementText.header).append(jQueryDiv)
  }

  def drawSelectedComponentStatusWindow(jsonSelectedComponentResult: JsonSelectedComponentResult): JQuery = {

    jQuery("#status").remove()

    val jQueryDiv = jQuery(HtmlElementText.statusDiv)

    jQueryDiv.attr(HtmlElementText.id, HtmlElementText.statusHtml)
    jQueryDiv.attr(HtmlElementText.clazz, HtmlElementText.statusHtml)

    val textError: String = jsonSelectedComponentResult.errors match {
      case Some(errors) => (errors map(_.name)).toString
      case None => "No errors"
    }

    val textWarning: String = jsonSelectedComponentResult.warning match {
      case Some(warning) =>
        getWarningForSxcludedComponent(warning.excludedComponentInternal) +
          " ||" +
        getWarningForSxcludedComponent(warning.excludedComponentExternal)
      case None => "No Warning"
    }

    val textInfo: String = jsonSelectedComponentResult.info match {
      case Some(info) => getInfoSelectionCriterion(info.selectionCriterion)
      case None => "No Info"
    }

    val htmlStatus = textError + " || " + textWarning + " || " + textInfo

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementText.header).append(jQueryDiv)
  }

  private def getInfoSelectionCriterion(i: Option[JsonInfo]): String = {
    i match {
      case Some(i) => i.name
      case None => ""
    }
  }

  private def getWarningForSxcludedComponent(w: Option[JsonWarning]): String = {
    w match {
      case Some(w) => w.name
      case None => ""
    }
  }

//  def drawNextStepStatusWindow(jsonStepOutResult: JsonStepResult) :JQuery = {
//
//    jQuery(HtmlElementText.statusJQuery).remove()
//
//    val jQueryDiv = jQuery(HtmlElementText.statusDiv)
//
//    jQueryDiv.attr(HtmlElementText.id, HtmlElementText.statusHtml)
//    jQueryDiv.attr(HtmlElementText.clazz, HtmlElementText.statusHtml)
//
//    val htmlStatus = "TODO Error, Warning, Info"
////      jsonStepStatus.nextStep.get.status +" , " + jsonStepStatus.common.get.status
//
//    jQueryDiv.append(htmlStatus)
//
//    jQuery(HtmlElementText.header).append(jQueryDiv)
//  }
}
