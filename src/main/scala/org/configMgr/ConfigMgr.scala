package org.configMgr

import org.configTree.component._
import org.container.Container
import scala.collection.mutable.ListBuffer
import org.container.Container
import org.container.Container
import org.configSettings.ConfigSettings
import org.configTree.step.Step
import org.configTree.step.DefaultStep
import org.configTree.step.FinalStep
import org.configTree.step.FirstStep
import org.configTree.step.ErrorStep
import org.configTree.step.LastStep
import org.configTree.step.NextStep
import org.configTree.step.SuccessStep
import org.configTree.step.CurrentConfigStep
import org.configTree.step.ConfigSettingsStep
import org.configTree.step.AnnounceStep

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
 * - weitere Parameter bei der MutableComponent prüfen und testen
 */

object ConfigMgr{
  val configMgr = new ConfigMgr

  def currentConfig = {
    configMgr.container.currentConfig
  }
  
  def startConfig: Step = {
    configMgr.startConfig
  }
  
  def getNextStep(selectedComponentIds: Set[SelectedComponent]): Step = {
    configMgr.getNextStep(selectedComponentIds)
  }
}

class ConfigMgr {
  
  val container = ConfigSettings.configSettings
  
//  def addError(error: ConfigTree) = {
//    
//    error match {
//      case errorComponent: ErrorComponent => Nil
//      case errorStep: ErrorStep => Nil
//    }
//  }

  /**
    * - durch den Typunterscheidung mit match herausfiltern
    * - ErrorStep als Fehler difenieren
    * @return
    */
  def startConfig = {
    val firstStep = container.configSettings filter(_.isInstanceOf[FirstStep])
    if(firstStep.size == 1) firstStep(0) else new ErrorStep("7", "exist more as one step", Nil)
  }
  
  /**
   * - addStepToCurrentConfig(selectedComponentIds(0)) vor List vorbereiten
   * - nextStep Id bei der Multichoose Component muss bei allen componentId mit Step Id übereinstimmen 
   * - currentConfig für den Multichoose Komponent erweitern
   */
  def getNextStep(selectedComponents: Set[SelectedComponent]): Step = {
    
    val step: Step = getStepOfComponents(selectedComponents)
    
    val selectCrit: AnnounceStep = checkSelectionCriterium(step, selectedComponents)
    
    val selectValue: AnnounceStep = checkParameterOfSelectedComponents(step, selectedComponents)
    
    if(selectCrit.isInstanceOf[ErrorStep]){
      selectCrit
    }else if (selectValue.isInstanceOf[ErrorStep]){
      selectValue
    }else{
      step match {
        case errorStep: ErrorStep => errorStep
        case lastStep: LastStep => {
          addStepToCurrentConfig(lastStep, selectedComponents)
          new FinalStep("0")
        }
        case step: DefaultStep => {
          addStepToCurrentConfig(step, selectedComponents)
          checkNextSteps(step, selectedComponents)
        }
        case step: FirstStep => {
          addStepToCurrentConfig(step, selectedComponents)
          checkNextSteps(step, selectedComponents)
        }
      }
    }
  }
  
