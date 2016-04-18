package configMgr

class ConfigMgr {
  //TODO create Factory Object
  val configFile = new ConfigFile

  val fileXML = configFile getXML

  def getSteps : Seq[Step] = {
    fileXML \ "step" map(s => configFile.toStep(s))
  }

  def getComponentsForStep(stepId: String, steps: scala.xml.NodeSeq = fileXML \ "step"): Seq[Component] = {

    val step = steps filter(s => (s \ "id").text == stepId)

    step \ "components" \ "component" map(c => configFile.toComponents(c))
  }
  
  def init = ???
  
  def getFirstStep = {
    val firstStep = getSteps filter (s => s.isStartStep == "true")
    //TODO Check for one Element in List
    firstStep(0)
  }
}