package org.persistence

import org.status.SuccessfulStatus
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable

import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL
import org.persistence.db.orientdb.StepVertex
import org.persistence.db.orientdb.ComponentVertex
import org.persistence.db.orientdb.HasComponentEdge
import org.persistence.db.orientdb.NextStepEdge
import org.persistence.db.orientdb.OrientDB
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Vertex
import org.status.Status
import org.dto.startConfig.StartConfigCS
import org.dto.startConfig.StartConfigSC
import org.dto.nextStep.NextStepSC
import org.dto.nextStep.NextStepCS

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 22.09.2016
 */

object Persistence {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.1.0
   * 
   * @param
   * 
   * @return
   */
  def startConfig(startConfigCS: StartConfigCS) : StartConfigSC = {
     val startConfigSC: StartConfigSC = StepVertex.firstStep(startConfigCS)
     startConfigSC
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.1.0
   * 
   * @param
   * 
   * @return
   */
  def nestStep(nextStepCS: NextStepCS): NextStepSC = {
    StepVertex.nextStep(nextStepCS)
  }
  
  
  
  def rules() = ???
 
}