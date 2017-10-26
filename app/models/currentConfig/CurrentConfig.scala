package models.currentConfig

import scala.collection.mutable.Map 
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import org.dto.currentConfig.CurrentConfigResult
import models.json.Step
import models.json.currentConfig.CurrentConfigIn
import models.json.currentConfig.CurrentConfigOut

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
    
    
    val currentConfigSteps: List[Step] = currentConfigs.get(clientId) match {
      case Some(step) => currentConfigs.get(clientId).get
      case None => currentConfig.setCurrentConfig(step, List[Step]())
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
  
  
  def getCurrentConfig(currentConfigIn: CurrentConfigIn): CurrentConfigOut = {
    
    val currentConfigSteps: List[Step] = currentConfigs.get(currentConfigIn.params.clientId) match {
      case Some(step) => step
      case None => List[Step]()
    }
    
    CurrentConfigOut(
        result = CurrentConfigResult(
            currentConfigSteps
        )
    )
  }
  
  def getCuttentConfig: Map[String, List[Step]] = {
    currentConfigs
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
