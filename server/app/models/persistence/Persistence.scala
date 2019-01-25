package models.persistence

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.{ComponentBO, SelectedComponentContainerBO, StepBO, StepContainerBO}
import org.shared.error.Error

import scala.collection.JavaConverters._
/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann on 22.09.2016
  */

object Persistence {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param configUrl : String
    * @return StepBO
    */

  def getStep(configUrl: Option[String] = None, componentId: Option[String] = None): StepContainerBO = {

    val (vStep, error) : (Option[OrientVertex], Option[Error]) = ((configUrl, componentId): @unchecked) match {
      case (Some(configUrl), None) => Graph.getStep(configUrl = Some(configUrl))
      case (None, Some(componentId)) => Graph.getStep(componentId = Some(componentId))
    }

    error match {
      case Some(_) => StepContainerBO(error = Some(List(error.get)))
      case None => StepContainerBO(
        step = Some(
          StepBO(
            stepId = Some(vStep.get.getIdentity.toString),
            nameToShow = Some(vStep.get.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
            selectionCriterionMin = Some(vStep.get.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN)),
            selectionCriterionMax = Some(vStep.get.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX))
          )
        )
      )
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return ComponentsBO
    */
  def getComponents(stepId: String): (Option[Set[ComponentBO]], Option[Error])  = {
    val (vComponents, error): (Option[Set[OrientVertex]], Option[Error]) = Graph.getComponents(stepId)

    vComponents match {
      case Some(vCs) =>
        val componentBOs = vCs map (vC => {
          ComponentBO(
              componentId = Some(vC.getIdentity.toString),
              nameToShow = Some(vC.getProperty(PropertyKeys.NAME_TO_SHOW))
          )
        })
        (Some(componentBOs.toSet), None)
      case None => (None, error)
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param selectedComponentId: String
    * @return ContainerComponentBO
    */
  def getSelectedComponent(selectedComponentId: String): SelectedComponentContainerBO = {

    val (vComponent, error): (Option[OrientVertex], Option[Error]) = Graph.getComponent(selectedComponentId)

    error match {
      case Some(error) => SelectedComponentContainerBO(errors = Some(List(error)))
      case None =>
        val excludeDependencyOut =
          Graph.getComponentDependenciesOut(vComponent.get) filter {_.dependencyType == PropertyKeys.EXCLUDE}
        val excludeDependencyIn =
          Graph.getComponentDependenciesIn(vComponent.get) filter {_.dependencyType == PropertyKeys.EXCLUDE}
        val requireDependencyOut =
          Graph.getComponentDependenciesOut(vComponent.get) filter {_.dependencyType == PropertyKeys.REQUIRE}
        val requireDependencyIn =
          Graph.getComponentDependenciesIn(vComponent.get) filter {_.dependencyType == PropertyKeys.REQUIRE}

        SelectedComponentContainerBO(
          selectedComponent = Some(ComponentBO(
            componentId = Some(vComponent.get.getIdentity.toString),
            nameToShow = Some(vComponent.get.getProperty(PropertyKeys.NAME_TO_SHOW)),
            excludeDependenciesOut = Some(excludeDependencyOut),
            excludeDependenciesIn = Some(excludeDependencyIn),
            requireDependenciesOut = Some(requireDependencyOut),
            requireDependenciesIn = Some(requireDependencyIn)
          )))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId: String
    * @return StepBO
    */
  def getCurrentStep(componentId: String): StepContainerBO = {
    val (vCurrentStep: Option[OrientVertex], error: Option[Error]) =
      Graph.getCurrentStep(componentId)

    error match {
      case Some(_) => StepContainerBO(error = Some(List(error.get)))
      case None => StepContainerBO(
        step = Some(StepBO(
          stepId = Some(vCurrentStep.get.getIdentity.toString),
          nameToShow = Some(vCurrentStep.get.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          selectionCriterionMin = Some(vCurrentStep.get.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          Some(vCurrentStep.get.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt)
        )),
        componentsForSelection =
          Some(
            (
              vCurrentStep.get.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
                ComponentBO(
                  componentId = Some(hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
                  )
                )
              })
              ).toSet
          )
      )
    }
  }
}