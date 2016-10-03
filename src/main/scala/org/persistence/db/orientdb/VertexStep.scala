package org.persistence.db.orientdb

import org.configTree.step.NextStep
import org.configTree.step.SelectionCriterium
import org.configTree.step.Source
import org.configTree.component.Component
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import org.configTree.step.Dependency
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientEdge

class VertexStep(
                              val id: String,
                              val nameToShow: String,
                              val fatherStep: String,
                              val nextStep: Seq[NextStep],
                              val components: Seq[Component],
                              val dependencies: List[Dependency]){

  def persistStep(graph: OrientGraph) = {
    
    if(graph.getVertexType("Step") == null){
      val vStep: OrientVertexType = graph.createVertexType("Step")
      vStep.createProperty("stepId", OType.STRING)
      graph.commit
    }
    if(graph.getVertexType("Component") == null ){
      val vComponent: OrientVertexType = graph.createVertexType("Component")
      vComponent.createProperty("componentId", OType.STRING)
      graph.commit
    }
    if(graph.getEdgeType("NextStep") == null){
    	val eNextStep: OrientEdgeType = graph.createEdgeType("nextStep")
    	eNextStep.createProperty("nextStepId", OType.STRING)
    	graph.commit
    }
    
    if(graph.getEdgeType("hasComponent") == null){
      val eHasComponent: OrientEdgeType = graph.createEdgeType("hasComponent")
      eHasComponent.createProperty("hasComponentId", OType.STRING)
      graph.commit
    }
    
    if(graph.getVertices("stepId", id).size == 0){
        val vStep: Vertex = graph.addVertex("class:Step", "stepId", id)
    }else{
      println(s"step with id $id exist")
    }
    
    components.foreach ( c => {
      if(graph.getVertices("componentId", c.id).size == 0){
        graph.addVertex("class:Component", "componentId", c.id)
        graph.commit()
      }else{
        println(s"$c exist")
      }
    })
    
    nextStep.foreach ( nS => {
      if(graph.getVertices("stepId", nS.step).size == 0){
        val vStep: Vertex = graph.addVertex("class:Step", "stepId", nS.step)
        val vComponent: Vertex = graph.getVertices("componentId", nS.byComponent).head
        
        graph.commit()
      }else{
        println(s"step with id $nS exist")
      }
    })
    
    components.foreach(c => {
      if(graph.getEdges("hasComponentId", c.id).size == 0){
    	  val eHasComponent: OrientEdge = graph.addEdge("class:hasComponent", 
    			  graph.getVertices("stepId", id).head, 
    			  graph.getVertices("componentId", c.id).head, 
    			  "hasComponent")
    		eHasComponent.setProperty("hasComponentId", c.id)
    		graph.commit
      }else{
        println(s"$c  exist")
      }
    })
    
    nextStep.foreach(nS => {
      if(graph.getEdges("nextStepId", nS.byComponent + nS.step).size == 0){
    	  val eNextStep: OrientEdge = graph.addEdge("class:nestStep", 
    			  graph.getVertices("componentId", nS.byComponent).head, 
    			  graph.getVertices("stepId", nS.step).head, "nextStep")
    		eNextStep.setProperty("nextStepId", nS.byComponent + nS.step)
        graph.commit
      }else{
        println(s"$nS exist")
      }
    })
    
//    graph.getVertices().foreach(v => println(v.getProperty("stepId")))
    
//      val res: OrientDynaElementIterable = graph.command(
//        new OCommandSQL(s"SELECT FROM Step WHERE stepId='S000001'")).execute()
  }
}