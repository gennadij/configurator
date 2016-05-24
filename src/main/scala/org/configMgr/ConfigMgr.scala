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
import org.configTree.step.ErrorStep
import org.configTree.step.LastStep
import org.configTree.step.NextStep

/**
 * TODO
 * - Die Reihenfolge der Schritten prüfen
 * - Lösung zu der zentrallen Stelle aller Fehler in dem Configurator
 * - definition von dem mutable Components (Slide, variable Massen)
 * - Lösung zu der Immutable CurrentConfig suchen
 * - Logger einrichten
 * - Prüfung einbauen, wenn Komponent, der nicht in Step exestiert, ausgewählt
 * - Selection criterium checken, wenn 2 meherere Components ausgewaehlt werden koennen 
 * - Selection Criterium to Int konvertieren
 */

object ConfigMgr{
  val configMgr = new ConfigMgr

  def currentConfig = {
    configMgr.container.currentConfig
  }
  
  def startConfig = {
    configMgr.startConfig
  }
  
  def getNextStep(selectedComponentIds: List[String]) = {
    configMgr.getNextStep(selectedComponentIds)
  }
  
  def getStepOfComponent(selectedComponentIds: List[String]): AbstractStep ={
    configMgr.getStepOfComponents(selectedComponentIds)
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
    if(firstStep.size == 1) firstStep(0) else null
  }
  
  /**
   * - addStepToCurrentConfig(selectedComponentIds(0)) vor List vorbereiten
   * - nextStep Id bei der Multichoose Component muss bei allen componentId mit Step Id übereinstimmen 
   */
  def getNextStep(selectedComponentIds: List[String]): AbstractStep = {
    
    val step = getStepOfComponents(selectedComponentIds)
    
    if(step.isInstanceOf[ErrorStep]){
      step
    }else{
      if(checkSelectionCriterium(step, selectedComponentIds)){
        addStepToCurrentConfig(selectedComponentIds(0))
        
        //TODO test
        val nextStep = step.nextStep filter(_.byComponent == selectedComponentIds(0))
          
        if(checkNextSteps(step, selectedComponentIds)){
          // TODO has yet to be implemented
          if(nextStep(0).step == "000")
            new FinalStep("0", "I am final step")
          else
            (container.configSettings filter (_.id == nextStep(0).step))(0)
          
          
        }else{
          new ErrorStep("7", "error step", "has yet to be implemented")
        }
      }else{
        new ErrorStep("7", "error step", "has yet to be implemented")
      }
    }
    
    
    
//    val nextStep = for{
//      step <- container.configSettings
//      nextStep <- step.nextStep if(nextStep.byComponent == selectedComponents(0))
//    }yield nextStep
    
//    if(nextStep(0).nextStep == "000")
//      new FinalStep("000", "I am final step")
//    else
//      (container.configSettings filter (_.id == nextStep(0).nextStep))(0)
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
  
  def checkSelectionCriterium(step: AbstractStep, selectedComponentIds: List[String]): Boolean = {
    val ids = step.components map (_.id)
    if(selectedComponentIds.size >= step.selectionCriterium.min.toInt &&
          selectedComponentIds.size <= step.selectionCriterium.max.toInt){
      true
    }else{
      false
    }
    
//    val step = getStepOfComponents(selectedComponentIds)
//    
//    val componentsExistInStep = for{
//      step <- container.configSettings
//    } yield checkComponents(step, selectedComponentIds)
//    if ((componentsExistInStep filter (_ == true)).size == 1){
//      (componentsExistInStep filter (_ == true))(0)
//    }else {
//      false
//    }
  }
  /**
   * sucht den Step in Config Settings mit Hilfe der IDs des Components
   * 
   * @return Step mit dem Components
   */
  
  private def getStepOfComponents(selectedComponentIds: List[String]): AbstractStep = {
    val steps = for{
      step <- container.configSettings
    }yield {
      val ids = step.components map (_.id)
      if(ids.exists { selectedComponentIds.contains _ }){
        step
      }else{
        new ErrorStep("7", "error step", "The selected components has " + 
            "not been found in any configuration step")
      }
    }
    val filterdSteps = steps filter (!_.isInstanceOf[ErrorStep])
    if(filterdSteps.size == 1){
      filterdSteps(0)
    }else{
      new ErrorStep("7", "error step", "found more than one step with " + 
          "selected components")
    }
      
  }
  
  def checkComponents(step: AbstractStep, selectedComponents: List[String]) = {
    val ids = step.components map (_.id)
    if(ids.exists ( selectedComponents contains _ ) &&
          selectedComponents.size >= step.selectionCriterium.min.toInt &&
          selectedComponents.size <= step.selectionCriterium.max.toInt){
      true
    }else{
      false
    }
  }
  
  private def checkNextSteps(step: AbstractStep, selectedComponentIds: List[String]) = {
    val nextStep: Seq[NextStep] = for{
      nextStep <- step.nextStep
      selCom <- selectedComponentIds
    }yield if (nextStep.byComponent == selCom) nextStep else null
    
    checkElem(nextStep filter (_.isInstanceOf[NextStep]) map (_.step))
  }
  
  def checkElem(list: Seq[String]) = {
    list match {
      case x :: rest => rest forall (_ == x)
      case _ => true
    }
  }
  
  
//  def valideSteps(container: Container): Boolean = {
//    
//    val isStartStep = container.configSettings filter(_.isStartStep == true)
//    
//    if(isStartStep.length == 1) true else false
//  }
}