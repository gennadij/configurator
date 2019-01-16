package models.configLogic

import models.bo._
import models.persistence.Persistence
import org.shared.status.common.Success

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.02.2018
  */

object StartConfig {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @param stepContainerBO : StartConfigIn
    * @return StartConfigOut
    */
  def startConfig(stepContainerBO: StepContainerBO, currentConfig: CurrentConfig): StepContainerBO = {
    new StartConfig(stepContainerBO.configUrl, currentConfig).startConfig
  }
}

class StartConfig(configUrl: Option[String], currentConfig: CurrentConfig) {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return StartConfigOut
    */
  private def startConfig: StepContainerBO = {

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