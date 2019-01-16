package models.persistence

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.{ComponentBO, SelectedComponentBO, StepBO}
import org.shared.error.Error
import org.shared.status.common
import org.shared.status.common.{Status, StatusStep, Success}
import org.shared.status.currentConfig.StepCurrentConfigSuccess
import org.shared.status.nextStep.{NextStepExist, StatusNextStep}
import org.shared.status.selectedComponent.{StatusComponent, StatusCurrentStep}

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

  def getFirstStep(configUrl: String): (Option[StepBO], Option[Error]) = {
    val (vFirstStep, error): (Option[OrientVertex], Option[Error]) =
      Graph.getFirstStep(configUrl)

    vFirstStep match {
      case Some(fS) =>
        (Some(StepBO(
          stepId = Some(fS.getIdentity.toString),
          nameToShow = Some(fS.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          selectionCriterionMin = Some(fS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN)),
          selectionCriterionMax = Some(fS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX)))
        ), None)
      case None =>
        (None, error)
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

//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param StartConfigIn
//    * @return StartConfigOut
//    */
  //  def startConfig(startConfigIn: StartConfigIn) : StartConfigOut = {
  //    StepVertex.firstStep(startConfigIn)
  //  }

//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param NextStepIn
//    * @return NextStepOut
//    */
  //  def nestStep(nextStepIn: NextStepIn): NextStepOut = {
  //    StepVertex.nextStep(nextStepIn)
  //  }

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
  def getCurrentStep(componentId: String): StepBO = {
    val (vCurrentStep: Option[OrientVertex], statusCurrentStep: StatusCurrentStep, statusCommon: Status) =
      Graph.getCurrentStep(componentId)

    vCurrentStep match {
      case Some(vCS) =>
        StepBO(
          Some(vCS.getIdentity.toString),
          Some(vCS.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          Some(vCS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          Some(vCS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          Some(StatusStep(
            currentStep = Some(statusCurrentStep),
            common = Some(statusCommon)
          )),
          Some(vCS.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
      case _ => StepBO(status = Some(StatusStep(currentStep = Some(statusCurrentStep), common = Some(statusCommon))))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param componentId: String
    * @return StepBO
    */
  def getNextStep(componentId: String): StepBO = {
    val (vNextStep, statusNextStep, statusCommon) : (Option[OrientVertex], StatusNextStep, Status) =
      Graph.getNextStep(componentId)

    vNextStep match {
      case Some(vNS) =>
        StepBO(
          stepId = Some(vNS.getIdentity.toString),
          nameToShow = Some(vNS.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          selectionCriterionMin = Some(vNS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          selectionCriterionMax = Some(vNS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          status = Some(StatusStep(
            nextStep = Some(NextStepExist()),
            currentConfig = Some(StepCurrentConfigSuccess()),
            common = Some(Success())
          )),
          componentIds = Some(vNS.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
      case None => StepBO(status = Some(common.StatusStep(nextStep = Some(statusNextStep), common = Some(statusCommon))))
    }
  }
}