package models.currentConfig

import scala.collection.mutable.Map 
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.dto.currentConfig.CurrentConfigResult
import models.json.common.JsonStep
import models.json.currentConfig.JsonCurrentConfigIn
import models.json.currentConfig.JsonCurrentConfigOut

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Crated by Gennadi Heimann 28.02.2017
 * 
 */
object CurrentConfig {
  private val currentConfigs: Map[String, List[JsonStep]] = Map.empty
  
  val currentConfig: CurrentConfig = new CurrentConfig
  
  def setCurrentConfig(clientId: String, step: JsonStep): Unit = {
    
    
    val currentConfigSteps: List[JsonStep] = currentConfigs.get(clientId) match {
      case Some(step) => currentConfigs.get(clientId).get
      case None => currentConfig.setCurrentConfig(step, List[JsonStep]())
//      case _ => println("Fehler bei CurrentConfig")
    }
    
    val indexOfSelectedStep = currentConfigSteps.indexWhere(s => s.stepId == step.stepId)
    
    if(indexOfSelectedStep == -1) {
      currentConfigs += (clientId -> (currentConfigSteps :+ step))
    }else {
       val countOfStepToDelete = currentConfigSteps.size - indexOfSelectedStep
       val steps = currentConfigSteps.dropRight(countOfStepToDelete)
       currentConfigs. += (clientId -> (steps :+ step))
    }
  }
  
  
  def getCurrentConfig(currentConfigIn: JsonCurrentConfigIn): JsonCurrentConfigOut = {
    
    val currentConfigSteps: List[JsonStep] = currentConfigs.get(currentConfigIn.params.clientId) match {
      case Some(step) => step
      case None => List[JsonStep]()
    }
    
    JsonCurrentConfigOut(
        result = CurrentConfigResult(
            currentConfigSteps
        )
    )
  }
  
  def getCuttentConfig: Map[String, List[JsonStep]] = {
    currentConfigs
  }
}

class CurrentConfig {

  private def setCurrentConfig(step: JsonStep, currentConfigSteps: List[JsonStep]): List[JsonStep] = {
    
    val newStep = JsonStep(
        step.stepId,
        step.nameToShow,
        step.components
    )
    
    currentConfigSteps.+:(newStep)
  }
  
  private def getCurrentConfig() = ???
}
