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
import org.status.WarningStatus



object Admin {
  /**
   * Administrator definiert und zusammenstellt sein einegen Konfiguration.
   * Auf der Webseite von Administrator werden alle notwendigen Einstellungen 
   * zu dem ConfigServer, ConfigClient getätigt. Danach kann der User alle 
   * Schritte der Konfiguration difenieren. 
   * 
   * 1. Administrator soll sich zuerst Registrieren mit einem Username und Password. 
   * 2. Nach der Regestrierung kann der Admin mit Username und Passwort sich in 
   * ---- der Administrationsbereich anmelden und eine eigen Konfiguration 
   * ---- erstellen.
   * 3. Die Konfiguration besteht aus der ConfigStep und Components.
   * ---- In jedem Step oder Component kann man die DB-Query difenieren um 
   * ---- die gesamte Information zu der Step mit Components aus der fremden DB zu lesen
   * 
   */
  
  /**
   * Rigestrierung
   * 
   * Client -> Regestrierung mit adminId und password
   * Server -> true/false Bestätigung
   * 
   * changeUsername(): AdminUser
   * changePassword(): AdminUser
   * deleteAccount(): AdminUser
   * 
   */
  
  def register(adminUsername: String,  adminUserPassword: String): AdminUser = {
    // TODO Alle Resourcen einstellen (AdminID, Datenbankverbindung usw.)
    // TODO Püfen ob der AdminUser schon exestiert
    Persistence.registAdminUser(adminUsername, adminUserPassword)
  }
  
  /**
   * END
   */
  
  
  /**
   * create ConfigTree
   * 
   * 
   */
  
  
  /*
   * return UserId für den Client. Bei der nächsten Anfragen wird diese 
   * UserId verwendet
   * 
   */
  def authenticate(username: String, password: String): Boolean = ???
  
  def configTree(adminId: String): List[Step] = ???
  
  /*
   * return aktualisierte Liste von Steps
   * 
   * action 
   *  - add
   *  - remove
   *  - update
   */
  
  def updateConfig(adminId: String, step: Step, action: String): List[Step] = ???
  
  def logout(adminId: String): Boolean = ???
  
  
  /**
   * END
   */
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * Anmeldung von Admin
   * 
   * Cleint -> Anmeldung mit regestrierten Daten (adminId, password)
   * 
   * Server -> true
   *        -> false --> adminId existiert nicht, Falsches Password
   */
  
  def connect(username: String, password: String): Status = {
    
//    val admins: Seq[AdminId] = InterfaceAdminPersistence.admin(adminId, password)
//    
//    findAndCheckAdmin(adminId, password, admins)
    val adminId = AdminUserVertex.adminId(username, password)
    
    if (adminId == "") new WarningStatus("The User with thisusername and password not exist")
      else new SuccessfulStatus(s"user with id = $adminId is logged")
  }
  
  def findAndCheckAdmin(adminId: String, password: String, admins: Seq[AdminId]): Status = {
    
    if(admins.exists { admin => admin.adminId == adminId && admin.password == password })
      SuccessfulStatus("Anmeldung ist erfolgreich")
    else
      ErrorStatus("Administrator Id oder Passwort falsch")
  }
  
  def setStep(user: String, isConnected: Boolean, step: Step): Status = {
    
    val stepId = List.empty
    
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