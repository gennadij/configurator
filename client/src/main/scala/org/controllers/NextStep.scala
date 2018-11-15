package org.controllers

import org.controllers.action.Component
import org.scalajs.jquery.jQuery
import org.shared.component.json.JsonComponentOut
import org.shared.nextStep.json.JsonNextStepOut
import org.views.{DrawNextStep, HtmlElementText}
import org.views.html.NextStepButton

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.10.2018
  */
class NextStep {

  def requirenNextStep(jsonComponentOut: JsonComponentOut) = {

    val componentTypeAndSelectionCriterium: (String, String) =
      (jsonComponentOut.result.status.selectionCriterium.get.status,
        jsonComponentOut.result.status.componentType.get.status)

    componentTypeAndSelectionCriterium match {
      case ("REQUIRE_NEXT_STEP", "DEFAULT_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()
        val jQueryButtonNextStep = NextStepButton.drawNextStepButton(jsonComponentOut.result.stepId)
        new action.NextStep().addMouseClick(jQueryButtonNextStep)

      case ("REQUIRE_NEXT_STEP", "FINAL_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case ("REQUIRE_COMPONENT", _) =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case ("ALLOW_NEXT_COMPONENT", "DEFAULT_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()
        val jQueryButtonNextStep = NextStepButton.drawNextStepButton(jsonComponentOut.result.stepId)
        new action.NextStep().addMouseClick(jQueryButtonNextStep)

      case ("ALLOW_NEXT_COMPONENT", "FINAL_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case _ => jQuery()
    }
  }

  def nextStep(jsonNextStepOut: JsonNextStepOut) = {

    jsonNextStepOut.result.status.nextStep.get.status match {
      case "NEXT_STEP_EXIST" =>
        val jQueryComponentWindows = new DrawNextStep().drawNextStep(jsonNextStepOut)

        jQueryComponentWindows foreach(jQCW => {
          new Component().addMouseClickForComponent(jQCW)
        })
    }
  }

}
