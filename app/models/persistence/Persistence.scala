package models.persistence

import scala.collection.JavaConversions._
import models.json.startConfig.JsonStartConfigOut
import models.json.startConfig.JsonStartConfigIn
import models.wrapper.startConfig.StartConfigIn
import models.wrapper.startConfig.StartConfigOut
import models.wrapper.nextStep.NextStepIn
import models.wrapper.nextStep.NextStepOut
import models.persistence.db.orientdb.StepVertex
import models.wrapper.component.ComponentOut
import models.bo.ComponentBO
import models.bo.StepBO
import models.persistence.orientdb.Graph

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 22.09.2016
 */

object Persistence {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String
   * 
   * @return StepBO
   */
  
  def getFirstStep(configUrl: String): StepBO = {
    Graph.getFirstStep(configUrl)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param String
   * 
   * @return List[ComponentBO]
   */
  def getComponents(stepId: String): List[ComponentBO] = {
    Graph.getComponents(stepId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param StartConfigIn
   * 
   * @return StartConfigOut
   */
  def startConfig(startConfigIn: StartConfigIn) : StartConfigOut = {
    StepVertex.firstStep(startConfigIn)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param NextStepIn
   * 
   * @return NextStepOut
   */
  def nestStep(nextStepIn: NextStepIn): NextStepOut = {
    StepVertex.nextStep(nextStepIn)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return 
   */
  def getSelectedComponent(selectedComponentId: String): Option[ComponentBO] = {
    Graph.getComponent(selectedComponentId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return 
   */
  def getFatherStep(componentId: String): StepBO = {
    Graph.getFatherStep(componentId)
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return 
   */
  def getNextStep(componentId: String): StepBO = {
    Graph.getNextStep(componentId)
  }
}