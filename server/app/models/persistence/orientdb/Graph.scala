package models.persistence.orientdb

import com.tinkerpop.blueprints.impls.orient.{OrientEdge, OrientGraph, OrientVertex}
import com.tinkerpop.blueprints.{Direction, Edge, Vertex}
import models.bo.{DependencyBO, StepBO}
import models.persistence.Database
import org.shared.common.status._
import org.shared.common.status.step._
import play.api.Logger

import scala.collection.JavaConverters._

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 13.02.2018
  */

object Graph {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  def getFatherStep(componentId: String): (Option[OrientVertex], StatusFatherStep, Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(Some(graph)).getFatherStep(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return ComponentBO
    */
  def getComponent(componentId: String): (Option[OrientVertex], Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(Some(graph)).getComponent(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  def getNextStep(componentId: String): (Option[OrientVertex], StatusNextStep, Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(Some(graph)).getNextStep(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return OrientVertex
    */
  def getFirstStep(configUrl: String): (Option[OrientVertex], StatusFirstStep, Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(Some(graph)).getFirstStep(configUrl)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return List[ComponentBO]
    */
  def getComponents(stepId: String): (Option[List[OrientVertex]], Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(Some(graph)).getComponents(stepId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param vComponent : OrientVertex
    * @return List[DependencyBO]
    */
  def getComponentDependenciesIn(vComponent: OrientVertex): List[DependencyBO] = {
    new Graph(None).getComponentDependenciesIn(vComponent)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param vComponent : OrientVertex
    * @return List[DependencyBO]
    */
  def getComponentDependenciesOut(vComponent: OrientVertex): List[DependencyBO] = {
    new Graph(None).getComponentDependenciesOut(vComponent)
  }

}

class Graph(graph: Option[OrientGraph]) {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return OrientVertex
    */
  private def getFatherStep(componentId: String): (Option[OrientVertex], StatusFatherStep, Status) = {

    try {
      val vComponent = graph.get.getVertex(componentId)

      val eHasComponents: List[OrientEdge] =
        vComponent.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT).asScala.toList map {_.asInstanceOf[OrientEdge]}

      eHasComponents map (eHasComponent => {eHasComponent.getVertex(Direction.OUT)}) match {
        case List() => (None, FatherStepNotExist(), Error())
        case vFS => (Some(vFS.head), FatherStepExist(), Success())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, CommonErrorFatherStep(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, CommonErrorFatherStep(), ODBReadError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return List[ComponentBO]
    */
  private def getComponents(stepId: String): (Option[List[OrientVertex]], Status) = {

    try {
      val vStep: OrientVertex = graph.get.getVertex(stepId)
      val eHasComponents: List[Edge] = vStep.getEdges(Direction.OUT).asScala.toList
      val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
      (Some(vComponents), Success())
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return ComponentOut
    */
  private def getComponent(componentId: String): (Option[OrientVertex], Status) = {

    try {
      val vComponent = graph.get.getVertex(componentId)
      (Some(vComponent), Success())

    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  private def getNextStep(componentId: String): (Option[OrientVertex], StatusNextStep, Status) = {

    try {
      graph.get.getVertex(componentId).getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
        case eHasSteps if eHasSteps.size == 1 =>
          (Some(eHasSteps.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]), NextStepExist(), Success())
        case eHasSteps if eHasSteps.size > 1 => (None, MultipleNextSteps(), Error())
        case eHasSteps if eHasSteps.isEmpty => (None, NextStepNotExist(), Success())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, CommonErrorNextStep(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, CommonErrorNextStep(), ODBReadError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return StepBO
    */

  private def getFirstStep(configUrl: String): (Option[OrientVertex], StatusFirstStep, Status) = {

    try {
      val vConfigs: List[Vertex] = graph.get.getVertices(PropertyKeys.CONFIG_URL, configUrl).asScala.toList
      vConfigs.size match {
        case vConfigsCount if vConfigsCount == 1 =>
          val eHasConfigs: List[Edge] = vConfigs.head.getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList
          eHasConfigs.size match {
            case eHasConfigsCount if eHasConfigsCount == 1 =>
              val vFirstStep: OrientVertex = eHasConfigs.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]
              (Some(vFirstStep), FirstStepExist(), Success())
            case eHasConfigsCount if eHasConfigsCount > 1 => (None, MultipleFirstSteps(), Error())
            case eHasConfigsCount if eHasConfigsCount < 1 => (None, FirstStepNotExist(), Error())
          }
        case vConfigsCount if vConfigsCount > 1 => (None, MultipleFirstSteps(), Error())
        case vConfigsCount if vConfigsCount < 1 => (None, FirstStepNotExist(), Error())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        (None, CommonErrorFirstStep(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, CommonErrorFirstStep(), ODBReadError())
    }


  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param s : StatusStep
    * @return StepBO
    */
  private def createErrorStepBO(s: StatusStep): StepBO = {
    StepBO(status = s, componentIds = None)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param vComponent : OrientVertex
    * @return List[DependencyBO]
    */
  private def getComponentDependenciesIn(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_DEPENDENCY)
      .asScala.toList map {
      _.asInstanceOf[OrientEdge]
    }
    eHasDependencies map {
      eHasDependency => {
        DependencyBO(
          eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
          eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
          eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
          eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
          eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString //nameToShow: String
        )
      }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param vComponent : OrientVertex
    * @return List[DependencyBO]
    */
  private def getComponentDependenciesOut(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_DEPENDENCY)
      .asScala.toList map {
      _.asInstanceOf[OrientEdge]
    }
    eHasDependencies map {
      eHasDependency => {
        DependencyBO(
          eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
          eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
          eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
          eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
          eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString //nameToShow: String
        )
      }
    }
  }
}