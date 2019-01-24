package models.persistence

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.{ComponentBO, SelectedComponentBO, StepBO, StepContainerBO}
import org.shared.error.Error
import org.shared.status.common.{Status, Success}
import org.shared.status.selectedComponent.StatusComponent

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
  def getSelectedComponent(selectedComponentId: String): SelectedComponentBO = {

    val (vComponent, statusCommon): (Option[OrientVertex], Status) = Graph.getComponent(selectedComponentId)

    vComponent match {
      case Some(vC) =>
        val excludeDependencyOut =
          Graph.getComponentDependenciesOut(vC) filter {_.dependencyType == PropertyKeys.EXCLUDE}
        val excludeDependencyIn =
          Graph.getComponentDependenciesIn(vC) filter {_.dependencyType == PropertyKeys.EXCLUDE}
        val requireDependencyOut =
          Graph.getComponentDependenciesOut(vC) filter {_.dependencyType == PropertyKeys.REQUIRE}
        val requireDependencyIn =
          Graph.getComponentDependenciesIn(vC) filter {_.dependencyType == PropertyKeys.REQUIRE}

        SelectedComponentBO(
          status = Some(StatusComponent(common = Some(Success()))),
          selectedComponent = Some(ComponentBO(
            Some(vC.getIdentity.toString),
            Some(vC.getProperty(PropertyKeys.NAME_TO_SHOW)),
            Some(excludeDependencyOut),
            Some(excludeDependencyIn),
            Some(requireDependencyOut),
            Some(requireDependencyIn)
          ))
      )

      case None => SelectedComponentBO(
        Some(StatusComponent(common = Some(statusCommon))))
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