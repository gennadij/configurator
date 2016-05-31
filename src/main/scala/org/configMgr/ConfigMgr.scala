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
 */

object ConfigMgr{
  val configMgr = new ConfigMgr

  def currentConfig = {
    configMgr.container.currentConfig
  }
  
  def startConfig = {
    configMgr.startConfig
  }
  
  def getNextStep(selectedComponentIds: Set[String]) = {
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
  def getNextStep(selectedComponentIds: Set[String]): AbstractStep = {
    checkSelectedComponentIds(selectedComponentIds) match {
        case errorStep: ErrorStep => errorStep
        //TODO final step testen, letzte step wird nicht in die CurrentConfig hinzugefuegt
        case lastStep: LastStep => new FinalStep("0", "I am final step")
        case step => {
          addStepToCurrentConfig(step, selectedComponentIds)
          checkNextSteps(step, selectedComponentIds)
        }
    }
  }
  
  /**
   * neue step für CurrentConfig
   */
  def addStepToCurrentConfig(step: AbstractStep, selectedComponentIds: Set[String]) = {
    
        val stepForCurrentConfig: CurrentConfigStep = new CurrentConfigStep(step.id, step.nameToShow, 
                              step.selectionCriterium, getComponent(step, selectedComponentIds))
        
    val index = container.currentConfig.indexWhere(s => stepForCurrentConfig.id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += stepForCurrentConfig
  }
  
  private def getComponent(step: AbstractStep, selectedComponentIds: Set[String]): Seq[Component] = {
    val components = for{
      component <- step.components
      id <- selectedComponentIds
    }yield if (component.id == id) component else new ErrorComponent("7", "error compnent", "error")
    
    components filter { ! _.isInstanceOf[ErrorComponent]}
    
  }
  
  
  
  private def checkSelectionCriterium(  step: AbstractStep, 
                                selectedComponentIds: Set[String]): AbstractStep = {
    
    val min = step.selectionCriterium.min.toInt
    val max = step.selectionCriterium.max.toInt
    
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
  
  private def getStepOfComponents(selectedComponentIds: Set[String]): AbstractStep = {
    val steps = for{
      step <- container.configSettings
    }yield {
      val ids = step.components map (_.id)
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
  private def checkNextSteps(step: AbstractStep, selectedComponentIds: Set[String]): AbstractStep = {
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
  
  private def checkSelectedComponentIds(selectedComponentIds: Set[String]): AbstractStep = {
    getStepOfComponents(selectedComponentIds) match {
      case errorStep: ErrorStep => errorStep
      case step => checkSelectionCriterium(step, selectedComponentIds)
    }
  }
}