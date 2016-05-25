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
import org.configTree.step.SuccessStep

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
  
  def getNextStep(selectedComponentIds: Set[String]) = {
    configMgr.getNextStep(selectedComponentIds)
  }
  
  def getStepOfComponent(selectedComponentIds: Set[String]): AbstractStep ={
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
   * - currentConfig für den Multichoose Komponent erweitern
   */
  def getNextStep(selectedComponentIds: Set[String]): AbstractStep = {
    checkSelectedComponentIds(selectedComponentIds) match {
        case errorStep: ErrorStep => errorStep
        //TODO final step testen
        case lastStep: LastStep => new FinalStep("0", "I am final step")
        case step => {
//          addStepToCurrentConfig(selectedComponentIds.head)
//          val nextStep = step.nextStep filter(_.byComponent == selectedComponentIds.head)
          checkNextSteps(step, selectedComponentIds)
          
        }
    }

//    val step = getStepOfComponents(selectedComponentIds)
    
//    if(step.isInstanceOf[ErrorStep]){
//      step
//    }else{
//      checkSelectionCriterium(step, selectedComponentIds) match {
//        case errorStep : ErrorStep => errorStep
//        case successStep: SuccessStep => {
//          addStepToCurrentConfig(selectedComponentIds.head)
//          val nextStep = step.nextStep filter(_.byComponent == selectedComponentIds.head)
//          if(checkNextSteps(step, selectedComponentIds)){
//            // TODO has yet to be implemented
//            if(nextStep(0).step == "000")
//              new FinalStep("0", "I am final step")
//            else
//              (container.configSettings filter (_.id == nextStep(0).step))(0)
//          }else{
//            new ErrorStep("7", "error step", "has yet to be implemented")
//          }
//        }
//      }
//    }
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
  
  private def getComponent(step: AbstractStep, selectedComponentId: String) = {
    step.components filter (_.id == selectedComponentId)
  }
  // TODO Verschieden ErrorStep werfen wenn max oder min verstoßen wurde
//  def checkSelectionCriterium(  step: AbstractStep, 
//                                selectedComponentIds: Set[String]): AbstractStep = {
//    
//    val min = step.selectionCriterium.min.toInt
//    val max = step.selectionCriterium.max.toInt
//    
//    selectedComponentIds.size match {
//      case com1: Int if com1 < min => ErrorStep("7", "error step", "selected to few components")
//      case com2: Int if com2 > max => ErrorStep("7", "error step", "selected to match components")
//      case _ => SuccessStep("3", "success step", "available selection criterium")
//    }
//  }
  
  def checkSelectionCriterium(  step: AbstractStep, 
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
        new ErrorStep("7", "error step", "The selected components has " + 
            "not been found in any configuration steps")
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
      //TODO testen
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