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
      // TODO nur einen FirstStep zu einen Config angehaengt werden kann
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

    // hole Vertex von selectedComponent aus der DB
    val vSelectedComponents: List[OrientVertex] = nextStepIn.componentIds map {
      componentId => {
        graph.getVertex(componentId)
      }
    }
    
    // hole Edge from hasStep von selectedComponents aus der DB
    val eHasStepFromSelectedComponents: List[OrientEdge] = 
      vSelectedComponents(0).getEdges(Direction.IN, "hasComponent").asScala.toList map {_.asInstanceOf[OrientEdge]}
    
    //TODO es darf nur einen Step gefunden werden
    // Das wird beim Admin ausgeschlossen
    val vSelectedStep: OrientVertex = eHasStepFromSelectedComponents(0)
      .getVertex(Direction.OUT).asInstanceOf[OrientVertex]
    
    
    val eHasStep: List[OrientEdge] = vSelectedComponents flatMap {
      vComponent => {
        vComponent.getEdges(Direction.OUT, "hasStep").asScala.toList map {
          _.asInstanceOf[OrientEdge]
        }
      }
    }
    
    val vNextSteps = eHasStep map {
      _.getVertex(Direction.IN)
    }
    
    // Admin schliesst aus, dass mehrere nextStep exestieren können
    
    //CURRENT_CONFIG
    
    val selectedComponents: List[Component] = getSelectedComponents(vSelectedStep, nextStepIn.componentIds)
    
    val currentConfigStep = Step(vSelectedStep.getIdentity.toString, vSelectedStep.getProperty(PropertyKey.NAME_TO_SHOW), 
        selectedComponents map {c => Component(
            c.componentId, 
            c.nameToShow
        )})
       //TODO client Id ist nicht mehr notwendig
//    CurrentConfig.setCurrentConfig(currentConfigStep)
    
    //TODO v0.1.0 Definition FinalStep und Abschluss der Konfiguration
    
    
    if(vSelectedStep.getProperty(PropertyKey.KIND).toString() == "final") {
       NextStepOut(
          status = ""
//            Status(
//              "final",
//              1,
//              "Die Konfiguration ist fertig"
//          )
          ,
          message = "",
              Step(
                  "",
                  "",
                  List[Component]()
              )
      )
    } else {
      NextStepOut(
          status = ""
//            Status(
//              "ok",
//              1,
//              "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
//          )
          ,message = ""
          ,
              Step(
                  vNextSteps(0).getIdentity.toString,
                  vNextSteps(0).getProperty(PropertyKey.NAME_TO_SHOW),
                  components(vNextSteps(0))
              )
      )
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
}