  /**
   * neue step für CurrentConfig
   */
  def addStepToCurrentConfig(step: ConfigSettingsStep, selectedComponents: Set[SelectedComponent]) = {
    
    val stepForCurrentConfig: CurrentConfigStep = new CurrentConfigStep(step.id, step.nameToShow, 
                               getComponent(step, selectedComponents))
        
    val index = container.currentConfig.indexWhere(s => stepForCurrentConfig.id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += stepForCurrentConfig
  }
  //TODO TEST
  private def getComponent(step: ConfigSettingsStep, selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    
    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
    //TODO warum rot
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

  
  private def checkParameterOfSelectedComponents(step: Step, 
                  selectedComponents: Set[SelectedComponent]): AnnounceStep = {
    
    
    if(step.isInstanceOf[ErrorStep]){
      new ErrorStep("7", step.errorMessage, step.errorComponent)
    }else{
      
      val checkedMutableComponents: List[Component] = checkMutableComponents(step, selectedComponents).toList
      
      val checkedImmutableComponents: List[Component] = checkImmutableComponents(step, selectedComponents).toList
      
      val checkedComponents = checkedImmutableComponents ::: checkedMutableComponents
      
      val errorComponents = checkedComponents filter { _.isInstanceOf[ErrorComponent] }
      
//      falls ErrorComponent in der Liste exestiert, Error weiter leiten
      errorComponents.size match{
        case 0 => SuccessStep("3","")
        case _ => new ErrorStep("7", "error", errorComponents)
      }
      
      
//      // mutable components from ConfigSettings
//      val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
//      
//      // finde passende id und pruefe das max und min values
//      val filteredSelection = for {
//        mutableComponent <- mutableComponents
//        selection <- selectedComponents
//        if mutableComponent.id == selection.id
//      } yield checkValue(mutableComponent, selection)
      
      // falls ErrorComponent in der Liste exestiert, Error weiter leiten
//      (filteredSelection filter(_.isInstanceOf[ErrorComponent])).size match{
//        case 0 => SuccessStep("3","")
//        case _ => new ErrorStep("7", "error", filteredSelection)
//      }
    }
  }
  
  private def checkMutableComponents(step: Step, 
                  selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    // mutable components from ConfigSettings
    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
    
    // finde passende id und pruefe das max und min values
    for {
      mutableComponent <- mutableComponents
      selection <- selectedComponents
      if mutableComponent.id == selection.id
    } yield checkValue(mutableComponent, selection)
  }
  
  
  
  
  private def checkImmutableComponents(step: Step, 
      selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    
    val immutableComponents = step.components filter (_.isInstanceOf[ImmutableComponent])
    
    // finde passende id und pruefe das max und min values
    for {
      immutableComponent <- immutableComponents
      selection <- selectedComponents
      if immutableComponent.id == selection.id
    } yield checkParameterPresence(immutableComponent, selection)
  }
  
  private def checkParameterPresence(immutableComponent: Component, selection:SelectedComponent) = {
    if(selection.value != 0) new ErrorComponent("7","ImmutableComponent allowed not parameter for value")
    else new SuccessComponent("3")
  }
  
  
  /**
   * pruft max und min Values
   * @return ErrorComponent -> value invalid
   * 				 Component -> value valid
   */
  private def checkValue(mutableComponent: Component, selection: Component) = {
    if(selection.value < mutableComponent.minValue || selection.value > mutableComponent.maxValue){
      new ErrorComponent("7", "minValue is smaller or maxValue is greater as definition in configSttings")
    }else{
    	new SuccessComponent("3")
    }
  }

  private def checkSelectionCriterium(step: Step, 
                                selectedComponents: Set[SelectedComponent]): AnnounceStep = {
    if(step.isInstanceOf[ErrorStep]){
      new ErrorStep("7", step.errorMessage, step.errorComponent)
    }else{
      val min = step.selectionCriterium.min.toInt
      val max = step.selectionCriterium.max.toInt
    
      val selectedComponentIds = selectedComponents map (_.id)
      selectedComponentIds.size match {
        case com1: Int if com1 < min => new ErrorStep("7", "selected to few components", Nil)
        case com2: Int if com2 > max => new ErrorStep("7", "selected to match components", Nil)
        case _ => new SuccessStep("3","")
      }
    }
  }
  
  /**
   * sucht den Step in Config Settings mit Hilfe der IDs des Components
   * 
   * @return Step mit dem Components
   */
  
  private def getStepOfComponents(selectedComponents: Set[SelectedComponent]): Step = {
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
      new ErrorStep("7", "The selected components has " + 
            "not been found in any configuration steps", Nil)
    }
  }
  
  /**
   * Vergleicht in currentStep.nextStep die difenierten componentIds mit 
   * selectedComponentIds
   * 
   * Bei der Multichoose werden die difenierte next Steps verglichen. Es darf nicht bei 
   * der verschiedenen selectedComponentsIds verscheidene nextSteps difeniert werden
   */
  private def checkNextSteps(step: Step, selectedComponents: Set[SelectedComponent]): Step = {
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
      case false => new ErrorStep("7", "nextSteps for selectedComponentIds was not same", Nil)
    }
  }
  
  
  private def checkElem(list: Seq[String]) = {
    list match {
      case x :: rest => rest forall (_ == x)
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