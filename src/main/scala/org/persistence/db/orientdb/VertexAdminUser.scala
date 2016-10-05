package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.orientechnologies.orient.core.metadata.schema.OType

object VertexAdminUser {
  
  val propClassName = "AdminUser"
  val propKeyForId = "adminUserId"
  val propKeyAdminUsername = "username"
  val propKeyAdminUserPassword = "userPassword"
  
  def create(adminUserId: String, adminUsername: String, adminUserPassword: String) {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyForId, adminUserId).size == 0){
        graph.addVertex("class:Step", propKeyForId, adminUserId, 
        propKeyAdminUsername, adminUsername, propKeyAdminUserPassword, adminUserPassword)
        graph.commit
        new SuccessfulStatus("object AdminUser with " + adminUserId + " was created")
    }else{
      new WarningStatus("object AdminUser with " + adminUserId + "already exist")
    }
  }
  def createSchema(graph: OrientGraph) = {
    if(graph.getVertexType(propClassName) == null){
      val vStep: OrientVertexType = graph.createVertexType("AdminUser")
      vStep.createProperty(propKeyForId, OType.STRING)
      vStep.createProperty(propKeyAdminUsername, OType.STRING)
      vStep.createProperty(propKeyAdminUserPassword, OType.STRING)
      
      graph.commit
      new SuccessfulStatus("class AdminUser was created")
    }else {
      new WarningStatus("class AdminUser already exist")
    }
  }
  
  def update = ???
  
  def get = ???

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