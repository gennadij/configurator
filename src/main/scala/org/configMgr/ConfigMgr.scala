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
import org.configTree.ConfigTree

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
 * - nur bei MutableComponent kann der value übergeben werden
 * - alle Überprüfungen in eine Methode zusammenführen
 * - Spec für minValues und maxValues schreiben
 * - Fehler -> wenn Immutable Component value gesetzt wird (Test dazu)
 * - Test min max Value funktioniert nicht
 * - ErrorStrings in der zentralle Stelle definieren
 * - Problem mit Error beheben
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
  
  def addError(error: ConfigTree) = {
    
    error match {
      case errorComponent: ErrorComponent => Nil
      case errorStep: ErrorStep => Nil
    }
  }

  /**
    * - durch den Typunterscheidung mit match herausfiltern
    * - ErrorStep als Fehler difenieren
    * @return
    */
  def startConfig = {
    val firstStep = container.configSettings filter(_.isInstanceOf[FirstStep])
    if(firstStep.size == 1) firstStep(0) else new ErrorStep("7", Nil, List("exist more as one step"))
  }
  
  /**
   * - addStepToCurrentConfig(selectedComponentIds(0)) vor List vorbereiten
   * - nextStep Id bei der Multichoose Component muss bei allen componentId mit Step Id übereinstimmen 
   * - currentConfig für den Multichoose Komponent erweitern
   */
  def getNextStep(selectedComponents: Set[SelectedComponent]): AbstractStep = {
    
    checkSelectedComponents(selectedComponents) match {
        case errorStep: ErrorStep => errorStep
        case lastStep: LastStep => {
          addStepToCurrentConfig(lastStep, selectedComponents)
          new FinalStep("0")
        }
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
                               getComponent(step, selectedComponents))
        
    val index = container.currentConfig.indexWhere(s => stepForCurrentConfig.id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += stepForCurrentConfig
  }
  //TODO TEST
  private def getComponent(step: AbstractStep, selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    
    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
    
    var immutableComponents = step.components filter (_.isInstanceOf[ImmutableComponent])
    
    val currentConfigMutableCommponents = for{
      mutableComponent <- mutableComponents
      selectedComponent <- selectedComponents
      if mutableComponent.id == selectedComponent.id
    }yield new CurrentConfigMutableComponent(  mutableComponent.id, 
                                               mutableComponent.nameToShow, 
                                               selectedComponent.value)
    
     val currentConfigImmutableCommponents = for{
      immutableComponent <- immutableComponents
      selectedComponent <- selectedComponents
      if immutableComponent.id == selectedComponent.id
    }yield new CurrentConfigImmutableComponent(  immutableComponent.id, 
                                               immutableComponent.nameToShow)
    
    currentConfigImmutableCommponents ++ currentConfigMutableCommponents
  }
  
  //TODO TEST
  private def checkParameterOfSelectedComponents(step: AbstractStep, 
                  selectedComponents: Set[SelectedComponent]) = {
    
    // mutable components from ConfigSettings
    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
    
    // finde passende id und pruefe das max und min values
    val filteredSelection = for {
      mutableComponent <- mutableComponents
      selection <- selectedComponents
      if mutableComponent.id == selection.id
    } yield checkValue(mutableComponent, selection)
    
    // falls ErrorComponent in der Liste exestiert, Error weiter leiten
    (filteredSelection filter(_.isInstanceOf[ErrorComponent])).size match{
      case 0 => step
      case _ => {
         val errorStep = for {
           selection <- filteredSelection
            if selection.isInstanceOf[ErrorComponent]
         }yield new ErrorStep("7", Nil, List(selection.errorMessage))
         
         //TODO
         errorStep(0)
         
      }
    }
  }
  
  /**
   * pruft max und min Values
   * @return ErrorComponent -> value invalid
   * 				 Component -> value valid
   */
  private def checkValue(mutableComponent: Component, selection: Component) = {
    if(selection.value < mutableComponent.minValue || selection.value > mutableComponent.maxValue){
      new ErrorComponent("7", "minValue is smaller or maxValue is greater as definition in configSttings")
    }else if(mutableComponent.isInstanceOf[ImmutableComponent]){
      new ErrorComponent("7", "ImmutableComponent has not parameter for value")
    }else{
    	selection
    }
  }
  
  private def checkSelectionCriterium(  step: AbstractStep, 
                                selectedComponents: Set[SelectedComponent]): AbstractStep = {
    
    val min = step.selectionCriterium.min.toInt
    val max = step.selectionCriterium.max.toInt
    
    val selectedComponentIds = selectedComponents map (_.id)
    selectedComponentIds.size match {
      case com1: Int if com1 < min => new ErrorStep("7", Nil, List("selected to few components"))
      case com2: Int if com2 > max => new ErrorStep("7", Nil, List("selected to match components"))
      case _ => checkParameterOfSelectedComponents(step, selectedComponents)
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
      new ErrorStep("7", List(new ErrorComponent("","")), List("The selected components has " + 
            "not been found in any configuration steps"))
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
      case false => new ErrorStep("7",Nil, List("nextSteps for selectedComponentIds was not same"))
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
  
//  private def checkSelectedComponents(step: AbstractStep, selectedComponents: Set[SelectedComponent]) = {
//    val errorSelectionCriterum = checkSelectionCriteriumV01(step, selectedComponents)
//    val errorSelectedComponents = checkParameterOfSelectedComponentsV01(step, selectedComponents)
//    List(errorSelectedComponents, errorSelectionCriterum)
//  }
//  
//  def checkSelectionCriteriumV01(step: AbstractStep, selectedComponents: Set[SelectedComponent]) = {
//    val min = step.selectionCriterium.min.toInt
//    val max = step.selectionCriterium.max.toInt
//    
//    val selectedComponentIds = selectedComponents map (_.id)
//    selectedComponentIds.size match {
//      case com1: Int if com1 < min => new ErrorStep("7", Nil, List("selected to few components"))
//      case com2: Int if com2 > max => new ErrorStep("7", Nil, List("selected to match components"))
//      case _ => SuccessStep("3","SelectionCriterium is OK")
//    }
//  }
//  
//  def checkParameterOfSelectedComponentsV01(step: AbstractStep, selectedComponents: Set[SelectedComponent]) = {
//    // mutable components from ConfigSettings
//    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
//    
//    // finde passende id und pruefe das max und min values
//    val filteredSelection = for {
//      mutableComponent <- mutableComponents
//      selection <- selectedComponents
//      if mutableComponent.id == selection.id
//    } yield checkValue(mutableComponent, selection)
//    
//    // falls ErrorComponent in der Liste exestiert, Error weiter leiten
//    (filteredSelection filter(_.isInstanceOf[ErrorComponent])).size match{
//      case 0 => SuccessStep("3", "Selected value is OK")
//      case _ => {
//         val errorStep = for {
//           selection <- filteredSelection
//            if selection.isInstanceOf[ErrorComponent]
//         }yield new ErrorStep("7", Nil, List(selection.errorMessage))
//         
//         //TODO
//         errorStep(0)
//         
//      }
//    }
//  }
  
}