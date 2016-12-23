//package org.admin.persistence.xml
//
//import org.admin.persistence.AdminId
//
//object AdminPersistenceXML {
//  
//  def adminFromXML(adminId: String, password: String): Seq[AdminId] = {
//    
//    val adminXML = scala.xml.XML.loadFile("persistence_XML/registered_admins.xml")
//    
//    val admins:Seq[AdminId] = adminXML \ "admin" map (a => toAdmin(a))
//    
//    admins
//  }
//  
//  def toAdmin(admin: scala.xml.Node): AdminId = {
//    
//    new AdminId((admin \ "id").text, (admin \ "password").text)
//    
//  }
//  
//}