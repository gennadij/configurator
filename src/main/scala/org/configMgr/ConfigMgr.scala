package org.configMgr

import scala.collection.mutable.ListBuffer

import org.configTree.component._
import org.container.Container
import org.configSettings.ConfigSettings
import org.configTree.step._
import org.errorHandling.ErrorStrings
import org.currentConfig.CurrentConfig
import org.dependency.Dependency

/**
 *  Managment of whole general Configurator
 */

object ConfigMgr{
  val configMgr = new ConfigMgr
  
//  def startConfig(client: org.client.ConfigClient): Step = {
//    configMgr.startConfig(client)
//  }
  
//  def getNextStep(client: org.client.ConfigClient, selectedComponentIds: Set[SelectedComponent]): Step = {
//    
//    
//    configMgr.getNextStep(client, selectedComponentIds)
//  }
}

class ConfigMgr extends Dependency{
  
//  val container: Container = ConfigSettings.configSettings

  /**
    * gibt den ersten Schritt in der Konfiguration
    * 
    * @return FirstStep
    * @error ErrorStep
    */
  
//  def startConfig(client: org.client.ConfigClient): Step = {
//    if(true){
//      ConfigSettings.firstStep(client)
//    }else{
//      null
//      
//    }
//    
//  }
  
  /**
   * - addStepToCurrentConfig(selectedComponentIds(0)) vor List vorbereiten
   * - nextStep Id bei der Multichoose Component muss bei allen componentId mit Step Id übereinstimmen 
   * - currentConfig für den Multichoose Komponent erweitern
   */
  def getNextStep(client: org.client.ConfigClient, selectedComponents: Set[SelectedComponent]): Step = {
    
    val step: Step = ConfigSettings.stepOfComponents(client, selectedComponents)
    
    val dependency: AnnounceStep = checkDependency(client, step)
      
    val selectCrit: AnnounceStep = checkSelectionCriterium(step, selectedComponents)
    
    val selectValue: AnnounceStep = checkParameterOfSelectedComponents(step, selectedComponents)
    // TODO Step muss zuerst hinzugefügt werden und danach geprüft
//    val checkCurrentConfig: AnnounceStep = checkStepsFromCurrentConfig(client, step, selectedComponents)
    
//    val checkCurrentConfig: AnnounceStep = addStepToCurrentConfig(client, step, selectedComponents)
    
    if(selectCrit.isInstanceOf[ErrorStep]){
      selectCrit
    }else if (selectValue.isInstanceOf[ErrorStep]){
      selectValue
    }
//      else if (checkCurrentConfig.isInstanceOf[ErrorStep]){
//      checkCurrentConfig
//    }
    else{
      step match {
        case errorStep: ErrorStep => errorStep
        case lastStep: LastStep => {
          addStepToCurrentConfig(client, lastStep, selectedComponents)
          new FinalStep("FS000000")
        }
        case step: DefaultStep => {
          addStepToCurrentConfig(client, step, selectedComponents)
          checkNextSteps(client, step, selectedComponents)
        }
        case step: FirstStep => {
          addStepToCurrentConfig(client, step, selectedComponents)
          checkNextSteps(client, step, selectedComponents)
        }
      }
    }
  }
  
  /**
   * Wenn einen Step ausgewält wird der nicht in der CurrentConfig existiert
   */
  
