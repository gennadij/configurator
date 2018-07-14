package models.persistence.orientdb

import com.tinkerpop.blueprints.impls.orient.{OrientEdge, OrientGraph, OrientVertex}
import com.tinkerpop.blueprints.{Direction, Edge, Vertex}
import models.bo.{ComponentBO, ContainerComponentBO, DependencyBO, StepBO}
import models.persistence.Database
import org.shared.common.status._
import org.shared.common.status.step._
import org.shared.component.status.StatusComponent
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
  def getFatherStep(componentId: String): StepBO = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(graph).getFatherStep(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return ComponentBO
    */
  def getComponent(componentId: String): ContainerComponentBO = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(graph).getComponent(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  def getNextStep(componentId: String): StepBO = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(graph).getNextStep(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return OrientVertex
    */
  def getFirstStep(configUrl: String): (Option[OrientVertex], StatusFirstStep, Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(graph).getFirstStep(configUrl)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return List[ComponentBO]
    */
  def getComponents(stepId: String): (Option[List[OrientVertex]], Status) = {
    val graph: OrientGraph = Database.getFactory().getTx
    new Graph(graph).getComponents(stepId)
  }

}

class Graph(graph: OrientGraph) {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return OrientVertex
    */
  private def getFatherStep(componentId: String): StepBO = {

    val graph: OrientGraph = Database.getFactory().getTx

    val vFatherStep: (Option[OrientVertex], Status) = try {
      val vComponent = graph.getVertex(componentId)

      val eHasComponents: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT)
        .asScala.toList map {
        _.asInstanceOf[OrientEdge]
      }

      val vFatherSteps: List[OrientVertex] = eHasComponents map {
        eHasComponent => {
          eHasComponent.getVertex(Direction.OUT)
        }
      }
      vFatherSteps match {
        case List() => (None, FatherStepNotExist())
        case _ => (Some(vFatherSteps.head), FatherStepExist())
      }

    } catch {
      case e2: ClassCastException =>
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      case e1: Exception =>
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
    }

    (vFatherStep: @unchecked) match {
      case (Some(vFatherStep), FatherStepExist()) =>
        StepBO(
          Some(vFatherStep.getIdentity.toString),
          Some(vFatherStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          Some(vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          Some(vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          StatusStep(
            None,
            None,
            Some(FatherStepExist()),
            Some(Success())
          ),
          Some(vFatherStep.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
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
    * @version 0.0.2
    * @param stepId : String
    * @return List[ComponentBO]
    */
  private def getComponents(stepId: String): (Option[List[OrientVertex]], Status) = {

    val graph: OrientGraph = Database.getFactory().getTx

    try {
      val vStep: OrientVertex = graph.getVertex(stepId)
      val eHasComponents: List[Edge] = vStep.getEdges(Direction.OUT).asScala.toList
      val vComponents: List[OrientVertex] = eHasComponents.map(_.getVertex(Direction.IN).asInstanceOf[OrientVertex])
      (Some(vComponents), Success())
    } catch {
      case e2: ClassCastException =>
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      case e1: Exception =>
        graph.rollback()
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
  private def getComponent(componentId: String): ContainerComponentBO = {

    val graph: OrientGraph = Database.getFactory().getTx


    try {
      val vComponent = graph.getVertex(componentId)

      ContainerComponentBO(
        status = Some(StatusComponent(common = Some(Success()))),
        components = List(ComponentBO(
          Some(vComponent.getIdentity.toString),
          Some(vComponent.getProperty(PropertyKeys.NAME_TO_SHOW)),
          Some(getComponentDependenciesOut(vComponent) filter {
            _.dependencyType == PropertyKeys.EXCLUDE
          }),
          Some(getComponentDependenciesIn(vComponent) filter {
            _.dependencyType == PropertyKeys.EXCLUDE
          }),
          Some(getComponentDependenciesOut(vComponent) filter {
            _.dependencyType == PropertyKeys.REQUIRE
          }),
          Some(getComponentDependenciesIn(vComponent) filter {
            _.dependencyType == PropertyKeys.REQUIRE
          })
        ))

      )

    } catch {
      case e2: ClassCastException =>
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        ContainerComponentBO(
          status = Some(StatusComponent(common = Some(ODBClassCastError())))
        )
      case e1: Exception =>
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        ContainerComponentBO(
          status = Some(StatusComponent(common = Some(ODBReadError())))
        )
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId : String
    * @return StepBO
    */
  private def getNextStep(componentId: String): StepBO = {
    val graph: OrientGraph = Database.getFactory().getTx

    val vNextStep: (Option[OrientVertex], Status) = try {

      graph.getVertex(componentId).getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
        case eHasSteps if eHasSteps.size == 1 =>
          (Some(eHasSteps.head.getVertex(Direction.IN).asInstanceOf[OrientVertex]), NextStepExist())
        case eHasSteps if eHasSteps.size > 1 => (None, MultipleNextSteps())
        case eHasSteps if eHasSteps.isEmpty => (None, NextStepNotExist())
      }
    } catch {
      case e2: ClassCastException =>
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
        (None, ODBClassCastError())
      case e1: Exception =>
        graph.rollback()
        Logger.error(e1.printStackTrace().toString)
        (None, ODBReadError())
    }

    (vNextStep: @unchecked) match {
      case (Some(vNextStep), NextStepExist()) =>
        StepBO(
          Some(vNextStep.getIdentity.toString),
          Some(vNextStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          Some(vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          Some(vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          StatusStep(
            None,
            Some(NextStepExist()),
            None,
            Some(Success())
          ),
          Some(vNextStep.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
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
    * @version 0.0.2
    * @param configUrl : String
    * @return StepBO
    */

  private def getFirstStep(configUrl: String): (Option[OrientVertex], StatusFirstStep, Status) = {

    try {
      val vConfigs: List[Vertex] = graph.getVertices(PropertyKeys.CONFIG_URL, configUrl).asScala.toList
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
        graph.rollback()
        (None, CommonErrorFirstStep(), ODBClassCastError())
      case e1: Exception =>
        graph.rollback()
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