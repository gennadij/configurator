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
    
    val eHasConfig: List[Edge] = vConfig.getEdges(Direction.OUT, "hasConfig").toList
    
    //TODO error wenn mehrere Edges gefunden werden. DB seitig speren. Mur einen Edge an den Config erlaubt. 
    val vFirstStep: OrientVertex = eHasConfig(0).getVertex(Direction.IN).asInstanceOf[OrientVertex]
    
    StartConfigSC(
        result = StartConfigResult(
            true,
            "FirstStep",
            Step(
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
//  def nextStep(nextStepCS: NextStepCS): NextStepSC = {
//    val graph: OrientGraph = OrientDB.getGraph()
//    
//    val configId: String = nextStepCS.params.configId
//    
//    /*
//     * -- Pruefe selectionCriterium wenn Component kind=mutable
//     * -- 
//     */
//    
//    
//     /*
//      * Component -> [NextStep]
//      * 
//      * hole NextStep Vertex von db
//      */
//    
//    val vNextSteps: List[OrientVertex] = nextStepCS.params.componentIds.map(cId => {
//      val sqlNextStep: String = 
//        s"select expand(out('nextStep')) from Component where adminId='$configId' and @rid='$cId'"
//      println(sqlNextStep)
//      val resNextStep: OrientDynaElementIterable = graph
//      .command(new OCommandSQL(sqlNextStep)).execute()
//      val nextSteps: List[OrientVertex] = resNextStep.toList.map(_.asInstanceOf[OrientVertex])
//      println(nextSteps)
//      if(nextSteps.size == 1) nextSteps(0) else null
//    })
//    println(vNextSteps)
//    val nextStepIds = vNextSteps.map(_.getId.toString)
//    
//    if(compareElemInList(nextStepIds)){
//      val nextStepId: String = vNextSteps(0).getId.toString
//      val sqlComponents: String = 
//        s"select expand(out('hasComponent')) from Step where adminId='$configId' and @rid='$nextStepId'"
//      val resComponents: OrientDynaElementIterable = graph
//      .command(new OCommandSQL(sqlComponents)).execute()
//      
//      val vComponents: List[OrientVertex] = resComponents.toList.map(_.asInstanceOf[OrientVertex])
//      
//      val components: List[Component] = vComponents.map(vC => {
//        new Component(
//            vC.getIdentity.toString,
//            vC.getProperty("kind")
//        )
//      })
//      
//      new NextStepSC(
//        status = new Status(
//            "ok",
//            0,
//            "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
//        ),
//        result = new NextStepResult(
//            configId,
//            new Step(
//                vNextSteps(0).getId.toString,
//                vNextSteps(0).getProperty("kind"),
//                components
//            )
//        )
//     )
//    }else{
//      new NextStepSC(
//        status = new Status(
//            "error",
//            ErrorIds.selectedComponentsHasVariosNextStep,
//            ErrorStrings.selectedComponentsHasVariosNextStep
//        ),
//        result = new NextStepResult(
//            configId,
//            new Step(
//                "",
//                "",
//                List.empty
//            )
//        )
//      )
//    }
//  }

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
    val vComponents: List[Vertex] = eHasComponents.map(_.getVertex(Direction.IN))
    vComponents.map(vC => {
      new Component(
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