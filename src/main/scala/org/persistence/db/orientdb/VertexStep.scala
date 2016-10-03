package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.Vertex


object VertexStep{
  def create(graph: OrientGraph, stepId: String){
    val vStep = new VertexStep(stepId)
  }
}

class VertexStep (stepId: String){
  
  val propStep = "Step"
  val propStepId = "stepId"
  
  private def create(graph: OrientGraph, props: Map[String, String]){
    if(graph.getVertices("stepId", props("id")).size == 0){
        graph.addVertex("class:Step", "stepId", props("id"))
        graph.commit
        new SuccessfulStatus("object Step with " + props("id") + " was created")
    }else{
      new WarningStatus("object Step with " + props("id") + "already exist")
    }
  }
  
  private def createSchema(graph: OrientGraph){
    if(graph.getVertexType(propStep) == null){
      val vStep: OrientVertexType = graph.createVertexType("Step")
      vStep.createProperty(propStepId, OType.STRING)
      graph.commit
      new SuccessfulStatus("class Step was created")
    }else {
      new WarningStatus("class Step already exist")
    }
  }
  
  private def update(graph: OrientGraph, props: Map[String, String]){
    //TODO bessere such Methode
    if(graph.getVertices("stepId", props("id")).size == 0){
//        graph.addVertex("class:Step", "stepId", props("id"))
//        graph.commit
      new WarningStatus("object Step with " + props("id") + "cannot update because not exist")
    }else{
      new SuccessfulStatus("object Step with " + props("id") + " was updated")
     
    }
  }
}