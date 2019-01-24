package models.configLogic

import models.bo._
import models.persistence.Persistence
import org.shared.error.Error

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

    val firstStepBO: StepContainerBO = Persistence.getStep(configUrl = configUrl)

    firstStepBO.step match {
      case Some(stepBO) =>
        val (componentBOs, errorComponents): (Option[Set[ComponentBO]], Option[Error]) =
          Persistence.getComponents(stepBO.stepId.get)

        val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
          stepBO.stepId.get,
          stepBO.nameToShow.get
        )

        // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
        currentConfig.addFirstStep(firstStepCurrentConfig)

        errorComponents match {
          case Some(error) =>
            StepContainerBO(
              error = Some(List(error))
            )
          case _ =>
            firstStepBO.copy(componentsForSelection = componentBOs)
        }
      case None =>
        firstStepBO
    }
  }
}