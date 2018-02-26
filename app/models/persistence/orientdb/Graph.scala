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
  def getComponent(componentId: String): Option[ComponentBO] = {
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
  
  def getComponents(stepId: String): List[ComponentBO] = {
    ???
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
    
    vFatherStep match {
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
            )
        )
      }
      case (None, FatherStepNotExist()) => 
        createStepBO(status = StatusStep(None, None, Some(FatherStepNotExist()), Some(Error())))
      case (None, ODBClassCastError()) => 
        createStepBO(status = StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createStepBO(status = StatusStep(None, None, Some(CommonErrorFatherStep()), Some(ODBReadError())))
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
    ???
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
  private def getComponent(componentId: String): Option[ComponentBO] = {
    
    val graph: OrientGraph = Database.getFactory().getTx
    
    val vComponent: Option[OrientVertex] = try{
      
      Some(graph.getVertex(componentId))
      
    }catch{
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        None
      }
      case e1: Exception => {
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        None
      }
    }
    
    vComponent match {
      case Some(vComponent) => {
        Some(ComponentBO(
            vComponent.getIdentity.toString,
            vComponent.getProperty(PropertyKeys.NAME_TO_SHOW),
            getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
            getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
            getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE},
            getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE}
        ))
      }
      case None => None
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
    
    vNextStep match {
      case (Some(vNextStep), NextStepExist()) => {
        createStepBO(
            vNextStep.getIdentity.toString,
            vNextStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt,
            StatusStep(
                None,
                Some(NextStepExist()),
                None,
                Some(Success())
            )
        )
      }
      case (None, MultipleNextSteps()) => 
        createStepBO(status = StatusStep(None, Some(MultipleNextSteps()), None, Some(Error())))
      case (None, NextStepNotExist()) => 
        createStepBO(status = StatusStep(None, Some(NextStepNotExist()), None, Some(Success())))
      case (None, ODBClassCastError()) => 
        createStepBO(status = StatusStep(None, Some(CommonErrorNextStep()), None, Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createStepBO(status = StatusStep(None, Some(CommonErrorNextStep()), None, Some(ODBReadError())))
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
      graph.getVertices(PropertyKeys.CONFIG_URL, configUrl).asScala.toList match {
        case vConfigs if vConfigs.size == 1 => {
          vConfigs.head.getEdges(Direction.OUT, PropertyKeys.HAS_FIRST_STEP).asScala.toList match {
            case eHasConfigs if eHasConfigs == 1 => {
              val vFirstStep: OrientVertex = eHasConfigs.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]
              (Some(vFirstStep), FirstStepExist())
            }
            case eHasConfigs if eHasConfigs.size > 1 => (None, MultipleFirstSteps())
            case eHasConfigs if eHasConfigs.size < 1 => (None, FirstStepNotExist())
          }
        }
        case vConfigs if vConfigs.size > 1 => (None, MultipleFirstSteps())
        case vConfigs if vConfigs.size < 1 => (None, FirstStepNotExist())
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
    
    vFirstStep match {
      case (Some(vFirstStep), FirstStepExist()) => {
        createStepBO(
            vFirstStep.getIdentity.toString(), 
            vFirstStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString(), 
            vFirstStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN), 
            vFirstStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX), 
            StatusStep(
                Some(FirstStepExist()),
                None,
                None,
                Some(Success())
            ))
      }
      case (None, MultipleFirstSteps()) => 
        createStepBO(status = StatusStep(Some(MultipleFirstSteps()), None, None, Some(Error())))
      case (None, FirstStepNotExist()) => 
        createStepBO(status = StatusStep(Some(FirstStepNotExist()), None, None, Some(Error())))
      case (None, ODBClassCastError()) => 
        createStepBO(status = StatusStep(Some(CommonErrorFirstStep()), None, None, Some(ODBClassCastError())))
      case (None, ODBReadError()) => 
        createStepBO(status = StatusStep(Some(CommonErrorFirstStep()), None, None, Some(ODBReadError())))
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
  private def createStepBO(
      id: String = "", nameToShow: String = "", sCMin: Int = -1, scMax: Int = -1, status: StatusStep): StepBO = {
    StepBO(id, nameToShow, sCMin, scMax, status)
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