package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.admin.configTree.AdminStep
import org.admin.configTree.AdminConfigTreeStep
import org.status.Status
import org.admin.configTree.AdminNextStep


object StepVertex {
  val propClassName = "Step"
  val propKeyId = "stepId"
  val propKeyAdminId = "adminId"
  val propKeyKind = "kind"
  
//  def create(graph: OrientGraph, propKeys: List[String]){
//    val vStep = new VertexStep()
//    vStep.create(graph, propKeys)
//  }
  
  // TODO if Einweisung mit stepId ausbessern
//  def addStep(step: AdminStep): String = {
//    val graph: OrientGraph = OrientDB.getGraph()
//    if(graph.getVertices(propKeyId, step.id).size == 0){
//        val vertex: OrientVertex = graph.addVertex("class:Step", 
////            propKeyId, props(propKeyId), 
//            propKeyAdminId, step.adminId,
//            propKeyKind, step.kind)
//        graph.commit
//        vertex.setProperty(propKeyId, "S" + vertex.getIdentity.toString())
//        graph.commit
//        vertex.getIdentity.toString
//    }else{
//      ""
//    }
//  }
  
    /**
   * 
   * fuegt Vertex Step zu ConfigTree hinzu
   * 
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param AdminStep
   * 
   * @return Status
   */
  
  def addStep(adminStep: AdminStep): AdminStep = {
    val graph: OrientGraph = OrientDB.getGraph()
    val vStep: OrientVertex = graph.addVertex("class:Step", 
            "adminId", adminStep.adminId,
            "kind", adminStep.kind)
        graph.commit
        vStep.setProperty("stepId", "S" + vStep.getIdentity.toString())
        graph.commit
        
        new AdminStep(
            true,
            vStep.getIdentity.toString(),
            "S" + vStep.getIdentity.toString(),
            vStep.getProperty("adminId"),
            vStep.getProperty("kind"), 
            null
        )
  }
  
    def addStep(adminStep: AdminNextStep): AdminNextStep = {
    val graph: OrientGraph = OrientDB.getGraph()
    val vStep: OrientVertex = graph.addVertex("class:Step", 
            "adminId", adminStep.adminId,
            "kind", adminStep.kind)
        graph.commit
        vStep.setProperty("stepId", "S" + vStep.getIdentity.toString())
        graph.commit
        
        new AdminNextStep(
            true,
            vStep.getIdentity.toString(),
            "S" + vStep.getIdentity.toString(),
            vStep.getProperty("adminId"),
            vStep.getProperty("kind"), 
            null,
            ""
        )
  }
  
  
  def create(props: Map[String, String]) = {
    val graph: OrientGraph = OrientDB.getGraph()
    if(graph.getVertices(propKeyId, props(propKeyId)).size == 0){
        val vertex: OrientVertex = graph.addVertex("class:Step", 
            propKeyId, props(propKeyId), 
            propKeyAdminId, props(propKeyAdminId),
            propKeyKind, props(propKeyKind))
        graph.commit
        new SuccessfulStatus("object Step with " + vertex.getId + vertex.getProperties + " was created", "")
    }else{
      new WarningStatus("object Step with " + props(propKeyId) + "already exist", "")
    }
  }
  
   def update(graph: OrientGraph, props: Map[String, String]){
    //TODO bessere such Methode
    if(graph.getVertices("stepId", props("id")).size == 0){
//        graph.addVertex("class:Step", "stepId", props("id"))
//        graph.commit
      new WarningStatus("object Step with " + props("id") + "cannot update because not exist", "")
    }else{
      new SuccessfulStatus("object Step with " + props("id") + " was updated", "")
     
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