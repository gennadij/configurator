package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import org.configTree.component.Component
import com.tinkerpop.blueprints.impls.orient.OrientEdge

object HasComponentEdge {
  
  val classname = "hasComponent"
  val propKeyHasComponentId = "hasComponentId"
  val propKeyAdminId = "adminId"
  val propKeyComponentId = "componentId"
  val propKeyStepId = "stepId"
  
  def createSchema(){
    val graph: OrientGraph = OrientDB.getGraph
    if(graph.getEdgeType(classname) == null){
      val eHasComponent: OrientEdgeType = graph.createEdgeType(classname)
      eHasComponent.createProperty(propKeyHasComponentId, OType.STRING)
      eHasComponent.createProperty(propKeyAdminId, OType.STRING)
      graph.commit
  	  new SuccessfulStatus(s"Class $classname was created")
    }else{
      new WarningStatus(s"Class $classname was already created")
    }
  }
  
  def connect(stepId: String, components: Seq[Component]) = {
    val graph: OrientGraph = OrientDB.getGraph
    components.foreach(c => {
      if(graph.getEdges("hasComponentId", c.id).size == 0){
    	  val eHasComponent: OrientEdge = graph.addEdge(s"class:$classname", 
    			  graph.getVertices(propKeyStepId, stepId).head, 
    			  graph.getVertices(propKeyComponentId, c.id).head, 
    			  classname)
    		eHasComponent.setProperty("hasComponentId", c.id)
    		graph.commit
    		new SuccessfulStatus("Edge with id = " + c.id + " was created")
      }else{
        new WarningStatus("Edge with id = " + c.id + " already exist")
      }
    })
  }
}