package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType

object ComponentVertex {
  
  val propClassName = "Component"
  val propKeyId = "componentId"
  val propKeyAdminId = "adminId"
  
  
  def createSchema(graph: OrientGraph) = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertexType(propClassName) == null){
      val vStep: OrientVertexType = graph.createVertexType(propClassName)
      vStep.createProperty(propKeyId, OType.STRING)
      vStep.createProperty(propKeyAdminId, OType.STRING)
      graph.commit
      new SuccessfulStatus("class Component was created")
    }else {
      new WarningStatus("class Component already exist")
    }
  }
  
  def create(props: Map[String, String]) = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyId, props(propKeyId)).size == 0){
        graph.addVertex(s"class:$propClassName", propKeyId, props(propKeyId), propKeyAdminId, props(propKeyAdminId))
        graph.commit
        new SuccessfulStatus("object Component with " + props(propKeyId) + " was created")
    }else{
      new WarningStatus("object Component with " + props(propKeyId) + "already exist")
    }
  }
  
   def update(graph: OrientGraph, props: Map[String, String]){
    //TODO bessere such Methode
    if(graph.getVertices("stepId", props("id")).size == 0){
//        graph.addVertex("class:Step", "stepId", props("id"))
//        graph.commit
      new WarningStatus("object Step with " + props("id") + "cannot update because not exist")
    }else{
      new SuccessfulStatus("object Step with " + props("id") + " was updated")
     
    }
  }
  
  def get() = ???
  
}