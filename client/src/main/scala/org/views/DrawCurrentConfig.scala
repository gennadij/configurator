package org.views

import org.scalajs.jquery.jQuery
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawCurrentConfig {

  def drawCurrentConfig = {
    val htmlCurrentConfig = "<dev id='currentConfig'><center> Test-Aktuelle Konfiguration </center></dev>"

    jQuery(htmlCurrentConfig).appendTo(jQuery(HtmlElementIds.section))
  }
}
