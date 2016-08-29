package org.admin.persistence

import org.configTree.step.Step
import org.admin.persistence.xml.AdminPersistenceXML

object InterfaceAdminPersistence {
  
  def admin(adminId: String, password: String): Seq[AdminId] = {
    AdminPersistenceXML.adminFromXML(adminId, password)
  }
  
  def registerAdmin = ???
  
  def connectAdmin(user: String, password: String): Boolean = ???
  
  def setStep(step: Step, user: String, connection: Boolean) = ???
  
  
}