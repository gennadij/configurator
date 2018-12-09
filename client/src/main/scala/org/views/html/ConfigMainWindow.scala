package org.views.html

import org.scalajs.jquery.jQuery
import org.views.HtmlElementText

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 26.10.2018
  */
object ConfigMainWindow {

  def drawConfigMainInSection = {
    val htmlConfigMainWindow = jQuery(HtmlElementText.configMainDiv)

    htmlConfigMainWindow.attr(HtmlElementText.id, HtmlElementText.configMainHtml)

    htmlConfigMainWindow.attr(HtmlElementText.clazz, HtmlElementText.configMainHtml)
  }
}
