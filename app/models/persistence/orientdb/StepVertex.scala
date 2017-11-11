package models.persistence.db.orientdb

import scala.collection.JavaConverters._
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.Edge
import models.persistence.OrientDB
import models.wrapper.startConfig.StartConfigIn
import models.wrapper.startConfig.StartConfigOut
import com.tinkerpop.blueprints.Direction
import models.wrapper.common.Step
import models.wrapper.nextStep.NextStepIn
import models.wrapper.nextStep.NextStepOut
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import models.wrapper.common.Component
import models.currentConfig.CurrentConfig
import models.status.StartConfigSuccessful
import models.status.StartConfigSuccessful
import models.status.Status
import models.status.StartConfigODBWriteError
import com.tinkerpop.blueprints.Vertex
import play.api.Logger
import models.status.common.ClassCastError
import models.status.NextStepSuccessful
import models.status.NextStepODBWriteError
import models.status.NextStepFinalStep


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 06.10.2016
 */

object StepVertex {

  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StartConfigIn
   * 
   * @return StartConfigOut
   */
  def firstStep(startConfigIn: StartConfigIn): StartConfigOut = {
    val graph: OrientGraph = OrientDB.getFactory().getTx
    
    val configUrl: String = startConfigIn.configUrl
    
    try{
      
      //TODO DOcu aus der OrientDB
      //Without an index against the property name, this query can take up a lot of time. You can improve performance by creating a new index against the name property:
      //http://orientdb.com/docs/last/Graph-VE.html
      
      val vConfigs: List[Vertex] = graph.getVertices("configUrl", configUrl).asScala.toList
      
      val eHasConfig: List[Edge] = vConfigs.head.getEdges(Direction.OUT, "hasFirstStep").asScala.toList
      val vFirstStep: OrientVertex = eHasConfig.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]
      val status: Status = new StartConfigSuccessful
      StartConfigOut(
          Some(Step(
              vFirstStep.getIdentity.toString,
              vFirstStep.getProperty(PropertyKey.NAME_TO_SHOW),
              components(vFirstStep)
          )),
          status.status,
          status.message
      )
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        val status: Status = new ClassCastError
        StartConfigOut(
            None,
            status.status,
            status.message
        )
      }
      case e1: Exception => {
        graph.rollback()
        val status: Status = new StartConfigODBWriteError
        StartConfigOut(
            None,
            status.status,
            status.message
        )
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param NextStepIn
   * 
   * @return NextStepOut
   */
  def nextStep(nextStepIn: NextStepIn): NextStepOut = {
    val graph: OrientGraph = OrientDB.getFactory().getTx

    try{
      
      // hole Vertices von selectedComponents aus der DB
      // TODO Die Implenmentierung bezieht sich zurzeit nur auf die Singelchooce
      // Die Multichooce wird spaeter implementiert. Es muss zuerst auf dem Admin-Seite vorbereitet werden
      val vSelectedComponents: List[OrientVertex] = nextStepIn.componentIds map {
        componentId => {
          graph.getVertex(componentId)
        }
      }
      
      val vNextStep: Option[OrientVertex] = getStepFromSelectedComponent(vSelectedComponents.head)
      
      vNextStep match {
        case Some(step) => {
          val status = new NextStepSuccessful
            NextStepOut(
                status.status,
                status.message,
                Some(
                    Step(
                        step.getIdentity.toString,
                        step.getProperty(PropertyKey.NAME_TO_SHOW),
                        components(step)
                    )
                )
            )
        }
        case None => {
          val status = new NextStepFinalStep
          NextStepOut(
                status.status,
                status.message,
                None
            )
        }
      }
    }catch{
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString())
        val status: Status = new NextStepODBWriteError
        NextStepOut(
            status.status,
            status.message,
            None
        )
      }
    }
  }
    
    
    
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.0
   * 
   * @param
   * 
   * @return
   */
  def getSelectedComponents(selectedStep: OrientVertex, selectedIdsOfComponent: List[String]): List[Component] = {
    
    val componentsOfSelectedStep: List[Component] = components(selectedStep)
    
    componentsOfSelectedStep.filter(cOfSS => {
      selectedIdsOfComponent.contains(cOfSS.componentId)
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
    val eHasComponents: List[Edge] = step.getEdges(Direction.OUT).asScala.toList
    val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
    vComponents.map(vC => {
      new Component(
          vC.getIdentity.toString,
          vC.getProperty(PropertyKey.NAME_TO_SHOW)
      )
    })
  }
  
  def getStepFromSelectedComponent(vSelectedComponent:OrientVertex): Option[OrientVertex] = {
    
      // hole Edge from hasStep von selectedComponents aus der DB
      val eHasStepFromSelectedComponents: List[OrientEdge] = 
          vSelectedComponent.getEdges(Direction.OUT, PropertyKey.HAS_STEP).asScala.toList map {_.asInstanceOf[OrientEdge]}
      eHasStepFromSelectedComponents match {
        case List() => None
        case _ => {
          // hole angehaengete Schritt aus der DB
          Some(eHasStepFromSelectedComponents.head.getVertex(Direction.IN).asInstanceOf[OrientVertex])
        }
      }
  }
}