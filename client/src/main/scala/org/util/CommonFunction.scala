package org.util

import org.scalajs.jquery.jQuery

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 25.05.2018
 */
trait CommonFunction {
  
  
  def cleanPage = {
    jQuery(HtmlElementIds.mainJQuery).remove
    jQuery(HtmlElementIds.statusJQuery).remove()
  }
  
  def drawNewStatus(status: String) {
    val htmlHeaderStatus = 
      "<div id='" + HtmlElementIds.statusHtml + "' class='" + HtmlElementIds.statusHtml + "'>" +
          status + 
      "</div>"
  
    jQuery(htmlHeaderStatus).appendTo(jQuery("header"))
  }
  
  def drawNewMain(html: String) = {
    jQuery(html).appendTo(jQuery(HtmlElementIds.section))
  }
  
  def drawButton(id: String, text: String) = {
    "<div id='" + id + "' class='button'> " + text + " </div>"
  }
  
  def drawInputField(id: String, label: String, typeofInput: String = "text"): String = {
    label + " <input id='" + id + "' type='" + typeofInput + "'>"
  }
  
}