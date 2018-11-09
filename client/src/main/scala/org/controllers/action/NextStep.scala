package org.controllers.action

import org.scalajs.jquery.JQuery

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
class NextStep {

  def addMouseClick(jQueryElem: JQuery) = {

    jQueryElem.on("click", () => actionNextStep())

  }

  def actionNextStep(): Unit = {
    println("NextStep")
  }

}