  def checkStepsFromCurrentConfig(client: org.client.ConfigClient ,
      step: Step, selectedComponents: Set[SelectedComponent]): AnnounceStep = {
    
    /**
     * Gehe zu der FatherStep und holle alle Component -> NextStep
     * holle selectedComponent bei der FatherComponent in der CurrentConfig
     *          wenn FatherStep nicht in der CurrentConfig zu finden ist
     *                dann konnte nicht in der Konfiguration dieser Komponent ausgewählt weden (client Fehler)
     *          wenn FatherStep in der CurrentConfig zu finden ist 
     *                dann holle selectedComponents aus der CurrentConfig und 
     *                vergleiche Component -> NextStep in der Config mit der SelectedComponent in der CurrentConfig
     *                gleche Componets sollen auf der Step zeigen
     *                     
     *                     
     *                     
     *                     
     * 1. gehe zu FatherStep in dem Config und holle alle Component und dazugehärige Steps in dem Object NextStep
     * 
     * 2. holle alle selectedComponents in dem CurrentConfig bei der FatherStep
     * 
     * ***wenn*** FatherStep nich in der CurrentConfig zu finden ist.
             ***dann*** suche recursiv bis ein FatherStep 
                  in der CurrentConfig zu finden ist
                  Dem Client wird dieser FatherStep angeboten 
                  um die Konfiguration aus der gültiger Step weitermachen
                  oder Fehler an der Client senden da der SelectedComponent 
                  duerfte nicht ausgewaelt weden.
       ***wenn*** FatherStep beim erstem Durchlauf zu finden ist
                ***dann*** suche in dem Config selectedComponent und deren NextStep.
                ***wenn*** gefundene NextStepId != stepId des selectedComponents
                	***dann*** Fehler an der Client senden da der SelectedComponent 
                              duerfte nicht ausgewaelt weden.
     		***wenn*** FatherStep eine MultiChooseSelection erlaubt
     		***dann*** kann die Konfiguration in zwei verschiedene Richtungen laufen. 
     * 
     * 
     */
    
    val fatherStepFromConfig: Seq[ConfigSettingsStep] = ConfigSettings.configSettings(client) filter (_.id == step.fatherStep)
    
    val nextStepsFromFatherStepFromConfig = if(fatherStepFromConfig.size == 1) fatherStepFromConfig(0).nextStep else new ErrorStep("7", "", Nil)
    
    val fatherStepFromCurrentConfig = client.currentConfig.last filter(_.id == fatherStepFromConfig)
    
    if(nextStepsFromFatherStepFromConfig.isInstanceOf[ErrorStep]){
      //Fehler kommt von Client, es ist nicht möglich ein nicht exestierende 
      // Komponent auszuwählen
      new ErrorStep("7", "Fehler kommt von Client, es ist nicht möglich ein nicht exestierende", Nil)
    }else{
      
      
    }
    
    if(client.currentConfig.size == 0){
      // es exestiert noch keinen Step in der Konfiguration
      
      //TODO bessere Loesung ueberlegen
       new SuccessStep("3", "success step")
      
    }else{
      val currentConfigStepIds: List[String] = client.currentConfig.last map (_.id)
    
      val configStepId: String = step.id
    
      val existStepInCurrentConfig: Boolean = currentConfigStepIds exists { _ == configStepId }
    
      if (existStepInCurrentConfig) 
        new SuccessStep("3", "success step") 
      else 
        ErrorStep("7", ErrorStrings.selectedComponentNotExistInCurrentConfig, Nil)
    }
  }
  
  
  /**
   * TODO
   * neue step für CurrentConfig
   */
  def addStepToCurrentConfig(client: ConfigClient, step: Step, 
      selectedComponents: Set[SelectedComponent]) = {
    
//    if(step.isInstanceOf[ErrorStep]){
//      new ErrorStep("7", step.errorMessage, step.errorComponent)
//    }else{
      val stepForCurrentConfig: CurrentConfigStep = new CurrentConfigStep(step.id, step.nameToShow, 
                                 getComponent(step, selectedComponents))
      
      val currentConfig: List[CurrentConfigStep] = if (client.currentConfig.size != 0) client.currentConfig.last else Nil
      
      val index: Int = currentConfig.indexWhere(s => stepForCurrentConfig.id == s.id)
      
      val currentConfigSize: Int = currentConfig.size
      
      val currentConfiguration: List[CurrentConfigStep] = 
        if(index != -1) currentConfig.dropRight(currentConfigSize - index) 
        else currentConfig
      
      val endcurrentConfig = currentConfiguration.:+(stepForCurrentConfig)
      
      //TODO das Problem noch mal ueberlegen
      client.currentConfig += endcurrentConfig
      
//      checkStepsFromCurrentConfig(client, step, selectedComponents) match {
//          case successStep: SuccessStep => {
//            new SuccessStep("3","Im CurrentConfig ist keinen Fehler aufgetretten")
//          }
//          case errorStep: ErrorStep => errorStep
//      }
//    }
  }
  
