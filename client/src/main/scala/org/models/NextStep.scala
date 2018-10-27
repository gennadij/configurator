package org.models

import org.controllers.actionListner
import org.scalajs.jquery.jQuery
import org.shared.component.json.JsonComponentOut
import org.views.html.NextStep

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.10.2018
  */
class NextStep {

  def nextStep(jsonComponentOut: JsonComponentOut) = {
    jsonComponentOut.result.status.selectionCriterium.get.status match {
      case "REQUIRE_NEXT_STEP" =>

        val jQueryButtonNextStep = NextStep.drawNextStepButton(jsonComponentOut.result.stepId)

        new actionListner.NextStep().addMouseClick(jQueryButtonNextStep, actionNextStep)

      case _ => jQuery()
    }
  }

  def actionNextStep(): Unit = {
    println("NextStep")
  }

}
