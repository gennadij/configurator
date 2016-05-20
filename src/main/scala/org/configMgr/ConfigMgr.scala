package org.configMgr

import org.configTree.component.Component
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
 * - Die Reihenfolge der Schritten prüfen
 * - Lösung zu der zentrallen Stelle aller Fehler in dem Configurator
 * - definition von dem mutable Components (Slide, variable Massen)
 * - Lösung zu der Immutable CurrentConfig suchen
 * - Logger einrichten
 * - Prüfung einbauen, wenn Komponent, der nicht in Step exestiert, ausgewählt
 * - MultiComponent kombeniert Mutable und ImmutableComponent in einem in einem Steps
 * - multichoose bei der Auswahl der Componenten
 * - Selection criterium checken, wenn 2 meherere Components ausgewaehlt werden koennen 
 */

object ConfigMgr{
  val configMgr = new ConfigMgr

  def currentConfig = {
    configMgr.container.currentConfig
  }
  
  def startConfig = {
    configMgr.startConfig
  }
  
  def getNextStep(selectedComponentId: String) = {
    configMgr.getNextStep(selectedComponentId)
  }
}

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

  def getNextStep(selectedComponents: List[String]): AbstractStep = {
    
    checkSelectionCriterium(selectedComponents)
    
    addStepToCurrentConfig(selectedComponents(0))
    
    val nextStep = for{
      step <- container.configSettings
      nextStep <- step.nextStep if(nextStep.byComponent == selectedComponents(0))
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
    
    val step = for {
      step <- container.configSettings
      component <- step.nextStep if (component.byComponent == selectedComponentId)
    }yield new DefaultStep(step.id, step.nameToShow, step.nextStep,
                              step.selectionCriterium, step.from, getComponent(step, selectedComponentId))
    val index = container.currentConfig.indexWhere(s => step(0).id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += step(0) 
  }
  
  private def getComponent(step: AbstractStep, selectedComponentId: String) = step.components filter (_.id == selectedComponentId)
  
  private def checkSelectionCriterium(selectedComponents: List[String]) = {
    if(selectedComponents.size == 1) true
    else {
      for{
        step <- container.configSettings 
      } yield checkComponents(step.components, selectedComponents)
    }
  }
  
  private def getStepOfComponent(components: List[String]): AbstractStep = {
//    container.configSettings map (_.components.contains())
    null
  }
  
  def checkComponents(components: Seq[Component], selectedComponents: List[String]) = {
    val ids = components map (_.id)
    ids.exists ( components contains _ )
  }
  
  
  
//  def valideSteps(container: Container): Boolean = {
//    
//    val isStartStep = container.configSettings filter(_.isStartStep == true)
//    
//    if(isStartStep.length == 1) true else false
//  }
}