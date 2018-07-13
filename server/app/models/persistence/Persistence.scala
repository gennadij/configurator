package models.persistence

import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.bo.{ComponentBO, ContainerComponentBO, StepBO}
import models.persistence.orientdb.{Graph, PropertyKeys}
import org.shared.common.status.step.{FirstStepExist, StatusFirstStep, StatusStep}
import org.shared.common.status.{Status, Success}
import org.shared.component.status.StatusComponent

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
          status = StatusStep(
            firstStep = Some(FirstStepExist()),
            common = Some(Success())
          )
        )
      case None =>
        StepBO(
          status = StatusStep(
            firstStep = Some(statusFirstStep),
            common = Some(statusCommon)
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
  def getComponents(stepId: String): ContainerComponentBO = {
    val (vComponents, statusCommon): (Option[List[OrientVertex]], Status) = Graph.getComponents(stepId)

    vComponents match {
      case Some(vCs) =>
        val componentBOs = vCs map (vC => {
          ComponentBO(
              componentId = Some(vC.getIdentity.toString),
              nameToShow = Some(vC.getProperty(PropertyKeys.NAME_TO_SHOW))
          )
        })

        ContainerComponentBO(
          status = Some(StatusComponent(common = Some(Success()))),
          components = componentBOs
        )

      case None => ContainerComponentBO(status = Some(StatusComponent(common = Some(statusCommon))))
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
    * @param
    * @return
    */
  def getSelectedComponent(selectedComponentId: String): ContainerComponentBO = {
    Graph.getComponent(selectedComponentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param
    * @return
    */
  def getFatherStep(componentId: String): StepBO = {
    Graph.getFatherStep(componentId)
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param
    * @return
    */
  def getNextStep(componentId: String): StepBO = {
    Graph.getNextStep(componentId)
  }
}