package models.persistence

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.{ComponentBO, ComponentsForSelectionBO, SelectedComponentBO, StepBO}
import models.persistence.orientdb.{Graph, PropertyKeys}
import org.shared.common.status.step._
import org.shared.common.status._
import org.shared.component.status.StatusComponent

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

  def getFirstStep(configUrl: String): StepBO = {
    val (vFirstStep, statusFirstStep, statusCommon): (Option[OrientVertex], StatusFirstStep, Status) =
      Graph.getFirstStep(configUrl)

    vFirstStep match {
      case Some(fS) =>
        StepBO(
          stepId = Some(fS.getIdentity.toString),
          nameToShow = Some(fS.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          selectionCriteriumMin = Some(fS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN)),
          selectionCriteriumMax = Some(fS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX)),
          status = Some(StatusStep(
            firstStep = Some(FirstStepExist()),
            common = Some(Success())
          )
        ))
      case None =>
        StepBO(
          status = Some(StatusStep(
            firstStep = Some(statusFirstStep),
            common = Some(statusCommon)
          )
        ))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepId : String
    * @return ComponentsBO
    */
  def getComponents(stepId: String): ComponentsForSelectionBO = {
    val (vComponents, statusCommon): (Option[List[OrientVertex]], Status) = Graph.getComponents(stepId)

    vComponents match {
      case Some(vCs) =>
        val componentBOs = vCs map (vC => {
          ComponentBO(
              componentId = Some(vC.getIdentity.toString),
              nameToShow = Some(vC.getProperty(PropertyKeys.NAME_TO_SHOW))
          )
        })

        ComponentsForSelectionBO(
          status = Some(StatusComponent(common = Some(Success()))),
          components = componentBOs
        )

      case None => ComponentsForSelectionBO(status = Some(StatusComponent(common = Some(statusCommon))))
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param StartConfigIn
    * @return StartConfigOut
    */
  //  def startConfig(startConfigIn: StartConfigIn) : StartConfigOut = {
  //    StepVertex.firstStep(startConfigIn)
  //  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param NextStepIn
    * @return NextStepOut
    */
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
      case Some(vC) => SelectedComponentBO(
        status = Some(StatusComponent(common = Some(Success()))),
        selectedComponent = Some(ComponentBO(
          Some(vC.getIdentity.toString),
          Some(vC.getProperty(PropertyKeys.NAME_TO_SHOW)),
          Some(Graph.getComponentDependenciesOut(vC) filter {
            _.dependencyType == PropertyKeys.EXCLUDE
          }),
          Some(Graph.getComponentDependenciesIn(vC) filter {
            _.dependencyType == PropertyKeys.EXCLUDE
          }),
          Some(Graph.getComponentDependenciesOut(vC) filter {
            _.dependencyType == PropertyKeys.REQUIRE
          }),
          Some(Graph.getComponentDependenciesIn(vC) filter {
            _.dependencyType == PropertyKeys.REQUIRE
          })
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
    val (vCurrentStep: Option[OrientVertex], statusCurrentStep: StatusCurrentStep, statusCommen: Status) =
      Graph.getCurrentStep(componentId)

    //TODO Status FirstSetep und CurrentStep zusammenführen
    vCurrentStep match {
      case Some(vCS) =>
        StepBO(
          Some(vCS.getIdentity.toString),
          Some(vCS.getProperty(PropertyKeys.NAME_TO_SHOW).toString),
          Some(vCS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          Some(vCS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          Some(StatusStep(
            None,
            None,
            Some(CurrentStepExist()),
            Some(Success())
          )),
          Some(vCS.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
      case _ => StepBO(status = Some(StatusStep(currentStep = Some(statusCurrentStep), common = Some(statusCommen))))
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
          selectionCriteriumMin = Some(vNS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt),
          selectionCriteriumMax = Some(vNS.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt),
          status = Some(StatusStep(
            firstStep = None,
            nextStep = Some(NextStepExist()),
            currentStep = None,
            common = Some(Success())
          )),
          componentIds = Some(vNS.getEdges(Direction.OUT, PropertyKeys.HAS_COMPONENT).asScala.toList map (hC => {
            hC.getVertex(Direction.IN).asInstanceOf[OrientVertex].getIdentity.toString()
          }))
        )
      case None => StepBO(status = Some(StatusStep(nextStep = Some(statusNextStep), common = Some(statusCommon))))
    }
  }
}