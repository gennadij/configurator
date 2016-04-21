package configMgr

import scala.collection.mutable.ListBuffer

class ConfigMgr {
  //TODO create Factory Object
  val configFile = new ConfigFile

  val fileXML = configFile getXML

  /**
    * Implement partial load from XML file
 *
    * @return
    */
  def loadStepsFromXML : Container = {
    new Container(fileXML \ "step" map(s => configFile.toStep(s)))
  }
  
  def loadCurrentConfig : CurrentConfig = {
    new CurrentConfig(ListBuffer.empty)
  }
  
  def getSelectedComponent(container: Container, id: String) = {
    container.steps flatMap (s => s.components filter (_.id == id))
  }

  def getStepWithSelectedComponent(container: Container, id: String): Seq[Step] = {
    val steps = for {
      step <- container.steps
      component <- step.components
    } yield 
    if(component.id == id) new Step(step.id, step.nameToShow, step.nextStep, step.isStartStep, Seq(component))
    else null
    steps filter { _ != null }
  }
  
  def getNextStep(container: Container, selectedComponentId: String) = {
    //TODO Pruefung der first Step einrichten damit die Funktion getFirstStep entfallen kann
    
    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStepId
    if(nextStepId == "000") "Last Step reach"
    else {
      val nextStep = container.steps filter (_.id == nextStepId)
      if(nextStep.length == 1) nextStep(0) else null
    }
  }

  
  def getFirstStep(container: Container): Step = {
    val firstStep = container.steps filter (s => s.isStartStep == "true")
    //TODO Check for one Element in List
    firstStep(0)
  }
}