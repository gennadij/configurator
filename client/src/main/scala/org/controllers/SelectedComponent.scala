package org.controllers

import org.controllers.action.CurrentConfig
import org.shared.component.json.JsonComponentOut
import org.views.DrawSelectedComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class SelectedComponent(jsonComponentOut: JsonComponentOut) {

  def selectedComponent = {

    new DrawSelectedComponent().drawSelectedComponent(jsonComponentOut)

    //CurrentConfig aufrufen

    new CurrentConfig().getCurrentConfig
  }

}
