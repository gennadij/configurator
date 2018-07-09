package models.persistence.orientdb

import scala.collection.JavaConverters._
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import models.persistence.Database
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.StepBO
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Direction
import play.api.Logger
import models.bo.ComponentBO
import models.bo.DependencyBO
import models.status.ODBClassCastError
import models.status.ODBReadError
import models.status.Success
import models.status.step.MultipleNextSteps
import models.status.step.NextStepNotExist
import models.status.step.NextStepExist
import models.status.step.StatusStep
import models.status.Status
import models.status.Status
import models.status.Error
import models.status.step.CommonErrorNextStep
import com.tinkerpop.blueprints.Vertex
import sun.awt.geom.Edge
import com.tinkerpop.blueprints.Edge
import models.status.step.FirstStepExist
import models.status.step.FirstStepNotExist
import models.status.step.MultipleFirstSteps
import models.currentConfig.CurrentConfig
import models.status.step.CommonErrorFirstStep
import models.status.step.FatherStepExist
import models.status.step.FatherStepNotExist
import models.status.step.CommonErrorFatherStep
import models.status.component.StatusComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.02.2018
 */

object Graph{
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getFatherStep(componentId: String): StepBO = {
    new Graph().getFatherStep(componentId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getComponent(componentId: String): ComponentBO = {
    new Graph().getComponent(componentId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getNextStep(componentId: String): StepBO = {
    new Graph().getNextStep(componentId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getFirstStep(configUrl: String): StepBO = {
    new Graph().getFirstStep(configUrl)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String
   * 
   * @return Option[List[ComponentBO]]
   */
  def getComponents(stepId: String): List[ComponentBO] = {
    new Graph().getComponents(stepId)
  }
  
}

class Graph {
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  private def getFatherStep(componentId: String): StepBO = {
    
    val graph: OrientGraph = Database.getFactory().getTx
    
    val vFatherStep: (Option[OrientVertex], Status) = try{
      val vComponent = graph.getVertex(componentId)
      
      val eHasComponents: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT)
          .asScala.toList map {_.asInstanceOf[OrientEdge]}
      
      val vFatherSteps: List[OrientVertex] = eHasComponents map {
        eHasComponent => {
          eHasComponent.getVertex(Direction.OUT)
        }
      }
      vFatherSteps match {
        case List() => (None, FatherStepNotExist())
        case _ => (Some(vFatherSteps.head), FatherStepExist())
      }
    
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
      }
    }
    
    (vFatherStep: @unchecked) match {
      case (Some(vFatherStep), FatherStepExist()) => {
        StepBO(
            vFatherStep.getIdentity.toString,
            vFatherStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
            vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt,
            vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt,
            StatusStep(
                None,
                None,
                Some(FatherStepExist()),
                Some(Success())
            ),
            vFatherStep.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
              hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
            })
        )
      }
      case (None, FatherStepNotExist()) => 
        createErrorStepBO(StatusStep(None, None, Some(FatherStepNotExist()), Some(Error())))
      case (None, ODBClassCastError()) => 
        createErrorStepBO(StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createErrorStepBO(StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBReadError())))
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String
   * 
   * @return List[ComponentBO]
   */
  private def getComponents(stepId: String): List[ComponentBO] = {
    
    val graph: OrientGraph = Database.getFactory().getTx
    
    try{
      val vStep: OrientVertex = graph.getVertex(stepId)
      val eHasComponents: List[Edge] = vStep.getEdges(Direction.OUT).asScala.toList
      val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
      vComponents.map(vC => {
        ComponentBO(
            vC.getIdentity.toString,
            vC.getProperty(PropertyKeys.NAME_TO_SHOW),
            StatusComponent(None, None , None, Some(Success()), None),
            List(), List(), List(), List()
        )
      })
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        List(ComponentBO(
            "", "",
            StatusComponent(None, None , None, Some(ODBClassCastError()), None),
            List(), List(), List(), List()
        ))
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        List(ComponentBO(
            "", "",
            StatusComponent(None, None , None, Some(ODBReadError()), None),
            List(), List(), List(), List()
        ))
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param ComponentIn
   * 
   * @return ComponentOut
   */
  private def getComponent(componentId: String): ComponentBO = {
    
    val graph: OrientGraph = Database.getFactory().getTx
    
    
    
    try{
      val vComponent = graph.getVertex(componentId)
      
      ComponentBO(
          vComponent.getIdentity.toString,
          vComponent.getProperty(PropertyKeys.NAME_TO_SHOW),
          StatusComponent(None, None, None, Some(Success()), None),
          getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
          getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
          getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE},
          getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE}
      )
    
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        ComponentBO(
            "", "", StatusComponent(None, None, None, Some(ODBClassCastError()), None),
            List(), List(), List(), List()
        )
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        ComponentBO(
            "", "", StatusComponent(None, None, None, Some(ODBReadError()), None),
            List(), List(), List(), List()
        )
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String
   * 
   * @return StepBO
   */
  private def getNextStep(componentId: String): StepBO = {
    val graph: OrientGraph = Database.getFactory().getTx
    
    val vNextStep: (Option[OrientVertex], Status) = try{
    
      graph.getVertex(componentId).getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
        case eHasSteps if eHasSteps.size == 1 => 
          (Some(eHasSteps.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]), NextStepExist())
        case eHasSteps if eHasSteps.size > 1 => (None, MultipleNextSteps())
        case eHasSteps if eHasSteps.size == 0 => (None, NextStepNotExist())
      }
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
      }
    }
    
    (vNextStep: @unchecked) match {
      case (Some(vNextStep), NextStepExist()) => {
        StepBO(
            vNextStep.getIdentity.toString,
            vNextStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt,
            StatusStep(
                None,
                Some(NextStepExist()),
                None,
                Some(Success())
            ),
            vNextStep.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
              hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
            })
        )
      }
      case (None, MultipleNextSteps()) => 
        createErrorStepBO(StatusStep(None, Some(MultipleNextSteps()), None, Some(Error())))
      case (None, NextStepNotExist()) => 
        createErrorStepBO(StatusStep(None, Some(NextStepNotExist()), None, Some(Success())))
      case (None, ODBClassCastError()) => 
        createErrorStepBO(StatusStep(None, Some(CommonErrorNextStep()), None, Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createErrorStepBO(StatusStep(None, Some(CommonErrorNextStep()), None, Some(ODBReadError())))
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String, String, Int, Int, StatusStep
   * 
   * @return StepBO
   */
  
  private def getFirstStep(configUrl: String): StepBO = {
    
    val graph: OrientGraph = Database.getFactory().getTx
    
    val vFirstStep: (Option[OrientVertex], Status) = try{
      val vConfigs: List[Vertex] = graph.getVertices(PropertyKeys.CONFIG_URL, configUrl).asScala.toList 
      vConfigs.size match {
        case vConfigsCount if vConfigsCount == 1 => {
          val eHasConfigs: List[Edge] = vConfigs.head.getEdges(Direction.OUT, PropertyKeys.HAS_FIRST_STEP).asScala.toList 
          eHasConfigs.size match {
            case eHasConfigsCount if eHasConfigsCount == 1 => {
              val vFirstStep: OrientVertex = eHasConfigs.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]
              (Some(vFirstStep), FirstStepExist())
            }
            case eHasConfigsCount if eHasConfigsCount > 1 => (None, MultipleFirstSteps())
            case eHasConfigsCount if eHasConfigsCount < 1 => (None, FirstStepNotExist())
          }
        }
        case vConfigsCount if vConfigsCount > 1 => (None, MultipleFirstSteps())
        case vConfigsCount if vConfigsCount < 1 => (None, FirstStepNotExist())
      }
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        (None, ODBClassCastError())
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
      }
    }
    
    (vFirstStep: @unchecked) match {
      case (Some(vFirstStep), FirstStepExist()) => {
        StepBO(
            vFirstStep.getIdentity.toString(), 
            vFirstStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString(), 
            vFirstStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN), 
            vFirstStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX), 
            StatusStep(
                Some(FirstStepExist()),
                None,
                None,
                Some(Success())
            ),
            List())
      }
      case (None, MultipleFirstSteps()) => 
        createErrorStepBO(StatusStep(Some(MultipleFirstSteps()), None, None, Some(Error())))
      case (None, FirstStepNotExist()) => 
        createErrorStepBO(StatusStep(Some(FirstStepNotExist()), None, None, Some(Error())))
      case (None, ODBClassCastError()) => 
        createErrorStepBO(StatusStep(Some(CommonErrorFirstStep()), None, None, Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createErrorStepBO(StatusStep(Some(CommonErrorFirstStep()), None, None, Some(ODBReadError())))
    }
  }
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String, String, Int, Int, StatusStep
   * 
   * @return StepBO
   */
  private def createErrorStepBO(s: StatusStep): StepBO = {
    StepBO(status = s, componentIds = List())
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param OrientVertex
   * 
   * @return List[DependencyBO]
   */
  private def getComponentDependenciesIn(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_DEPENDENCY)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    eHasDependencies map {
      eHasDependency => {
        DependencyBO(
            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
        )
      }
    }
  }
  
    /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param OrientVertex
   * 
   * @return List[DependencyBO]
   */
  private def getComponentDependenciesOut(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_DEPENDENCY)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    eHasDependencies map {
      eHasDependency => {
        DependencyBO(
            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
        )
      }
    }
  }
}