  //TODO TEST
  private def getComponent(step: Step, selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    
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

  
  private def checkParameterOfSelectedComponents(step: Step, 
                  selectedComponents: Set[SelectedComponent]): AnnounceStep = {
    
    if(step.isInstanceOf[ErrorStep]){
      new ErrorStep("7", step.errorMessage, step.errorComponent)
    }else{
      
      val checkedMutableComponents: List[Component] = checkMutableComponents(step, selectedComponents).toList
      
      //TODO DIe ImmutableComponents braucht man nicht zu testen die gesetzten Values in 
      // der selectedComponent werden einfach ignoriert.
      
      val checkedImmutableComponents: List[Component] = checkImmutableComponents(step, selectedComponents).toList
      
      val checkedComponents = checkedImmutableComponents ::: checkedMutableComponents
      
      val errorComponents = checkedComponents filter { _.isInstanceOf[ErrorComponent] }
      
//      falls ErrorComponent in der Liste exestiert, Error weiter leiten
      errorComponents.size match{
        case 0 => SuccessStep("3","")
        case _ => new ErrorStep("7", ErrorStrings.fromErrorComponent, errorComponents)
      }
    }
  }
  
  private def checkMutableComponents(step: Step, 
                  selectedComponents: Set[SelectedComponent]): Seq[Component] = {
    // mutable components from ConfigSettings
    val mutableComponents = step.components filter (_.isInstanceOf[MutableComponent])
    
    // finde passende id
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
  
  private def checkParameterPresence(immutableComponent: Component, selection: SelectedComponent) = {
//    if(selection.value != 0) new ErrorComponent("7", ErrorStrings.valueForImmutableComponent)
//    else new SuccessComponent("3")
    new SuccessComponent("3")
  }
  
  
  /**
   * pruft max und min Values
   * @return ErrorComponent -> value invalid
   * 				 Component -> value valid
   */
  private def checkValue(mutableComponent: Component, selection: Component) = {
    if(selection.value < mutableComponent.minValue || selection.value > mutableComponent.maxValue){
      new ErrorComponent("7", ErrorStrings.minMaxValue)
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
        case com1: Int if com1 < min => new ErrorStep("7", ErrorStrings.selectionFewComponents, Nil)
        case com2: Int if com2 > max => new ErrorStep("7", ErrorStrings.selectionMatchComponents, Nil)
        case _ => new SuccessStep("3","")
      }
    }
  }
  
  /**
   * sucht den Step in Config Settings mit Hilfe der IDs des Components
   * 
   * @return Step mit dem Components
   */
  
//  private def getStepOfComponents(selectedComponents: Set[SelectedComponent]): Step = {
//    val steps = for{
//      step <- container.configSettings
//    }yield {
//      val ids = step.components map (_.id)
//      val selectedComponentIds = selectedComponents map (_.id)
//      if(ids.exists { selectedComponentIds.contains _ }){
//        step
//      }else{
//        null
//      }
//    }
//    val filterdSteps = steps filter (_ != null)
//    
//    if(filterdSteps.size == 1){
//      filterdSteps(0)
//    }else{
//      new ErrorStep("7", ErrorStrings.notFoundSteps, Nil)
//    }
//  }
  
  /**
   * Vergleicht in currentStep.nextStep die difenierten componentIds mit 
   * selectedComponentIds
   * 
   * Bei der Multichoose werden die difenierte next Steps verglichen. Es darf nicht bei 
   * der verschiedenen selectedComponentsIds verscheidene nextSteps difeniert werden
   */
  private def checkNextSteps(client: org.client.ConfigClient, step: Step, selectedComponents: Set[SelectedComponent]): Step = {
    val selectedComponentIds = selectedComponents map (_.id)
    val nextStep: Seq[NextStep] = for{
      nextStep <- step.nextStep
      selCom <- selectedComponentIds
    }yield if (nextStep.byComponent == selCom) nextStep else null
    
    checkElem(nextStep filter (_.isInstanceOf[NextStep]) map (_.step)) match {
      case true => {
        val nextStep = step.nextStep filter(_.byComponent == selectedComponentIds.head)
        (ConfigSettings.configSettings(client) filter (_.id == nextStep(0).step))(0)
      }
      case false => new ErrorStep("7", ErrorStrings.notFoundNextStep, Nil)
    }
  }
  
  
  private def checkElem(list: Seq[String]) = {
    list match {
      case x :: rest => rest forall (_ == x)
    }
  }
}