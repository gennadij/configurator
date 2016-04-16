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
}