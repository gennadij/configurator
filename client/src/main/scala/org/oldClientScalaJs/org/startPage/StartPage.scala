package org.oldClientScalaJs.org.startPage

import org.scalajs.jquery.jQuery
import org.scalajs.dom.raw.WebSocket

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 18.12.2017
 */
class StartPage {
  
  val idStartConfigButton: String = "startConfigButton"
  val idWilcomeText: String = "wilcomeText"
  
  def setStartPage(socket: WebSocket) = {
    jQuery(createStartConfigHtml).appendTo("body")
    jQuery(s"#$idStartConfigButton").on("click", () => startConfig(socket))
    startConfig(socket)
  }
  
  
  def startConfig(socket: WebSocket): Unit = {
    jQuery("#wilcomeText").remove()
    jQuery("#startConfigButton").remove()
    val startConfigIn: String = """{"json":"StartConfig","params":{"configUrl":"http://contig1/user29_v016"}}"""
    socket.send(startConfigIn)
  }
  
  def createStartConfigHtml = {
    s"<h1 id='$idWilcomeText'> Wilkommen zu der Konfiguration</h1>" + 
    s"<button id='$idStartConfigButton' type='button'>Konfiguration starten</button>"
  }
}