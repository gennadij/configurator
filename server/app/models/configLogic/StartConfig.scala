package models.configLogic

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
  def startConfig(startConfigIn: StartConfigBO, currentConfig: CurrentConfig): StartConfigBO = {
    new StartConfig(startConfigIn.configUrl, currentConfig).getFirstStep
  }
}

class StartConfig(configUrl: Option[String], currentConfig: CurrentConfig) {


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

        val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
          stepId,
          firstStep.nameToShow.get
        )

        // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
        currentConfig.addFirstStep(firstStepCurrentConfig)

        val componentsBOWithHashId: ComponentsForSelectionBO = componentsForSelectionBO.status.get.common match {
          case Some(Success()) =>
            ComponentsForSelectionBO(
              status = componentsForSelectionBO.status,
              components =  componentsForSelectionBO.components
            )
          case None => componentsForSelectionBO
          case _ => componentsForSelectionBO
        }

        StartConfigBO(
          step = Some(firstStep),
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