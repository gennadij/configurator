package org.configMgr

import org.configTree.component.Component
import org.configTree.step.Step
import org.container.Container
import scala.collection.mutable.ListBuffer
import org.container.Container
import org.container.Container
import org.configSettings.ConfigSettings
import org.configTree.step.AbstractStep
import org.configTree.step.DefaultStep
import org.configTree.step.FinalStep
import org.configTree.step.FirstStep

/**
 * TODO
  * - first Stepp ist immer erste hinzugefügte Step zu dem Config Setting
  * - Configuration zu der CurrentCOnfig sofort bei der Auruf von nextStep hinzufügen.
  * - create Factory Object
 * 
 */
class ConfigMgr {
  
  val container = ConfigSettings.configSettings

  /**
    * durch den Typunterscheidung mit match herausfiltern
    * @return
    */
  def startConfig = {
    val firstStep = container.configSettings filter(_.isInstanceOf[FirstStep])
    if(firstStep.size == 1) firstStep(0) else null
  }

  def getNextStep(selectedComponentId: String): AbstractStep = {
    
    addStepToCurrentConfig(selectedComponentId)
    
    val nextStep = for{
      step <- container.configSettings
      nextStep <- step.nextStep if(nextStep.byComponent == selectedComponentId)
    }yield nextStep
    if(nextStep(0).nextStep == "000")
      new FinalStep("000", "I am final step")
    else
      (container.configSettings filter (_.id == nextStep(0).nextStep))(0)
  }
  
  /**
   * neue step für CurrentConfig
   */
  private def addStepToCurrentConfig(selectedComponentId: String) = {
    
    // return new val mit veränderer currentConfig mit zusätzlichem step
    val step = for {
      step <- container.configSettings
      component <- step.nextStep if (component.byComponent == selectedComponentId)
    }yield new DefaultStep(step.id, step.nameToShow, step.nextStep, step.kind, 
                              step.selectionCriterium, step.from, getComponent(step, selectedComponentId))
    val index = container.currentConfig.indexWhere(s => step(0).id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += step(0) 
  }
  
  def getComponent(step: AbstractStep, selectedComponentId: String) = step.components filter (_.id == selectedComponentId)
    
    /**
    * - fuege der Step und loesche alle Step nach dem hinzugefügtem Step
    * -  damit die CurrentConfifuration immer aktuell iste , wenn
    * - die Stepps gemischt ausgewaelt werden.
    * - bei wiederkehrender Auswahl der Komponenten prüfen ob der Auswahl möglich ist
    *     Z.b Step 006 kann wiederkehrend Ausgewählt wenn component 004003 schon Ausgewählt war
    *
    * @param container
    * @param selectedComponentId
    * @return
    */

//  def addStepToCurrentConfig(container: Container, selectedComponentId: String) = {
//
//    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)
//
//    val indexStep = for {
//      step <- container.currentConfig if (currentStep.id == step.id)
//    } yield step
//
//    val index = container.currentConfig.indexWhere(s => currentStep.id == s.id)
//
//    val currentConfigSize = container.currentConfig.size
//
//    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)
//
//    container.currentConfig += currentStep
//  }
  
  
  /**
    * TODO check if more component find, or multichose component selected
    * @param container
    * @return
    */
//  def startConfig(container: Container): Step = {
//    (container.configSettings filter(_.id == "001"))(0)
//  }
  
  
//  def getSelectedComponent(container: Container, id: String) = {
//    container.configSettings flatMap (s => s.components filter (_.id == id))
//  }

  /**
    * erzeugt eine Kopie des Stepes mir parametrisierten Commponent ID
    *
    *
    * @param container
    * @param selectedComponentId
    * @return Step -> Simple choice, null -> multiple choice
    */
//  private def getStepWithSelectedComponent(container: Container, selectedComponentId: String): Step = {
//    val steps = for {
//      step <- container.configSettings
//      component <- step.components
//    } yield
//    if(component.id == selectedComponentId) {
//      new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
//    } else null
//    val filteredSteps = steps filter { _ != null }
//    if(filteredSteps.size < 1){
//      null
//    }else {
//      filteredSteps(0)
//    }
//  }
  
//  def getCurrentStep(container: Container):Step = {
//    container.currentConfig.last
//  }

  /**
    *
    * @param container
    * @param selectedComponentId
    * @return
    */

//  def  getNextStep(container: Container, selectedComponentId: String) = {
//
//    //testen jeden Step ob er letzte step ist, wenn einen Letzten Step gefunden einen objekte LastStep zurückgeben
//
//    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
//    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStep
//    val nextStep = container.configSettings filter (_.id == nextStepId)
//    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)
//
//    if(nextStepId == "000") {
//      (null, currentStep)
//    }
//    else {
//      (nextStep(0), currentStep)
//    }
//  }
  
//  def valideSteps(container: Container): Boolean = {
//    
//    val isStartStep = container.configSettings filter(_.isStartStep == true)
//    
//    if(isStartStep.length == 1) true else false
//  }
}