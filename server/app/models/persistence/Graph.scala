package models.persistence

import com.tinkerpop.blueprints.impls.orient.{OrientEdge, OrientGraph, OrientVertex}
import com.tinkerpop.blueprints.{Direction, Edge, Vertex}
import models.bo.DependencyBO
import models.bo.types.{Auto, SelectableDecision}
import org.shared.status.common._
import org.shared.status.nextStep._
import org.shared.status.selectedComponent._
import org.shared.status.startConfig._
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
  def getCurrentStep(componentId: String): (Option[OrientVertex], StatusCurrentStep, Status) = {
    val graph: (Option[OrientGraph], String) = ODatabase.getFactory
    graph._1 match {
      case Some(g) => new Graph(Some(g)).getCurrentStep(componentId)
      case None => (None, CommonErrorCurrentStep(), ODBConnectionError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return ComponentBO
    */
  def getComponent(componentId: String): (Option[OrientVertex], Status) = {
    val graph: (Option[OrientGraph], String) = ODatabase.getFactory
    graph._1 match {
      case Some(g) => new Graph(Some(g)).getComponent(componentId)
      case None => (None, ODBConnectionError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  def getNextStep(componentId: String): (Option[OrientVertex], StatusNextStep, Status) = {
    val graph: (Option[OrientGraph], String) = ODatabase.getFactory
    graph._1 match {
      case Some(g) => new Graph(Some(g)).getNextStep(componentId)
      case None => (None, NextStepCommonError(), ODBConnectionError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return OrientVertex
    */
  def getFirstStep(configUrl: String): (Option[OrientVertex], StatusStartConfig, Status) = {
    val graph: (Option[OrientGraph], String) = ODatabase.getFactory
    graph._1 match {
      case Some(g) => new Graph(Some(g)).getFirstStep(configUrl)
      case None => (None, CommonErrorStartConfig(), ODBConnectionError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return List[ComponentBO]
    */
  def getComponents(stepId: String): (Option[List[OrientVertex]], Status) = {
    val graph: (Option[OrientGraph], String) = ODatabase.getFactory
    graph._1 match {
      case Some(g) => new Graph(Some(g)).getComponents(stepId)
      case None => (None, ODBConnectionError())
    }
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
//TODO In der OrientDB Client war der Test bei der RegExp erfolgreich


class Graph(graph: Option[OrientGraph]) {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return OrientVertex
    */
  private def getCurrentStep(componentId: String): (Option[OrientVertex], StatusCurrentStep, Status) = {

    try {
      val vComponent = graph.get.getVertex(componentId)

      val eHasComponents: List[OrientEdge] =
        vComponent.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT).asScala.toList map {_.asInstanceOf[OrientEdge]}

      val vFatherSteps : List[OrientVertex]= eHasComponents map (eHasComponent => {eHasComponent.getVertex(Direction.OUT)})
      vFatherSteps.size match {
        case s if s == 0 => (None, CurrentStepNotExist(), Success())
        case s if s == 1 => (Some(vFatherSteps.head), CurrentStepExist(), Success())
        case s if s >  1 => (None, MultipleCurrentSteps(), Success())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, CommonErrorCurrentStep(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, CommonErrorCurrentStep(), ODBReadError())
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
        case eHasSteps if eHasSteps.size > 1 => (None, NextMultipleSteps(), Success())
        case eHasSteps if eHasSteps.isEmpty => (None, NextStepNotExist(), Success())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, NextStepCommonError(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, NextStepCommonError(), ODBReadError())
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return StepBO
    */

  private def getFirstStep(configUrl: String): (Option[OrientVertex], StatusStartConfig, Status) = {

    try {
      val vConfigs: List[Vertex] = graph.get.getVertices(PropertyKeys.CONFIG_URL, configUrl).asScala.toList
      vConfigs.size match {
        case vConfigsCount if vConfigsCount == 1 =>
          val eHasConfigs: List[Edge] = vConfigs.head.getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList
          eHasConfigs.size match {
            case eHasConfigsCount if eHasConfigsCount == 1 =>
              val vFirstStep: OrientVertex = eHasConfigs.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]
              (Some(vFirstStep), StartConfigExist(), Success())
            case eHasConfigsCount if eHasConfigsCount > 1 => (None, MultipleFirstSteps(), Error())
            case eHasConfigsCount if eHasConfigsCount < 1 => (None, StartConfigNotExist(), Error())
          }
        case vConfigsCount if vConfigsCount > 1 => (None, MultipleFirstSteps(), Error())
        case vConfigsCount if vConfigsCount < 1 => (None, StartConfigNotExist(), Error())
      }
    } catch {
      case e2: ClassCastException =>
        graph.get.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, CommonErrorStartConfig(), ODBClassCastError())
      case e1: Exception =>
        graph.get.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, CommonErrorStartConfig(), ODBReadError())
    }


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
          outId = eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString,
          inId = eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString,
          visualization = eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString,
          dependencyType = eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString,
          nameToShow = eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
          strategyOfDependencyResolver = eHasDependency.getProperty(PropertyKeys.STRATEGY_OF_DEPENDENCY_RESOLVER).toString match {
            case Auto.value => Auto
            case SelectableDecision.value => SelectableDecision
          }
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
          outId = eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString,
          inId = eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString,
          visualization = eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString,
          dependencyType = eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString,
          nameToShow = eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
          strategyOfDependencyResolver = eHasDependency.getProperty(PropertyKeys.STRATEGY_OF_DEPENDENCY_RESOLVER).toString match {
            case Auto.value => Auto
            case SelectableDecision.value => SelectableDecision
          }
        )
      }
    }
  }
}