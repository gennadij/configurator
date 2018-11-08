package org.views

import org.shared.common.json.{JsonComponent, JsonStep}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 25.05.2018
 */

object HtmlElementText {
  //common HTML Strings
  def id =                          "id"
  def clazz =                       "class"
  def numberSign =                  "#"
  def section =                     "section"
  def header =                      "header"
  // Main window for configurator
  def configMainHtml =              "main"
  def configMainJQuery =            "#main"
  def configMainDiv =               "<div> <center> <h3> Konfigurator </h3> </center> </div>"

  def statusHtml =                  "status"
  def statusJQuery =                "#status"

  def classStep =                   "step"
  def stepDiv =                     "<div></div>"
  def stepText(
                jsonStep: JsonStep
              ): String =           "ID: " + jsonStep.stepId.subSequence(0, 6) + "&emsp; || &emsp; nameToShow: " + jsonStep.nameToShow

  def classComponent =              "component"

  def componentDiv =                "<div></div>"

  def componentText(
                     jsonComponent: JsonComponent
                   ): String =       "ID " + jsonComponent.componentId.subSequence(0, 6) + "&emsp; || &emsp;" + "nameToShow: " + jsonComponent.nameToShow

  def currentConfigJQuery =          "#currentConfig"
  def currentConfigHtml =           "currentConfig"

  def stepCurrentConfigHtml =       "stepCurrentConfig"

  def componentCurrentConfigHtml =  "componentCurrentConfig"

  def buttonHtml                 =  "button"
  def textInButtonHtml =            "NextStep"

}
