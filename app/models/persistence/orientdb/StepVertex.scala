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
import models.status.startCongig.StartConfigSuccessful
import models.status.startCongig.StartConfigSuccessful
import models.status.Status
import com.tinkerpop.blueprints.Vertex
import play.api.Logger
import models.status.common.ClassCastError
import models.status.nextStep.NextStepSuccessful
import models.status.nextStep.FinalStepSuccessful
import models.status.common.ODBReadError
import models.status.common.ODBReadError
import models.wrapper.dependency.Dependency


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
        val status: Status = new ODBReadError
        Logger.error(e1.printStackTrace().toString)
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
      
      // Die Erstellung von NextStep muss nach der Pruefung der Dependencies erfolgen
      
      
      val vSelectedComponents: List[OrientVertex] = nextStepIn.componentIds map {
        componentId => {
          graph.getVertex(componentId)
        }
      }
      
      val vNextStep: Option[OrientVertex] = getStepFromSelectedComponent(vSelectedComponents.head)
      
      //lese die Abhaengigkeiten
      
      val dependencies: List[Dependency] = getDependencies(vSelectedComponents)
      
      vNextStep match {
        case Some(step) => {
          createNextStepOut(step)
        }
        case None => {
          createFinalStep
        }
      }
    }catch{
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString())
        createErrorStep
      }
    }
  }
    
    
    
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
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
   * @version 0.0.1
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
   * @version 0.0.1
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
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param
   * 
   * @return
   */
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
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param
   * 
   * @return
   */
  def createNextStepOut(step: OrientVertex): NextStepOut = {
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
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param
   * 
   * @return
   */
  def createFinalStep: NextStepOut = {
    val status = new FinalStepSuccessful
    NextStepOut(
          status.status,
          status.message,
          None
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param
   * 
   * @return
   */
  def createErrorStep: NextStepOut = {
    val status: Status = new ODBReadError
    NextStepOut(
        status.status,
        status.message,
        None
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param
   * 
   * @return
   */
  def getDependencies(components: List[OrientVertex]): List[Dependency] = {
    components map (component => {
        val eHasDependency: OrientEdge = component.getEdges(Direction.OUT, PropertyKey.HAS_DEPENDENCY).asInstanceOf[OrientEdge]
        Dependency(
            eHasDependency.getProperty(PropertyKey.OUT),
            eHasDependency.getProperty(PropertyKey.IN),
            eHasDependency.getProperty(PropertyKey.VISUALIZATION),
            eHasDependency.getProperty(PropertyKey.DEPENDENCY_TYPE),
            eHasDependency.getProperty(PropertyKey.NAME_TO_SHOW),
        )
      }
    )
  }
}