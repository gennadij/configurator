package models.persistence

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable

import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Vertex
import models.json.startConfig.JsonStartConfigOut
import models.json.startConfig.JsonStartConfigIn
import models.wrapper.startConfig.StartConfigIn
import models.wrapper.startConfig.StartConfigOut
import models.wrapper.nextStep.NextStepIn
import models.wrapper.nextStep.NextStepOut
import models.persistence.db.orientdb.StepVertex
import models.persistence.orientdb.ComponentVertex
import models.wrapper.component.ComponentOut
import models.wrapper.component.ComponentIn

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 22.09.2016
 */

object Persistence {
  
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
   * @version 0.0.1
   * 
   * @param ComponentIn
   * 
   * @return ComponentOut
   */
  def component(componentIn: ComponentIn): ComponentOut = {
    ComponentVertex.component(componentIn)
  }
}