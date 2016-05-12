package org.configMgr

import org.configTree.component.Component
import org.configTree.staticStep.Step
import org.container.Container

import scala.collection.mutable.ListBuffer

/**
 * TODO
  * - first Stepp ist immer erste hinzugefügte Step zu dem Config Setting
  * - Configuration zu der CurrentCOnfig sofort bei der Auruf von nextStep hinzufügen.
  * - create Factory Object
 * 
 */
class ConfigMgr {

  /**
    * TODO check if more component find, or multichose component selected
    * @param container
    * @return
    */
  def startConfig(container: Container): Step = {
    (container.configSettings filter(_.id == "001"))(0)
  }

  def getSelectedComponent(container: Container, id: String) = {
    container.configSettings flatMap (s => s.components filter (_.id == id))
  }

  /**
    * erzeugt eine Kopie des Stepes mir parametrisierten Commponent ID
    *
    *
    * @param container
    * @param selectedComponentId
    * @return Step -> Simple choice, null -> multiple choice
    */
  private def getStepWithSelectedComponent(container: Container, selectedComponentId: String): Step = {
    val steps = for {
      step <- container.configSettings
      component <- step.components
    } yield
    if(component.id == selectedComponentId) {
      new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
    } else null
    val filteredSteps = steps filter { _ != null }
    if(filteredSteps.size < 1){
      null
    }else {
      filteredSteps(0)
    }
  }
  
  def getCurrentStep(container: Container):Step = {
    container.currentConfig.last
  }


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

  def addStepToCurrentConfig(container: Container, selectedComponentId: String) = {

    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)

    val indexStep = for {
      step <- container.currentConfig if (currentStep.id == step.id)
    } yield step

    val index = container.currentConfig.indexWhere(s => currentStep.id == s.id)

    val currentConfigSize = container.currentConfig.size

    if(index != -1) container.currentConfig.remove(index, currentConfigSize - index)

    container.currentConfig += currentStep
  }


  /**
    *
    * @param container
    * @param selectedComponentId
    * @return
    */

  def  getNextStep(container: Container, selectedComponentId: String) = {

    //testen jeden Step ob er letzte step ist, wenn einen Letzten Step gefunden einen objekte LastStep zurückgeben

    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStep
    val nextStep = container.configSettings filter (_.id == nextStepId)
    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)

    if(nextStepId == "000") {
      (null, currentStep)
    }
    else {
      (nextStep(0), currentStep)
    }
  }
  
  
  def valideSteps(container: Container): Boolean = {
    
    val isStartStep = container.configSettings filter(_.isStartStep == true)
    
    if(isStartStep.length == 1) true else false
  }
}