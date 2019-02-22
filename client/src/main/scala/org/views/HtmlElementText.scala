package org.views

import org.shared.json.step.{JsonComponent, JsonStep}


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 25.05.2018
 */

object HtmlElementText {

  //common HTML Strings
  def id =                                          "id"
  def clazz =                                       "class"
  def numberSign =                                  "#"
  def section =                                     "section"
  def header =                                      "header"

  // Main window for configurator
  def configMainHtml =                              "main"
  def configMainJQuery =                            "#main"
  def configMainDiv =                               "<div> <center> <h3> Konfigurator </h3> </center> </div>"

  //TODO zu der Warning umbennen
  //Status
  def statusHtml =                                  "status"
  def statusJQuery =                                "#status"
  def statusDiv =                                   "<div></div>"

  //Info

  def infoHtml =                                  "info"
  def infoJQuery =                                "#info"
  def infoDiv =                                   "<div></div>"

  //Step
  def classStep =                                   "step"
  def stepDiv =                                     "<div></div>"
  def stepText(jsonStep: JsonStep): String =        "ID: " + jsonStep.stepId.subSequence(0, 6) +
                                                    "&emsp; || &emsp; nameToShow: " +
                                                    jsonStep.nameToShow

  //Component
  def classComponent =                              "component"
  def componentDiv =                                "<div></div>"
  def componentText(jsonComponent: JsonComponent): String =
                                                    "ID " + jsonComponent.componentId.subSequence(0, 6) +
                                                    "&emsp; || &emsp;" + "nameToShow: " +
                                                    jsonComponent.nameToShow

  //CurrentConfig
  def currentConfigJQuery =                         "#currentConfig"
  def currentConfigHtml =                           "currentConfig"
  def stepCurrentConfigHtml =                       "stepCurrentConfig"
  def componentCurrentConfigHtml =                  "componentCurrentConfig"

  //NextStep
  def buttonNextStepDiv =                           "<div></div>"
  def buttonJQuery    =                             "#button"
  def buttonHtml                 =                  "button"
  def textInButtonHtml =                            "NextStep"
}
