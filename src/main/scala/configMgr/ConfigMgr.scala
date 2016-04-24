package configMgr

import scala.collection.mutable.ListBuffer

/**
 * TODO
 * 
 */
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
    
    val selectedComponent: Seq[Component] = getSelectedComponent(container, selectedComponentId)
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStepId
    val nextStep = container.steps filter (_.id == nextStepId)
    val currentStep = getStepWithSelectedComponent(container, selectedComponentId)
    if(nextStepId == "000") {
      (null, currentStep(0))
    }
    else {
      (nextStep(0), currentStep(0))
    }
  }

  
  def getFirstStep(container: Container): Step = {
    val firstStep = container.steps filter (s => s.isStartStep == "true")
    //TODO Check for one Element in List
    firstStep(0)
  }
  
  
  def valideSteps(container: Container): Boolean = {
    
    val isStartStep = container.steps filter(_.isStartStep == true)
    
    if(isStartStep.length == 1) true else false
  }
}