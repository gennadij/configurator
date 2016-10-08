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

object AdminUserVertex {
  
  val className = "AdminUser"
  val propKeyAdminId = "adminId"
  val propKeyAdminUsername = "username"
  val propKeyAdminUserPassword = "userPassword"
  
  def create(adminUserId: String, adminUsername: String, adminUserPassword: String): Status = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyAdminId, adminUserId).size == 0){
      graph.addVertex(s"class:$className", propKeyAdminId, adminUserId, 
      propKeyAdminUsername, adminUsername, propKeyAdminUserPassword, adminUserPassword)
      graph.commit
      new SuccessfulStatus("object AdminUser with " + adminUserId + " was created")
    }else{
      new WarningStatus("object AdminUser with " + adminUserId + " already exist")
    }
  }
  def createSchema = {
    val graph: OrientGraph = OrientDB.getGraph
    if(graph.getVertexType(className) == null){
      val vStep: OrientVertexType = graph.createVertexType(className)
      vStep.createProperty(propKeyAdminId, OType.STRING)
      vStep.createProperty(propKeyAdminUsername, OType.STRING)
      vStep.createProperty(propKeyAdminUserPassword, OType.STRING)
      graph.commit
      new SuccessfulStatus("class AdminUser was created")
    }else {
      new WarningStatus("class AdminUser already exist")
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
      .command(new OCommandSQL(s"SELECT adminId FROM AdminUser WHERE username='$username' and userPassword='$adminPassword'")).execute()
//     var id = ""
//      res.foreach ( v => {
//      val vAdmin: OrientVertex = v.asInstanceOf[OrientVertex]
//      id = vAdmin.getProperty(propKeyAdminId)
//    })
//    println(id)
    
    val array = res.toList
    
    (array.map(_.asInstanceOf[OrientVertex].getProperty("adminId").toString())).head
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