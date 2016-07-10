package org.admin

import org.configSettings.ConfigSettings
import scala.xml._



object Admin {
  
  def setConnectPathForConfigClient(clientId: String, configPath: String) = {
    
    val clientFile = "xmlClients/client_" + configPath.split("/").last + ".xml"
    
    val configFile = "config_" + configPath.split("/").last + ".xml"
    
    val client = new ConfigClient(clientId, configPath, configFile)
    
    val clientXML: Node = client.toXML
    
    scala.xml.XML.save(clientFile, clientXML, "UTF-8", true, null)
  }
}