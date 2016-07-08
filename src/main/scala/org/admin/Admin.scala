package org.admin

import org.configSettings.ConfigSettings

object Admin {
  
//  val step = <step>001003</step>
  
//  scala.xml.XML.save("config/test_save.xml", step, "UTF-8", true, null)
  
  var xmlFile = ""
  
  def setXMlFile(xmlFile: String) = {
    if(xmlFile.isEmpty()){
      this.xmlFile = "config/config_v0.1.xml"
    }else {
      this.xmlFile = "config/" + xmlFile
    }
  }
  
  def getXmlFile() = xmlFile
  
}