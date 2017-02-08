package org.admin.persistence

import org.configTree.step.Step
import org.admin.persistence.xml.AdminPersistenceXML

object InterfaceAdminPersistence {
  
  def registerAdmin = ???
  
  def admin(adminId: String, password: String): Seq[AdminId] = {
    AdminPersistenceXML.adminFromXML(adminId, password)
  }
  
  def connectAdmin(user: String, password: String): Boolean = ???
  
  def setStep(step: Step, user: String, connection: Boolean) = ???
  
  def getStep(step: Step, user: String, connection: Boolean) = ???
  
  
}