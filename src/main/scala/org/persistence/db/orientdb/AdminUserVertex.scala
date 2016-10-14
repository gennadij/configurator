package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.Status
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.admin.AdminUser

object AdminUserVertex {
  
  val className = "AdminUser"
  val propKeyAdminId = "adminId"
  val propKeyAdminUsername = "username"
  val propKeyAdminUserPassword = "userPassword"
  
  def create(adminUsername: String, adminUserPassword: String): AdminUser = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyAdminUsername, adminUsername).size == 0){
      val vAdminUser: OrientVertex = graph.addVertex(s"class:$className",
                      propKeyAdminUsername, adminUsername, 
                      propKeyAdminUserPassword, adminUserPassword)
      graph.commit
      new AdminUser("AU" + vAdminUser.getIdentity.toString(), 
                    vAdminUser.getProperty(propKeyAdminUsername).toString(), 
                    vAdminUser.getProperty(propKeyAdminUserPassword).toString(), 
                    "object AdminUser with username " + 
                    vAdminUser.getProperty(propKeyAdminUsername).toString() + 
                    " und AdminUserId " + "AU" + vAdminUser.getIdentity.toString() +  
                    " was created")
//      new SuccessfulStatus("object AdminUser with " + adminUsername + " was created")
    }else{
      new AdminUser("", "", "", "object AdminUser with Username " + adminUsername + " already exist")
//      new WarningStatus("object AdminUser with " + adminUsername + " already exist")
    }
  }

  
  def update = ???
  
  def getAll = {
    
  }
  
  def get(adminUsername: String, adminPassword: String) = {
    val graph: OrientGraph = OrientDB.getGraph
  }
  
  def adminId(username: String, adminPassword: String) = {
    val graph: OrientGraph = OrientDB.getGraph
    val res: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"SELECT FROM AdminUser WHERE username='$username' and userPassword='$adminPassword'")).execute()
    val adminId = res.toList.map(_.asInstanceOf[OrientVertex].getIdentity)
    if(adminId.size == 1) adminId else "" 
  }

//  def create(adminUserId: String, adminUsername: String, adminUserPassword: String) = {
//    val vAdminUser = new VertexAdminUser(adminUserId, adminUsername, adminUserPassword)
//    vAdminUser.create
//  }
}


//class VertexAdminUser(adminUserId: String, adminUserName: String, adminUserPassword: String){
//  val propKeyForId = "adminUserId"
//  val propKeyAdminUsername = "username"
//  val propKeyAdminUserPassword = "userPassword"
//  
//  private def create() {
//    val graph: OrientGraph = OrientDB.getGraph()
//    if(graph.getVertices(propKeyForId, adminUserId).size == 0){
//        graph.addVertex("class:Step", propKeyForId, adminUserId, 
//        propKeyAdminUsername, adminUserName, propKeyAdminUserPassword, adminUserPassword)
//        graph.commit
//        new SuccessfulStatus("object AdminUser with " + adminUserId + " was created")
//    }else{
//      new WarningStatus("object AdminUser with " + adminUserId + "already exist")
//    }
//  }
//
//}
//
//  def createSchema = {
//    val graph: OrientGraph = OrientDB.getGraph
//    if(graph.getVertexType(className) == null){
//      val vStep: OrientVertexType = graph.createVertexType(className)
//      vStep.createProperty(propKeyAdminId, OType.STRING)
//      vStep.createProperty(propKeyAdminUsername, OType.STRING)
//      vStep.createProperty(propKeyAdminUserPassword, OType.STRING)
//      graph.commit
//      new SuccessfulStatus("class AdminUser was created")
//    }else {
//      new WarningStatus("class AdminUser already exist")
//    }
//  }