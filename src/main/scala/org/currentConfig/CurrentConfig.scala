package org.currentConfig

import scala.collection.mutable.Map
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.dto.currentConfig.CurrentConfigCS
import org.dto.currentConfig.CurrentConfigSC
import org.dto.currentConfig.CurrentConfigResult
import org.dto.Step

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 * 
 */
object CurrentConfig {
  private val currentConfigs: Map[String, List[Step]] = Map.empty
  
  val currentConfig: CurrentConfig = new CurrentConfig
  
  def setCurrentConfig(clientId: String, step: Step): Unit = {
    
    
//    val currentConfigSteps: List[Step] = if (currentConfigs.get(clientId) == None) {
//      
//    }
    
    val currentConfigSteps: List[Step] = currentConfigs.get(clientId) match {
      case Some(step) => currentConfigs.get(clientId).get
      case None => currentConfig.setCurrentConfig(step, List[Step]())
//      case _ => println("Fehler bei CurrentConfig")
    }
    
    
//    val currentConfigSteps: List[Step] = currentConfigs.get(clientId).get
    
    
    
    currentConfigs += (clientId -> currentConfigSteps)
  }
  
  
  def getCurrentConfig(currentConfigCS: CurrentConfigCS): CurrentConfigSC = {
    
    val currentConfigSteps: List[Step] = currentConfigs.get(currentConfigCS.params.clientId) match {
      case Some(step) => step
      case None => List[Step]()
    }
    
    CurrentConfigSC(
        result = CurrentConfigResult(
            currentConfigSteps
        )
    )
  }
}

class CurrentConfig {

  private def setCurrentConfig(step: Step, currentConfigSteps: List[Step]): List[Step] = {
    
    val newStep = Step(
        step.stepId,
        step.nameToShow,
        step.components
    )
    
    currentConfigSteps.+:(newStep)
  }
  
  private def getCurrentConfig() = ???
}
