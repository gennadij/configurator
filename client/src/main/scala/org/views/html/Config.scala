package org.views.html

import org.scalajs.jquery.{JQuery, jQuery}
import org.util.HtmlElementIds

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 17.10.2018
  */
object Config {

  def getConfigWindow():JQuery = {

    val textHtml = "<div> <center> <h3> Konfigurator </h3> </center> </div>"

    val htmlMainWindow = jQuery(textHtml)

    htmlMainWindow.attr("id", HtmlElementIds.mainHtml)
    htmlMainWindow.attr("class", HtmlElementIds.mainHtml)
  }
}
