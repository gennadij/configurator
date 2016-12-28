package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.dto.startConfig.StartConfigCS
import org.dto.startConfig.StartConfigSC
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.orientechnologies.orient.core.sql.OCommandSQL
import org.dto.startConfig.StartConfigResult
import org.dto.Step
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Direction
import org.dto.Component
import org.dto.nextStep.NextStepCS
import org.dto.nextStep.NextStepSC
import org.dto.nextStep.NextStepResult


object StepVertex {
  
  def firstStep(configId: String): StartConfigSC = {
    val graph: OrientGraph = OrientDB.getGraph()
    
    val resFirstStep: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"select from Step where adminId='$configId' and kind='first'")).execute()
    val firstStep = resFirstStep.toList.map(_.asInstanceOf[OrientVertex])
    
    new StartConfigSC(
        result = new StartConfigResult(
            configId,
            new Step(
                firstStep(0).getIdentity.toString,
                firstStep(0).getProperty("kind"),
                components(firstStep(0))
            )
        )
    )
  }
  
  def nextStep(nextStepCS: NextStepCS): NextStepSC = {
    val graph: OrientGraph = OrientDB.getGraph()
    
    val configId: String = nextStepCS.params.configId
    
    //select Component where adminId=''
    
    val vComponents: List[OrientVertex] = nextStepCS.params.componentIds.map(compId => {
      graph.getVertex(compId)
    })
    
    //TODO lese die nextStep und vergleiche miteinander. Die StepIds sollen gleich sein.
    // das sollte man befor first Step geschickt wird geprüft. Andere Möglichkeit dass beim Admin zu prüfen.
    
    val resNextStep: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"select from Step where adminId='$configId' and kind='first'")).execute()
    val nextStep = resNextStep.toList.map(_.asInstanceOf[OrientVertex])
    
    new NextStepSC(
        result = new NextStepResult(
            configId,
            new Step(
                "",
                "",
                List(new Component("","",""))
            )
            
        )
    )
  }
  
  def components(step: OrientVertex): List[Component] = {
    val eHasComponents: List[Edge] = step.getEdges(Direction.OUT).toList
    val vComponents: List[Vertex] = eHasComponents.map(_.getVertex(Direction.IN))
    vComponents.map(vC => {
      new Component(
          vC.getId.toString,
          vC.getProperty("kind"),
          getNextStep(vC)
      )
    })
  }
  
  private def getNextStep(component: Vertex): String = {
    val eNextStep: List[Edge] = component.getEdges(Direction.OUT).toList
    val vNextStep: List[Vertex] = eNextStep.map ( { eNS => 
      eNS.getVertex(Direction.IN)
    })
    if(vNextStep.size == 1) vNextStep.head.getId.toString() else "no nextStep"
  }
  
  
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
  
//  def addStep(adminStep: AdminStep): AdminStep = {
//    val graph: OrientGraph = OrientDB.getGraph()
//    val vStep: OrientVertex = graph.addVertex("class:Step", 
//            "adminId", adminStep.adminId,
//            "kind", adminStep.kind)
//        graph.commit
//        vStep.setProperty("stepId", "S" + vStep.getIdentity.toString())
//        graph.commit
//        
//        new AdminStep(
//            vStep.getIdentity.toString(),
//            "S" + vStep.getIdentity.toString(),
//            vStep.getProperty("adminId"),
//            vStep.getProperty("kind")
//        )
//  }
  
//    def addStep(adminStep: AdminNextStep): AdminNextStep = {
//    val graph: OrientGraph = OrientDB.getGraph()
//    val vStep: OrientVertex = graph.addVertex("class:Step", 
//            "adminId", adminStep.adminId,
//            "kind", adminStep.kind)
//        graph.commit
//        vStep.setProperty("stepId", "S" + vStep.getIdentity.toString())
//        graph.commit
//        
//        new AdminNextStep(
//            true,
//            vStep.getIdentity.toString(),
//            "S" + vStep.getIdentity.toString(),
//            vStep.getProperty("adminId"),
//            vStep.getProperty("kind"), 
//            null,
//            ""
//        )
//  }
  
  
//  def create(props: Map[String, String]) = {
//    val graph: OrientGraph = OrientDB.getGraph()
//    if(graph.getVertices(propKeyId, props(propKeyId)).size == 0){
//        val vertex: OrientVertex = graph.addVertex("class:Step", 
//            propKeyId, props(propKeyId), 
//            propKeyAdminId, props(propKeyAdminId),
//            propKeyKind, props(propKeyKind))
//        graph.commit
//        new SuccessfulStatus("object Step with " + vertex.getId + vertex.getProperties + " was created", "")
//    }else{
//      new WarningStatus("object Step with " + props(propKeyId) + "already exist", "")
//    }
//  }
  
//   def update(graph: OrientGraph, props: Map[String, String]){
//    //TODO bessere such Methode
//    if(graph.getVertices("stepId", props("id")).size == 0){
////        graph.addVertex("class:Step", "stepId", props("id"))
////        graph.commit
//      new WarningStatus("object Step with " + props("id") + "cannot update because not exist", "")
//    }else{
//      new SuccessfulStatus("object Step with " + props("id") + " was updated", "")
//     
//    }
//  }
//  
//  def get() = ???
//}
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
  } 