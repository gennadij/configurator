package models.configLogic

import models.bo.currentConfig.{CurrentConfigContainerBO, CurrentConfigStepBO}
import models.bo.step
import models.bo.step.{ComponentForSelectionBO, StepContainerBO}
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
  def startConfig(stepContainerBO: StepContainerBO,
//                  currentConfig: CurrentConfig
                 currentConfigContainerBO: CurrentConfigContainerBO
                 ): StepContainerBO = {
    new StartConfig(stepContainerBO.configUrl,
//      currentConfig
      currentConfigContainerBO
    ).startConfig
  }
}

class StartConfig(
                   configUrl: Option[String],
                   currentConfigContainerBO: CurrentConfigContainerBO
                 ) extends  CurrentConfig {


  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return StartConfigOut
    */
  private def startConfig: StepContainerBO = {

    val firstStepBO: StepContainerBO = Persistence.getStep(configUrl = configUrl)

    firstStepBO.step match {
      case Some(stepBO) =>
        val (componentsForSelectionBO, errorComponents): (Option[List[ComponentForSelectionBO]], Option[Error]) =
          Persistence.getComponents(stepBO.stepId.get)

        val firstStepCurrentConfig: CurrentConfigStepBO = CurrentConfigStepBO(
          stepBO.stepId.get,
          stepBO.nameToShow.get
        )

        // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
        addFirstStep(currentConfigContainerBO, firstStepCurrentConfig)

        errorComponents match {
          case Some(error) =>
            step.StepContainerBO(
              error = Some(List(error))
            )
          case _ =>
            firstStepBO.copy(componentsForSelection = componentsForSelectionBO)
        }
      case None =>
        firstStepBO
    }
  }
}