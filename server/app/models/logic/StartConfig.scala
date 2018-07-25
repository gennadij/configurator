package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.Success

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.02.2018
  */

object StartConfig {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param startConfigIn : StartConfigIn
    * @return StartConfigOut
    */
  def startConfig(startConfigIn: StartConfigBO): StartConfigBO = {
    new StartConfig(startConfigIn.configUrl).getFirstStep
  }
}

class StartConfig(configUrl: Option[String]) {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return StartConfigOut
    */
  private def getFirstStep: StartConfigBO = {

    val firstStep: StepBO = Persistence.getFirstStep(configUrl.get)

    firstStep.stepId match {
      case Some(stepId) =>
        val componentsForSelectionBO: ComponentsForSelectionBO = Persistence.getComponents(stepId)

        val firstStepIdHash = RidToHash.setIdAndHash(stepId)._2

        val firstStepWithHashId = firstStep.copy(stepId = Some(firstStepIdHash))

        val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
          firstStepIdHash,
          firstStep.nameToShow.get
        )

        // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
        CurrentConfig.addFirstStep(firstStepCurrentConfig)

        val componentsBOWithHashId: ComponentsForSelectionBO = componentsForSelectionBO.status.get.common match {
          case Some(Success()) =>
            ComponentsForSelectionBO(
              status = componentsForSelectionBO.status,
              componentsForSelectionBO.components map (c => {
                c.copy(componentId = Some(RidToHash.setIdAndHash(c.componentId.get)._2))
              }))
          case None => componentsForSelectionBO
          case status => componentsForSelectionBO
        }


        StartConfigBO(
          step = Some(firstStepWithHashId),
          componentsForSelection = Some(componentsBOWithHashId)
        )

      case None =>
        StartConfigBO(
          step = Some(firstStep),
          componentsForSelection = None
        )
    }



  }
}