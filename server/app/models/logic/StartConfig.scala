package models.logic

import models.bo.{ContainerComponentBO, StartConfigBO, StepBO, StepCurrentConfigBO}
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

    val componentsBO: ContainerComponentBO = Persistence.getComponents(firstStep.stepId.get)

    val firstStepIdHash = RidToHash.setIdAndHash(firstStep.stepId.get)._2

    val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
      firstStepIdHash,
      firstStep.nameToShow.get
    )

    // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
    CurrentConfig.addFirstStep(firstStepCurrentConfig)

    //convert ids to hash
    val firstStepWithHashId = firstStep.copy(stepId = Some(firstStepIdHash))

    val componentsBOWithHashId = componentsBO.status.get.common match {
      case Some(Success()) =>
        ContainerComponentBO(
          status = componentsBO.status,
          componentsBO.components map (c => {
            c.copy(componentId = Some(RidToHash.setIdAndHash(c.componentId.get)._2))
          }))
      case None => componentsBO
    }


    StartConfigBO(
      step = Some(firstStepWithHashId),
      components = Some(componentsBOWithHashId)
    )
  }
}