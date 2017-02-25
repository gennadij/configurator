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
import org.dto.Status
import org.status.ErrorIds
import org.status.ErrorStrings
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import org.dto.CurrentConfig

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 06.10.2016
 */

object StepVertex {

  /**
   * @author Gennadi Heimann
   * 
   * @version 0.1.0
   * 
   * @param
   * 
   * @return
   */
  def firstStep(startConfigCS: StartConfigCS): StartConfigSC = {
    val graph: OrientGraph = OrientDB.getGraph()
    
    val configUrl: String = startConfigCS.params.configUrl
    
    val sql: String = s"select from Config where configUrl='$configUrl'"
    val resConfigs: OrientDynaElementIterable = graph
      .command(new OCommandSQL(sql)).execute()
    val vConfigs: List[OrientVertex] = resConfigs.toList.map(_.asInstanceOf[OrientVertex])
    //TODO error bei der schon exestierenden configUrl, wenn db mehrere configs findet.
    val vConfig: OrientVertex = vConfigs(0)
    
    val eHasConfig: List[Edge] = vConfig.getEdges(Direction.OUT, "hasFirstStep").toList
    
    //TODO error wenn mehrere Edges gefunden werden. DB seitig speren. Mur einen Edge an den Config erlaubt. 
    val vFirstStep: OrientVertex = eHasConfig(0).getVertex(Direction.IN).asInstanceOf[OrientVertex]
    
    StartConfigSC(
        result = StartConfigResult(
            true,
            "FirstStep",
            Step(
                vFirstStep.getIdentity.toString,
                "First Step",
                components(vFirstStep)
            )
        )
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.1.0
   * 
   * @param
   * 
   * @return
   */
  def nextStep(nextStepCS: NextStepCS): NextStepSC = {
    val graph: OrientGraph = OrientDB.getGraph()

    val vSelectedComponents: List[OrientVertex] = nextStepCS.params.componentIds map {
      componentId => {
        graph.getVertex(componentId)
      }
    }
    
    val eHasStepFromSelectedComponents: List[OrientEdge] = 
      vSelectedComponents(0).getEdges(Direction.IN, "hasComponent").toList map {_.asInstanceOf[OrientEdge]}
    
    val vSelectedStep: OrientVertex = eHasStepFromSelectedComponents(0)
      .getVertex(Direction.OUT).asInstanceOf[OrientVertex]
    
    
    val eHasStep: List[OrientEdge] = vSelectedComponents flatMap {
      vComponent => {
        vComponent.getEdges(Direction.OUT, "hasStep") map {
          _.asInstanceOf[OrientEdge]
        }
      }
    }
    println(eHasStep)
    
    val vNextSteps = eHasStep map {
      _.getVertex(Direction.IN)
    }
    
    //TODO pruefe ob in der vNextStep gleiche Steps -> verwende compareOneWithAll(list: Seq[OrientVertex]): Boolean
    
//    val eHasComponents: List[OrientEdge] = vNextSteps(0).getEdges(Direction.OUT, "hasComponent").toList map {
//      _.asInstanceOf[OrientEdge]
//    }
//    
//    val vComponentsOfNextStep: List[OrientVertex] = eHasComponents map {
//      eHasComponent => {
//        eHasComponent.getVertex(Direction.IN).asInstanceOf[OrientVertex]
//      }
//    }
    
    //CURRENT_CONFIG
    
    // Statefull connection 
    // in DB ablegen
    // über Client Schleifen lassen (bei stateless connection)
    
    
    NextStepSC(
        status = Status(
            "ok",
            1,
            "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
        ),
        result = NextStepResult(
            Step(
                vNextSteps(0).getIdentity.toString,
                "Next Step",
                components(vNextSteps(0))
            )
        )
    )
  }

  /**
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param
   * 
   * @return
   */
//  private def compareElemInList(list: Seq[String]) = {
//    list match {
//      case x :: rest => rest forall (_ == x)
//    }
//  }
  
    private def compareOneWithAll(list: Seq[OrientVertex]): Boolean = {
    list match {
      case firstVertex :: rest => rest forall (_ == firstVertex)
    }
  }

  /**
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param
   * 
   * @return
   */
  def components(step: OrientVertex): List[Component] = {
    val eHasComponents: List[Edge] = step.getEdges(Direction.OUT).toList
    val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
    vComponents.map(vC => {
      new Component(
          vC.getIdentity.toString,
          "Component"
      )
    })
  }

  /**
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param
   * 
   * @return
   */
  private def getNextStep(component: Vertex): String = {
    val eNextStep: List[Edge] = component.getEdges(Direction.OUT).toList
    val vNextStep: List[Vertex] = eNextStep.map ( { eNS => 
      eNS.getVertex(Direction.IN)
    })
    if(vNextStep.size == 1) vNextStep.head.getId.toString() else "no nextStep"
  }
}