package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType

object GlobalConfigVertex {
  
  def create = {
    val graph: OrientGraph = OrientDB.getGraph
    if(graph.getVertices("", "").size == 0){
        graph.addVertex("class:Step", "", "")
        graph.commit
        new SuccessfulStatus("object Step with " + "" + " was created", "")
    }else{
      new WarningStatus("object Step with " + "" + "already exist", "")
    }
  }
  
  def createSchema = {
    val graph: OrientGraph = OrientDB.getGraph
    if(graph.getVertexType("") == null){
      val vStep: OrientVertexType = graph.createVertexType("Step")
      vStep.createProperty("", OType.STRING)
      vStep.createProperty("", OType.STRING)
      graph.commit
      new SuccessfulStatus("class Step was created", "")
    }else {
      new WarningStatus("class Step already exist", "")
    }
  }
  
  
  
  
}