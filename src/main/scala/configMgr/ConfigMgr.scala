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

  
  def getNextStep(container: Container, selectedComponentId: String) = {
    
    val selectedComponent: Seq[Component] = container.steps flatMap (
      s => s.components filter (_.id == selectedComponentId)
    )
    val nextStepId = if (selectedComponent.length == 1) selectedComponent(0).nextStepId
    val nextStep = container.steps filter (_.id == nextStepId)
    if(nextStep.length == 1) nextStep(0) else null
  }

  
  def getFirstStep(container: Container): Step = {
    val firstStep = container.steps filter (s => s.isStartStep == "true")
    //TODO Check for one Element in List
    firstStep(0)
  }
}