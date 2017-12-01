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
import models.currentConfig.CurrentConfig
import models.currentConfig.StepCurrentConfig
import models.wrapper.common.Component
import models.status.common.Successful
import models.status.RequireComponent
import models.status.ExcludeComponent
import models.status.RequireNextStep
import models.status.AllowNextComponent
import models.status.common.Error
import models.status.AllowNextComponent
import models.status.RequireComponent
import models.status.AllowNextComponent
import models.status.RequireComponent
import models.status.RequireComponent
import models.status.SelectionCriteriumStatus
import models.status.SelectionCriteriumStatus
import models.json.component.JsonComponentOut
import models.status.common.ClassCastError
import models.status.common.ODBReadError

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
      
      Logger.info(this.getClass.getSimpleName + " dependencies : " + dependencies)
      
      val excludeDependencies: List[Dependency] = dependencies filter {_.dependencyType == PropertyKey.EXCLUDE}
      
      Logger.info(this.getClass.getSimpleName + " excludeDependencies : " + excludeDependencies)
      
      val requireDependencies: List[Dependency] = dependencies filter {_.dependencyType == PropertyKey.REQUIRE}
      
      Logger.info(this.getClass.getSimpleName + " requireDependencies : " + requireDependencies)
      
      val vFatherStep: OrientVertex = getFatherStep(vComponent)
      
      Logger.info(this.getClass.getSimpleName + ": fatherStep " + vFatherStep)
      
      val selectionCriterium: SelectionCriterium = getSelectionCriterium(vFatherStep)
      
      Logger.info(this.getClass.getSimpleName + ": selectionCriterium " + selectionCriterium)
      
      val currentConfig: Option[StepCurrentConfig] = CurrentConfig.getCurrentConfig
      
      val previousSelectedComponents: List[Component] = currentConfig match {
        case Some(step) => step.components
        case None => List()
      }
      
      val stausSelectionCriterium: SelectionCriteriumStatus = 
        checkSelectionCriterium(previousSelectedComponents.size, selectionCriterium)
      
      Logger.info(this.getClass.getSimpleName + ": " + stausSelectionCriterium)
      
      val currentStep: Option[StepCurrentConfig] = CurrentConfig.getCurrentStep(vFatherStep.getIdentity.toString)
      
      stausSelectionCriterium match {
        case status: RequireComponent => {
          val component: Component = Component(
              vComponent.getIdentity.toString,
              vComponent.getProperty(PropertyKey.NAME_TO_SHOW)
          )
          CurrentConfig.addComponent(currentStep.get, component)
          
          ComponentOut(
               status.status,
               status.message,
               requireDependencies ::: excludeDependencies
           )
        }
        case status: RequireNextStep => {
           ComponentOut(
               status.status,
               status.message,
               requireDependencies ::: excludeDependencies
           )
        }
        case status: AllowNextComponent => {
          val component: Component = Component(
              vComponent.getIdentity.toString,
              vComponent.getProperty(PropertyKey.NAME_TO_SHOW)
          )
          CurrentConfig.addComponent(currentStep.get, component)
          
           ComponentOut(
               status.status,
               status.message,
               requireDependencies ::: excludeDependencies
           )
        }
        case status: ExcludeComponent => {
           ComponentOut(
               status.status,
               status.message,
               requireDependencies ::: excludeDependencies
           )
        }
      }
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        val status: Status = new ClassCastError
        ComponentOut(
               status.status,
               status.message,
               List()
           )
      }
      case e1: Exception => {
        graph.rollback()
        val status: Status = new ODBReadError
        Logger.error(e1.printStackTrace().toString)
        ComponentOut(
               status.status,
               status.message,
               List()
           )
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
    Logger.info(this.getClass.getSimpleName + ": " + eHasDependencies.head.getPropertyKeys.toString())
    eHasDependencies map {
      eHasDependency => {
        Logger.info(this.getClass.getSimpleName + ": " + eHasDependency.getProperties.toString())
        //TODO PropertyKey.VISUALIZATION in DB mit Leerzeichen
        Dependency(
            eHasDependency.getProperty(PropertyKey.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
            eHasDependency.getProperty(PropertyKey.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
            eHasDependency.getProperty(PropertyKey.VISUALIZATION).toString, //visualization: String,
            eHasDependency.getProperty(PropertyKey.DEPENDENCY_TYPE).toString, //dependencyType: String,
            eHasDependency.getProperty(PropertyKey.NAME_TO_SHOW).toString  //nameToShow: String
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
   * @param 
   * 
   * @return
   */
  
  def checkSelectionCriterium(countOfSelectedComponents: Int, selectionCriterium: SelectionCriterium): SelectionCriteriumStatus = {
    //Ungepruefte Komponente, die noch nicht in der aktuellen Konfiguration hinzugefuegt wird
    val countOfComponents = countOfSelectedComponents + 1
    val min = selectionCriterium.min
    val max = selectionCriterium.max
    
    Logger.info(this.getClass.getSimpleName + " min : " + min)
    Logger.info(this.getClass.getSimpleName + " max : " + max)
    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
    
    selectionCriterium match {
      case requireComponent if min > countOfComponents && max > countOfComponents => RequireComponent()
      case requireNextStep if min <= countOfComponents && max == countOfComponents => RequireNextStep()
      case allowNextComponent if min <= countOfComponents && max > countOfComponents => AllowNextComponent()
      case excludeComponent if max < countOfComponents => ExcludeComponent()
    }
  }
}