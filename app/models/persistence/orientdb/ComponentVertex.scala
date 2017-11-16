package models.persistence.orientdb

import scala.collection.JavaConverters._
import models.wrapper.component.ComponentIn
import models.wrapper.component.ComponentOut
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import models.persistence.OrientDB
import models.status.Status
import play.api.Logger
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.wrapper.dependency.Dependency
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Direction
import models.persistence.db.orientdb.PropertyKey
import models.wrapper.common.SelectionCriterium

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 13.11.2017
 */
object ComponentVertex {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param ComponentIn
   * 
   * @return ComponentOut
   */
  def component(componentIn: ComponentIn): ComponentOut = {
    val graph: OrientGraph = OrientDB.getFactory().getTx
    
    
    try{
      val vComponent = graph.getVertex(componentIn.componentId)
      
      val dependencies: List[Dependency] = getComponentDependenciesOut(vComponent)
      
      val vFatherStep: OrientVertex = getFatherStep(vComponent)
      
      val selectionCriterium: SelectionCriterium = getSelectionCriterium(vFatherStep)
      
      val currentConfig = ???
      
      val stausSelectionCriterium = checkSelectionCriterium
      
      ???
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        val status: Status = ???
        ???
      }
      case e1: Exception => {
        graph.rollback()
        val status: Status = ???
        Logger.error(e1.printStackTrace().toString)
        ???
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param OrientVertex
   * 
   * @return List[Dependency]
   */
  def getComponentDependenciesOut(vComponent: OrientVertex): List[Dependency] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.OUT, PropertyKey.HAS_DEPENDENCY)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    
    eHasDependencies map {
      eHasDependency => {
        Dependency(
            eHasDependency.getProperty(PropertyKey.OUT), //out: String,
            eHasDependency.getProperty(PropertyKey.IN), //in: String,
            eHasDependency.getProperty(PropertyKey.VISUALIZATION), //visualization: String,
            eHasDependency.getProperty(PropertyKey.DEPENDENCY_TYPE), //dependencyType: String,
            eHasDependency.getProperty(PropertyKey.NAME_TO_SHOW)  //nameToShow: String
        )
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getFatherStep(component: OrientVertex): OrientVertex = {
    val eHasComponents: List[OrientEdge] = component.getEdges(Direction.IN, PropertyKey.HAS_COMPONENT)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    
    val vFatherStep: List[OrientVertex] = eHasComponents map {
      eHasComponent => {
        eHasComponent.getVertex(Direction.OUT)
      }
    }
    vFatherStep.head
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getSelectionCriterium(vStep: OrientVertex): SelectionCriterium = {
    SelectionCriterium(
        vStep.getProperty(PropertyKey.SELECTION_CRITERIUM_MIN).toString.toInt, //min: Int,
        vStep.getProperty(PropertyKey.SELECTION_CRITERIUM_MAX).toString.toInt //max: Int
    )
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  
  def checkSelectionCriterium = {
    ???
  }
}