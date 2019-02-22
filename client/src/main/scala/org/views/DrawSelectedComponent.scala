package org.views

import org.shared.json.selectedComponent.JsonSelectedComponentOut
import org.views.html.{ComponentWindow, StatusWindow}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.11.2018
  */
class DrawSelectedComponent {

  def drawSelectedComponent(jsonComponentOut: JsonSelectedComponentOut): Unit = {

    StatusWindow.drawSelectedComponentStatusWindow(jsonComponentOut.result)
    StatusWindow.drawSelectedComponentInfoWindow(jsonComponentOut.result)

    ComponentWindow.markSelectedComponent(
      jsonComponentOut.result.selectedComponentId,
      jsonComponentOut.result)
  }
}
