package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientVertex


object StepVertex {
  val propClassName = "Step"
  val propKeyId = "stepId"
  val propKeyAdminId = "adminId"
  val propKeyKind = "kind"
  
//  def create(graph: OrientGraph, propKeys: List[String]){
//    val vStep = new VertexStep()
//    vStep.create(graph, propKeys)
//  }
  
  def create(props: Map[String, String]) = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyId, props(propKeyId)).size == 0){
        val vertex: OrientVertex = graph.addVertex("class:Step", 
            propKeyId, props(propKeyId), 
            propKeyAdminId, props(propKeyAdminId),
            propKeyKind, props(propKeyKind))
        graph.commit
        new SuccessfulStatus("object Step with " + vertex.getId + vertex.getProperties + " was created")
    }else{
      new WarningStatus("object Step with " + props(propKeyId) + "already exist")
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
//class VertexStep {
//  
//  val propStep = "Step"
//  val propStepId = "stepId"
//  
//  private def create(graph: OrientGraph, props: Map[String, String]){
//    if(graph.getVertices("stepId", props("id")).size == 0){
//        graph.addVertex("class:Step", "stepId", props("id"))
//        graph.commit
//        new SuccessfulStatus("object Step with " + props("id") + " was created")
//    }else{
//      new WarningStatus("object Step with " + props("id") + "already exist")
//    }
//  }
//  
//  private def createSchema(graph: OrientGraph){
//    if(graph.getVertexType(propStep) == null){
//      val vStep: OrientVertexType = graph.createVertexType("Step")
//      vStep.createProperty(propStepId, OType.STRING)
//      graph.commit
//      new SuccessfulStatus("class Step was created")
//    }else {
//      new WarningStatus("class Step already exist")
//    }
//  }
//  
//  private def update(graph: OrientGraph, props: Map[String, String]){
//    //TODO bessere such Methode
//    if(graph.getVertices("stepId", props("id")).size == 0){
////        graph.addVertex("class:Step", "stepId", props("id"))
////        graph.commit
//      new WarningStatus("object Step with " + props("id") + "cannot update because not exist")
//    }else{
//      new SuccessfulStatus("object Step with " + props("id") + " was updated")
//     
//    }
//  }
//}

//  def createSchema(graph: OrientGraph) = {
//    if(graph.getVertexType(propClassName) == null){
//      val vStep: OrientVertexType = graph.createVertexType("Step")
//      vStep.createProperty(propKeyId, OType.STRING)
//      vStep.createProperty(propKeyAdminId, OType.STRING)
//      graph.commit
//      new SuccessfulStatus("class Step was created")
//    }else {
//      new WarningStatus("class Step already exist")
//    }
//  }