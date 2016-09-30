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

class VertexStep(
                              val id: String,
                              val nameToShow: String,
                              val fatherStep: String,
                              val nextStep: Seq[NextStep],
                              val components: Seq[Component],
                              val dependencies: List[Dependency]){
  
  
  
  def persistStep(graph: OrientGraph) = {
    
    if(graph.getVertex("Step") != null){
      val step: OrientVertexType = graph.createVertexType("Step")
      step.createProperty("id", OType.STRING)
      step.createProperty("nameToShow", OType.STRING)
      step.createProperty("fatherStep", OType.STRING)
    }else{
      graph.addVertex("Class:Step", "nameToShow", s"step $id", "fatherStep", fatherStep)
      
      val res: OrientDynaElementIterable = graph.command(
        new OCommandSQL(s"SELECT FROM Step WHERE id='$id'")).execute()
        res.foreach(v => {println(v)})
    }
  }
}