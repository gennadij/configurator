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

  def drawSelectedComponentInfoWindow(jsonSelectedComponentResult: JsonSelectedComponentResult): JQuery = {
    jQuery("#info").remove()

    val jQueryDiv = jQuery(HtmlElementText.infoDiv)

    jQueryDiv.attr(HtmlElementText.id, HtmlElementText.infoHtml)
    jQueryDiv.attr(HtmlElementText.clazz, HtmlElementText.infoHtml)

    val errorMessage: String = jsonSelectedComponentResult.errors match {
      case Some(e) => (e map (_.message)).toString()
      case None => ""
    }

    val warningExcludedInternal: String = jsonSelectedComponentResult.warning match {
      case Some(w) => w.excludedComponentInternal match {
        case Some(eI) => eI.message
        case None => ""
      }
      case None => ""
    }

    val warningExcludedExternal: String = jsonSelectedComponentResult.warning match {
      case Some(w) => w.excludedComponentExternal match {
        case Some(eE) => eE.message
        case None => ""
      }
      case None => ""
    }

    val warningExcludeExternal: String = jsonSelectedComponentResult.warning match {
      case Some(w) => w.excludeComponentExternal match {
        case Some(eE) => eE.message
        case None => ""
      }
      case None => ""
    }

    val warningExcludeInternal: String = jsonSelectedComponentResult.warning match {
      case Some(w) => w.excludeComponentInternal match {
        case Some(eI) => eI.message
        case None => ""
      }
      case None => ""
    }

    val warningMessage = warningExcludedInternal + warningExcludedExternal + warningExcludeInternal + warningExcludeExternal

    val infoMessage = jsonSelectedComponentResult.info match {
      case Some(i) => i.selectionCriterion match {
        case Some(sC) => sC.message
        case None => ""
      }
      case None => ""
    }

    val htmlMessage =
      "Error: " + errorMessage + "<br>" +
        "Warning: " + warningMessage + "<br>" +
        "Info: " + infoMessage

    jQueryDiv.append(htmlMessage)

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
        "excludedComponentInternal=" + getWarning(warning.excludedComponentInternal) +
        "<br> &emsp;" +
        "excludedComponentExternal=" + getWarning(warning.excludedComponentExternal) +
        "<br> &emsp;" +
        "excludeComponentExternal=" + getWarning(warning.excludeComponentExternal) +
        "<br> &emsp;" +
        "excludeComponentInternal=" + getWarning(warning.excludeComponentInternal)
      case None => "No Warning"
    }

    val textInfo: String = jsonSelectedComponentResult.info match {
      case Some(info) => getInfoSelectionCriterion(info.selectionCriterion)
      case None => "No Info"
    }

    val htmlStatus =
      "Error: " + textError +
        " <br> " +
      "Warning: " + textWarning +
        " <br> " +
      "Info: " + textInfo

    jQueryDiv.append(htmlStatus)

    jQuery(HtmlElementText.header).append(jQueryDiv)
  }

  private def getInfoSelectionCriterion(i: Option[JsonInfo]): String = {
    i match {
      case Some(i) => i.name
      case None => "No Warning"
    }
  }

  private def getWarning(w: Option[JsonWarning]): String = {
    w match {
      case Some(w) => w.name
      case None => "No Warning"
    }
  }
}
