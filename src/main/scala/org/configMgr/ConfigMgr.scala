package org.configMgr

import org.configTree.{Component, Step}
import org.container.Container

import scala.collection.mutable.ListBuffer

/**
 * TODO
 * 
 */
class ConfigMgr {
  //TODO create Factory Object

  def loadCurrentConfig : CurrentConfig = {
    new CurrentConfig(ListBuffer.empty)
  }
  
  def getSelectedComponent(container: Container, id: String) = {
    container.configSettings flatMap (s => s.components filter (_.id == id))
  }

  def getStepWithSelectedComponent(container: Container, id: String): Seq[Step] = {
    val steps = for {
      step <- container.configSettings
      component <- step.components
    } yield 
    if(component.id == id) new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
    else null
    steps filter { _ != null }
  }
  
  def getCurrentStep():Step = ???
  
  def  getNextStep(container: Container, selectedComponentId: String) = {
    
    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStep
    val nextStep = container.configSettings filter (_.id == nextStepId)
    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)
    if(nextStepId == "000") {
      (null, currentStep(0))
    }
    else {
      (nextStep(0), currentStep(0))
    }
  }

  
  def getFirstStep(container: Container): Step = {
    val firstStep = container.configSettings filter (s => s.isStartStep == "true")
    if(firstStep.size == 1){
      firstStep(0)
    }else{
      new Step("999")
    }
  }
  
  
  def valideSteps(container: Container): Boolean = {
    
    val isStartStep = container.configSettings filter(_.isStartStep == true)
    
    if(isStartStep.length == 1) true else false
  }
}