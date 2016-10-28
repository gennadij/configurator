/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import org.configTree.component.Component
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import org.status.Status

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
  	  new SuccessfulStatus(s"Class $classname was created", "")
    }else{
      new WarningStatus(s"Class $classname was already created", "")
    }
  }
  
  def connect(stepId: String, components: Seq[Component]): List[Status] = {
    val graph: OrientGraph = OrientDB.getGraph
    val status: List[Status] = List.empty
    val st = components.foreach(c => {
      if(graph.getEdges("hasComponentId", c.id).size == 0){
    	  val eHasComponent: OrientEdge = graph.addEdge(s"class:$classname", 
    			  graph.getVertices(propKeyStepId, stepId).head, 
    			  graph.getVertices(propKeyComponentId, c.id).head, 
    			  classname)
    		eHasComponent.setProperty("hasComponentId", c.id)
    		graph.commit
    		status.::( new SuccessfulStatus("Edge with id = " + c.id + " was created", ""))
      }else{
        status.::(new WarningStatus("Edge with id = " + c.id + " already exist", ""))
      }
    })
    status
  }
  
  def add(adminId: String, outStep: String, inComponents: List[String]): Status = {
     val graph: OrientGraph = OrientDB.getGraph
     val hasComponentIds = inComponents.map ({ iC =>
       val eHasComponent: OrientEdge = graph.addEdge("class:hasComponent", 
         graph.getVertex(outStep), 
          graph.getVertex(iC), 
         "hasComponent")
       eHasComponent.setProperty("adminId", adminId)
       eHasComponent.setProperty("hasComponentId", "S" + outStep + "C" + iC )
    	 graph.commit
    	 "S" + outStep + "C" + iC
     })
     
     new SuccessfulStatus("added hasComponents",hasComponentIds.toString())
  }
  
  def add(adminId: String, outStep: String, inComponent: String): Status = {
     val graph: OrientGraph = OrientDB.getGraph
     val eHasComponent: OrientEdge = graph.addEdge("class:hasComponent", 
       graph.getVertex(outStep), 
        graph.getVertex(inComponent), 
       "hasComponent")
     eHasComponent.setProperty("adminId", adminId)
     eHasComponent.setProperty("hasComponentId", "S" + outStep + "C" + inComponent )
  	 graph.commit
     
     new SuccessfulStatus("added hasComponents", "S" + outStep + "C" + inComponent)
  }
}