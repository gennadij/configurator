package org.admin.persistence

import org.configTree.step.Step

object InterfaceAdminPersistence {
  
  
  def registerAdmin = ???
  
  def connectAdmin(user: String, password: String): Boolean = ???
  
  def setStep(step: Step, user: String, connection: Boolean) = ???
  
  
}