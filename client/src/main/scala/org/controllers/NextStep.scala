package org.controllers

import org.controllers.action.Component
import org.scalajs.jquery.jQuery
import org.shared.json.selectedComponent.JsonSelectedComponentOut
import org.shared.json.step.JsonStepOut
import org.views.{DrawNextStep, HtmlElementText}
import org.views.html.NextStepButton

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.10.2018
  */
class NextStep {

  def requireNextStep(jsonComponentOut: JsonSelectedComponentOut) = {

    val isLastStepAndSelectionCriterionWarning: (Boolean, String) = jsonComponentOut.result.info match {
      case Some(info) => (jsonComponentOut.result.lastComponent, info.selectionCriterion.get.name)
      case None => (jsonComponentOut.result.lastComponent, "") //TODO anpassen fuer None
    }

    isLastStepAndSelectionCriterionWarning match {
      case (false, "REQUIRE_NEXT_STEP") =>

        jQuery(HtmlElementText.buttonJQuery).remove()
        val jQueryButtonNextStep = NextStepButton.drawNextStepButton(jsonComponentOut.result.stepId)
        new action.NextStep().addMouseClick(jQueryButtonNextStep)

      case (true, "REQUIRE_NEXT_STEP") =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case (_, "REQUIRE_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case (false, "ALLOW_NEXT_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()
        val jQueryButtonNextStep = NextStepButton.drawNextStepButton(jsonComponentOut.result.stepId)
        new action.NextStep().addMouseClick(jQueryButtonNextStep)

      case (true, "ALLOW_NEXT_COMPONENT") =>

        jQuery(HtmlElementText.buttonJQuery).remove()

      case _ => jQuery()
    }
  }

  def nextStep(jsonStepOut: JsonStepOut) = {

    jsonStepOut.result.errors match {
      case _ =>
        val jQueryComponentWindows = new DrawNextStep().drawNextStep(jsonStepOut)

        jQueryComponentWindows foreach(jQCW => {
          new Component().addMouseClickForComponent(jQCW)
        })
    }
  }
}
