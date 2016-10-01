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
    	val eNextStep: OrientEdgeType = graph.createEdgeType("NextStep")
    	eNextStep.createProperty("byComponent", OType.STRING)
    	eNextStep.createProperty("nextStep", OType.STRING)
    	graph.commit
    }
    
    graph.getVertices().foreach(v => println(v.getProperty("stepId")))
    
    
    graph.addVertex("class:Step", "stepId", id)
      graph.commit()
      val res: OrientDynaElementIterable = graph.command(
        new OCommandSQL(s"SELECT FROM Step")).execute()
      res.foreach(v => {println(v)})
  }
}