package org.configMgr

import org.configTree.component._
import org.container.Container
import scala.collection.mutable.ListBuffer
import org.container.Container
import org.container.Container
import org.configSettings.ConfigSettings
import org.configTree.step.AbstractStep
import org.configTree.step.DefaultStep
import org.configTree.step.FinalStep
import org.configTree.step.FirstStep
import org.configTree.step.ErrorStep
import org.configTree.step.LastStep
import org.configTree.step.NextStep
import org.configTree.step.SuccessStep
import org.configTree.step.CurrentConfigStep

/**
 * TODO
 * - Logger einrichten
 * - Die Reihenfolge der Schritten prüfen
 * - Lösung zu der zentrallen Stelle aller Fehler in dem Configurator
 * - definition von dem mutable Components (Slide, variable Massen)
 * - Lösung zu der Immutable CurrentConfig suchen
 * - Selection criterium checken, wenn 2 meherere Components ausgewaehlt werden koennen 
 * - Selection Criterium to Int konvertieren
 * - selectionCriteriumMax darf nicht grösser als Anzahl der Components sein und Umgekehrt
 * - ID durch 100100 ersetzen => Typ Int
 */

object ConfigMgr{
  val configMgr = new ConfigMgr

  def currentConfig = {
    configMgr.container.currentConfig
  }
  
  def startConfig = {
    configMgr.startConfig
  }
  
  def getNextStep(selectedComponentIds: Set[SelectedComponent]) = {
    configMgr.getNextStep(selectedComponentIds)
  }
}

class ConfigMgr {
  
  val container = ConfigSettings.configSettings

  /**
    * - durch den Typunterscheidung mit match herausfiltern
    * - ErrorStep als Fehler difenieren
    * @return
    */
  def startConfig = {
    val firstStep = container.configSettings filter(_.isInstanceOf[FirstStep])
    if(firstStep.size == 1) firstStep(0) else new ErrorStep("7", "error step", "exist more as one step")
  }
  
  /**
   * - addStepToCurrentConfig(selectedComponentIds(0)) vor List vorbereiten
   * - nextStep Id bei der Multichoose Component muss bei allen componentId mit Step Id übereinstimmen 
   * - currentConfig für den Multichoose Komponent erweitern
   */
  def getNextStep(selectedComponents: Set[SelectedComponent]): AbstractStep = {
    checkSelectedComponents(selectedComponents) match {
        case errorStep: ErrorStep => errorStep
        //TODO final step testen, letzte step wird nicht in die CurrentConfig hinzugefuegt
        case lastStep: LastStep => new FinalStep("0", "I am final step")
        case step => {
          addStepToCurrentConfig(step, selectedComponents)
          checkNextSteps(step, selectedComponents)
        }
    }
  }
  
  /**
   * neue step für CurrentConfig
   */
  def addStepToCurrentConfig(step: AbstractStep, selectedComponents: Set[SelectedComponent]) = {
    
    val stepForCurrentConfig: CurrentConfigStep = new CurrentConfigStep(step.id, step.nameToShow, 
                              step.selectionCriterium, getComponent(step, selectedComponents))
        
    val index = container.currentConfig.indexWhere(s => stepForCurrentConfig.id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += stepForCurrentConfig
  }
  
  private def getComponent(step: AbstractStep, selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    val selectedComponentIds = selectedComponents map (_.id)
    val components = for{
      component <- step.components
      id <- selectedComponentIds
    }yield if (component.id == id) component else new ErrorComponent("7", "error compnent", "error")
    
    components filter { ! _.isInstanceOf[ErrorComponent]}
    
  }
  
  
  private def checkParameterOfSelectedComponents(step: AbstractStep, 
                  selectedComponents: Set[SelectedComponent]): AbstractStep = {
    
    val components = step.components filter (_.isInstanceOf[MutableComponent])
    
    for{
      component <- components
      selectedComponent <- selectedComponents
    } yield {
      if(component.id == selectedComponent.id){
        if(selectedComponent.minValue < component.minValue &&
            selectedComponent.maxValue > component.maxValue){
          ErrorStep("7", "error step", "minValue is smaller or naxValue is greater as definition in configSttings")
        }
      }
      
      
      component
    }
    
    ErrorStep("7", "error step", "not implemented")
    
  }
  
  
  private def checkSelectionCriterium(  step: AbstractStep, 
                                selectedComponents: Set[SelectedComponent]): AbstractStep = {
    
    val min = step.selectionCriterium.min.toInt
    val max = step.selectionCriterium.max.toInt
    
    val selectedComponentIds = selectedComponents map (_.id)
    selectedComponentIds.size match {
      case com1: Int if com1 < min => new ErrorStep("7", "error step", "selected to few components")
      case com2: Int if com2 > max => new ErrorStep("7", "error step", "selected to match components")
      case _ => step
    }
  }
  
  /**
   * sucht den Step in Config Settings mit Hilfe der IDs des Components
   * 
   * @return Step mit dem Components
   */
  
  private def getStepOfComponents(selectedComponents: Set[SelectedComponent]): AbstractStep = {
    val steps = for{
      step <- container.configSettings
    }yield {
      val ids = step.components map (_.id)
      val selectedComponentIds = selectedComponents map (_.id)
      if(ids.exists { selectedComponentIds.contains _ }){
        step
      }else{
        null
      }
    }
    val filterdSteps = steps filter (_ != null)
    
    if(filterdSteps.size == 1){
      filterdSteps(0)
    }else{
      new ErrorStep("7", "error step", "The selected components has " + 
            "not been found in any configuration steps")
    }
  }
  
  /**
   * Vergleicht in currentStep.nextStep die difenierten componentIds mit 
   * selectedComponentIds
   * 
   * Bei der Multichoose werden die difenierte next Steps verglichen. Es darf nicht bei 
   * der verschiedenen selectedComponentsIds verscheidene nextSteps difeniert werden
   */
  private def checkNextSteps(step: AbstractStep, selectedComponents: Set[SelectedComponent]): AbstractStep = {
    val selectedComponentIds = selectedComponents map (_.id)
    val nextStep: Seq[NextStep] = for{
      nextStep <- step.nextStep
      selCom <- selectedComponentIds
    }yield if (nextStep.byComponent == selCom) nextStep else null
    
    checkElem(nextStep filter (_.isInstanceOf[NextStep]) map (_.step)) match {
      case true => {
        val nextStep = step.nextStep filter(_.byComponent == selectedComponentIds.head)
        (container.configSettings filter (_.id == nextStep(0).step))(0)
      }
      case false => new ErrorStep("7", "error step", "nextSteps for selectedComponentIds was not same")
    }
  }
  
  private def checkElem(list: Seq[String]) = {
    list match {
      case x :: rest => rest forall (_ == x)
//      case _ => true
    }
  }
  
  private def checkSelectedComponents(selectedComponents: Set[SelectedComponent]): AbstractStep = {
    
    
    
    getStepOfComponents(selectedComponents) match {
      case errorStep: ErrorStep => errorStep
      case step => checkSelectionCriterium(step, selectedComponents)
    }
  }
}