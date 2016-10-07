package org.admin

import org.configSettings.ConfigSettings
import scala.xml._
import org.admin.persistence.xml.ConfigID
import org.admin.persistence.InterfaceAdminPersistence
import org.admin.persistence.AdminId
import org.status.Status
import org.status.SuccessfulStatus
import org.status.ErrorStatus
import org.configTree.step.Step
import org.persistence.Persistence
import org.persistence.db.orientdb.AdminUserVertex



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
     * 4. Der Admin braucht der Produktendatenbank fuer die Konfiguration,
     * ---- deswegen er braucht eine Verknüpfung zwischen Konfiguration und
     * ---- Produktendatenbank (Componenten).
     * 
     * 
     * 
     * 
     * 
     */
  
  /**
   * Rigestrierung
   * 
   * Client -> Regestrierung mit adminId und password
   * Server -> true/false Bestätigung
   * 
   * 
   */
  
  def register(adminUserId: String, adminUsername: String,  adminUserPassword: String): Status = {
    // TODO Alle Resourcen einstellen (AdminID, Datenbankverbindung usw.)
    // TODO Püfen ob der AdminUser schon exestiert
    AdminUserVertex.createSchema
    AdminUserVertex.create(adminUserId, adminUsername, adminUserPassword)
  }
  
  /**
   * Anmeldung von Admin
   * 
   * Cleint -> Anmeldung mit regestrierten Daten (adminId, password)
   * 
   * Server -> true
   *        -> false --> adminId existiert nicht, Falsches Password
   */
  
  def connect(adminUsername: String, password: String): Status = {
    
//    val admins: Seq[AdminId] = InterfaceAdminPersistence.admin(adminId, password)
//    
//    findAndCheckAdmin(adminId, password, admins)
    new SuccessfulStatus("")
  }
  
  def findAndCheckAdmin(adminId: String, password: String, admins: Seq[AdminId]): Status = {
    
    if(admins.exists { admin => admin.adminId == adminId && admin.password == password })
      SuccessfulStatus("Anmeldung ist erfolgreich")
    else
      ErrorStatus("Administrator Id oder Passwort falsch")
  }
  
  def setStep(user: String, isConnected: Boolean, step: Step): Status = {
    
    
    Persistence.setStep(user, isConnected, step)
  }
  
  
  
  def setConnectPathForConfigClient(clientId: String, configPath: String) = {
    
    val pathToConfigID = "config_ids/client_" + configPath.split("/").last + ".xml"
    
    val configFile = "config_" + configPath.split("/").last + ".xml"
    
    val client = new ConfigID(clientId, configPath, configFile)
    
    val clientXML: Node = client.toXML
    
    scala.xml.XML.save(pathToConfigID, clientXML, "UTF-8", true, null)
  }
}