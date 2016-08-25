package org.admin

import org.configSettings.ConfigSettings
import scala.xml._
import org.admin.persistence.xml.ConfigID
import org.admin.persistence.InterfaceAdminPersistence



object Admin {
      /**
     * Administrator definiert und zusammenstellt sein einegen Konfiguration.
     * Auf der Webseite von Administrator werden alle notwendigen Einstellungen 
     * zu dem ConfigServer, ConfigClient getätigt. Danach kann der User alle 
     * Schritte der Konfiguration difenieren. 
     * 
     * 1. Administrator soll sich zuerst Registrieren mit einem adminID und Password. 
     * ----(Implementierung einer Schnittstelle, Zuerst zu der XML-Persistenz später DB)
     * 2. Nach den Regestrierung kann der Admin mit Username und Passwort sich in 
     * ---- der Administrationsbereich anmelden und eine eigen Konfiguration 
     * ---- erstellen.
     * 3. Die Konfiguration besteht aus der ConfigStep und Components.
     * ---- 
     * 
     * 
     */
  def register(adminId: String, password: String) = {
    // Implement later (not so important for this time)
    InterfaceAdminPersistence.registerAdmin
  }
  
  def connect(adminID: String, password: String) = ???
  
  def setConnectPathForConfigClient(clientId: String, configPath: String) = {
    
    val pathToConfigID = "config_ids/client_" + configPath.split("/").last + ".xml"
    
    val configFile = "config_" + configPath.split("/").last + ".xml"
    
    val client = new ConfigID(clientId, configPath, configFile)
    
    val clientXML: Node = client.toXML
    
    scala.xml.XML.save(pathToConfigID, clientXML, "UTF-8", true, null)
  }
}