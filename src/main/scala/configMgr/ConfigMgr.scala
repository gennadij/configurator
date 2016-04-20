package configMgr

class ConfigMgr {
  //TODO create Factory Object
  val configFile = new ConfigFile

  val fileXML = configFile getXML

//  def getSteps : Seq[Step] = {
//    fileXML \ "step" map(s => configFile.toStep(s))
//  }

  /**
    * Implement partial load from XML file
    * @return
    */
  def loadStepsFromXML : Container = {
    new Container(fileXML \ "step" map(s => configFile.toStep(s)))
  }
  
  def loadCurentConfig : CurentConfig = {
    new CurentConfig(List.empty)
  }
  
  def getComponent(step: Step, id:String): Boolean = {
    val component = step.components filter (_.id == id)
//    val nextStepId = component(0).nextStepId
    if(component.length >= 1)
      
      true
    else
      false
  }
  
  def getNextStep(container: Container, selectedComponentId: String) = {
    
    val step = container.steps filter (getComponent(_, selectedComponentId))
//    val selectedComponent = step.components filter(_.id == selectedComponentId)
    val component = step(0).components filter(_.id == selectedComponentId)
    val nextStepId = component(0).nextStepId
    container.steps filter (_.id == nextStepId)
  }

  def getComponentsForStep(step: Step): Seq[Component] = {

//    val step = steps filter(s => (s \ "id").text == stepId)
    
//    step \ "components" \ "component" map(c => configFile.toComponents(c))
    step.components
  }

  
  def getFirstStep(container: Container): Step = {
    val firstStep = container.steps filter (s => s.isStartStep == "true")
    //TODO Check for one Element in List
    firstStep(0)
  }
}