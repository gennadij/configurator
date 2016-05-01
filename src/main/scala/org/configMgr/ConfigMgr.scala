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

  
  def getSelectedComponent(container: Container, id: String) = {
    container.configSettings flatMap (s => s.components filter (_.id == id))
  }

  /**
    * erzeugt eine Kopie des Stepes mir parametrisierten Commponent ID
    * @param container
    * @param selectedComponentId
    * @return
    */
  private def getStepWithSelectedComponent(container: Container, selectedComponentId: String): Seq[Step] = {
    val steps = for {
      step <- container.configSettings
      component <- step.components
    } yield
    //testen
      //component <- step.components if(component.id == selectedComponentId)
      //yield new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
    if(component.id == selectedComponentId) new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
    else null
    steps filter { _ != null }
  }
  
  def getCurrentStep():Step = ???



  def addStepToCurrentConfig(container: Container, selectedComponentId: String) = {
    // fuege der Step und loesche alle Step nach dem hinzugefügtem Step
    // damit die CurrentConfifuration immer aktuell iste , wenn
    // die Stepps gemischt ausgewaelt werden.

    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)

//    val currentStep = for {
//      step <- container.configSettings
//      component <- step.components if(component.id == selectedComponentId)
//    } yield

    // Der Step muss an der Richtige Position hinzugefuegt.
    container.currentConfig += currentStep(0)
  }


  def  getNextStep(container: Container, selectedComponentId: String) = {

    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStep
    val nextStep = container.configSettings filter (_.id == nextStepId)
    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)

    container.currentConfig += currentStep(0)
    println(container.currentConfig.size)

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