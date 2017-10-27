package models.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.Edge
import models.json.startConfig.JsonStartConfigOut
import models.json.startConfig.JsonStartConfigIn
import models.persistence.OrientDB


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
  def firstStep(startConfigIn: JsonStartConfigIn): JsonStartConfigOut = {
    val graph: OrientGraph = OrientDB.getGraph()
    
    val configUrl: String = startConfigIn.params.configUrl
    
    val sql: String = s"select from Config where configUrl='$configUrl'"
    val resConfigs: OrientDynaElementIterable = graph
      .command(new OCommandSQL(sql)).execute()
    val vConfigs: List[OrientVertex] = resConfigs.toList.map(_.asInstanceOf[OrientVertex])
    //TODO error bei der schon exestierenden configUrl, wenn db mehrere configs findet.
    // das Problem soll beim Admin geloest werden.
    val vConfig: OrientVertex = vConfigs(0)
    
    val eHasConfig: List[Edge] = vConfig.getEdges(Direction.OUT, "hasFirstStep").toList
    
    //TODO error wenn mehrere Edges gefunden werden. DB seitig speren. Mur einen Edge an den Config erlaubt.
    // es wird bei der Admin ausgeschlossen
    val vFirstStep: OrientVertex = eHasConfig(0).getVertex(Direction.IN).asInstanceOf[OrientVertex]
    
    StartConfigSC(
        result = StartConfigResult(
            true,
            "FirstStep",
            Step(
                vFirstStep.getIdentity.toString,
                vFirstStep.getProperty(PropertyKey.NAME_TO_SHOW),
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

    // hole Vertex von selectedComponent aus der DB
    val vSelectedComponents: List[OrientVertex] = nextStepCS.params.componentIds map {
      componentId => {
        graph.getVertex(componentId)
      }
    }
    
    // hole Edge from hasStep von selectedComponents aus der DB
    val eHasStepFromSelectedComponents: List[OrientEdge] = 
      vSelectedComponents(0).getEdges(Direction.IN, "hasComponent").toList map {_.asInstanceOf[OrientEdge]}
    
    //TODO es darf nur einen Step gefunden werden
    // Das wird beim Admin ausgeschlossen
    val vSelectedStep: OrientVertex = eHasStepFromSelectedComponents(0)
      .getVertex(Direction.OUT).asInstanceOf[OrientVertex]
    
    
    val eHasStep: List[OrientEdge] = vSelectedComponents flatMap {
      vComponent => {
        vComponent.getEdges(Direction.OUT, "hasStep") map {
          _.asInstanceOf[OrientEdge]
        }
      }
    }
    
    val vNextSteps = eHasStep map {
      _.getVertex(Direction.IN)
    }
    
    // Admin schliesst aus, dass mehrere nextStep exestieren können
    
    //CURRENT_CONFIG
    
    val selectedComponents: List[Component] = getSelectedComponents(vSelectedStep, nextStepCS.params.componentIds)
    
    val currentConfigStep = Step(vSelectedStep.getIdentity.toString, vSelectedStep.getProperty(PropertyKey.NAME_TO_SHOW), 
        selectedComponents map {c => Component(
            c.componentId, 
            c.nameToShow
        )})
        
    CurrentConfig.setCurrentConfig(nextStepCS.params.clientId, currentConfigStep)
    
    //TODO v0.1.0 Definition FinalStep und Abschluss der Konfiguration
    
    
    if(vSelectedStep.getProperty(PropertyKey.KIND).toString() == "final") {
       NextStepSC(
          status = Status(
              "final",
              1,
              "Die Konfiguration ist fertig"
          ),
          result = NextStepResult(
              Step(
                  "",
                  "",
                  List[Component]()
              )
          )
      )
    } else {
      NextStepSC(
          status = Status(
              "ok",
              1,
              "Der naechste Schrit mit Komponenten wurde erfolgreich geladen"
          ),
          result = NextStepResult(
              Step(
                  vNextSteps(0).getIdentity.toString,
                  vNextSteps(0).getProperty(PropertyKey.NAME_TO_SHOW),
                  components(vNextSteps(0))
              )
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
    val eHasComponents: List[Edge] = step.getEdges(Direction.OUT).toList
    val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
    vComponents.map(vC => {
      new Component(
          vC.getIdentity.toString,
          vC.getProperty(PropertyKey.NAME_TO_SHOW)
      )
    })
  }
}