package models.logic

import models.wrapper.startConfig.StartConfigOut
import models.wrapper.startConfig.StartConfigIn
import models.persistence.Persistence
import models.bo.StepBO
import models.bo.ComponentBO

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 19.02.2018
 */

object StartConfig{
  def startConfig(startConfigIn: StartConfigIn): StartConfigOut = {
    new StartConfig(startConfigIn.configUrl).getFirstStep
  }
}

class StartConfig(configUrl: String) {
  
  private def getFirstStep: StartConfigOut = {
    //TODO Wenn selectionCriterium min = 0 und max > 1 darf der Benutzer ohne ausgewählte Komponente zu dem weiterem Schritt gehen
    //Bei diesem SelectionCriterium soll beim Laden von dem Step das Naechste Schritt Button akteviert werden.
    val firstStep: StepBO = Persistence.getFirstStep(configUrl)
    
    val components: List[ComponentBO] = Persistence.getComponents(firstStep.stepId)
    
//    val firstStepCurrentConfig: StepCurrentConfigBO = StepCurrentConfigBO(
//          firstStep.stepId,
//          firstStep.nameToShow,
//          List(),
//          None
//      )
//      
//      // Fuege den ersten Schritt zu der aktuelle Konfiguration hinzu
//      CurrentConfig.addStep(Some(firstStepCurrentConfig), None)
//      
//      val status: Status = new StartConfigSuccessful()
//      StartConfigOut(
//          Some(Step(
//              vFirstStep.getIdentity.toString,
//              vFirstStep.getProperty(PropertyKeys.NAME_TO_SHOW),
//              getComponentsFromNextStep(vFirstStep)
//          )),
//          status.status,
//          status.message
//      )
    
    
    
    
    ???
    
    
  }
}