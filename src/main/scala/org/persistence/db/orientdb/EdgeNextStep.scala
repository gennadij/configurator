package org.persistence.db.orientdb

import scala.collection.JavaConversions._
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import org.configTree.step.NextStep


object EdgeNextStep {
  
  val classname = "NextStep"
  val propKeyNextStepId = "nextStepId"
  val propKeyAdminId = "adminId"
  
  def createSchema(){
    val graph: OrientGraph = OrientDB.getGraph
    if(graph.getEdgeType("NextStep") == null){
  	  val eNextStep: OrientEdgeType = graph.createEdgeType(classname)
//  	  eNextStep.createProperty(propKeyNextStepId, OType.STRING)
//  	  eNextStep.createProperty(propKeyAdminId, OType.STRING)
  	  graph.commit
  	  new SuccessfulStatus("")
    }else{
      WarningStatus("")
    }
  }
  
  def create(nextSteps: List[NextStep]) = {
    val graph: OrientGraph = OrientDB.getGraph
    nextSteps.foreach ( nS => {
      if(graph.getVertices("stepId", nS.step).size == 0){
        val vStep: Vertex = graph.addVertex("class:Step", "stepId", nS.step)
        val vComponent: Vertex = graph.getVertices("componentId", nS.byComponent).head
        graph.commit()
      }else{
        println(s"step with id $nS exist")
      }
    })
  }

  
}