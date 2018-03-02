package models.logic

import models.wrapper.startConfig.StartConfigOut
import models.wrapper.startConfig.StartConfigIn
import models.persistence.Persistence
import models.bo.StepBO
import models.bo.ComponentBO
import models.bo.StepCurrentConfigBO
import models.currentConfig.CurrentConfig
import models.json.startConfig.JsonStartConfigOut

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 19.02.2018
 */

object StartConfig{
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param StartConfigIn
   * 
   * @return StartConfigOut
   */
  def startConfig(startConfigIn: StartConfigIn): StartConfigOut = {
    new StartConfig(startConfigIn.configUrl).getFirstStep
  }
}

class StartConfig(configUrl: String) {
  
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return StartConfigOut
   */
  private def getFirstStep: StartConfigOut = {

    val firstStep: StepBO = Persistence.getFirstStep(configUrl)
    
    val components: List[ComponentBO] = Persistence.getComponents(firstStep.stepId) match {
      case Some(components) => components
      case None => List()
    }
    
    
    
    val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
          firstStep.stepId,
          firstStep.nameToShow,
          List(),
          None
      )
      
  // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
  CurrentConfig.addFirstStep(firstStepCurrentConfig)
    
    StartConfigOut(
        firstStep,
        components
    )
  }
}