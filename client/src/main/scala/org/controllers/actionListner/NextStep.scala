package org.controllers.actionListner

import org.scalajs.jquery.JQuery

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
class NextStep {

  def addMouseClick(jQueryElem: JQuery, action: () => Unit) = {

    jQueryElem.on("click", action)

  }

}
