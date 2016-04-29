package configMgr

object ConfigSettings {
  
  val configSettings:Container  = {
     val configSet = new ConfigSettings
    new Container(configSet.getXML \ "step" map(s => configSet.toStep(s)))
  }
}


class ConfigSettings {
   private def getXML = scala.xml.XML.loadFile("src/main/scala/xml_json/config.xml")

   private def toStep(stepXML: scala.xml.Node): Step = {
    new Step(
      (stepXML \ "id").text,
      (stepXML \ "nameToShow").text,
      (stepXML \ "nextStep").text,
      (stepXML \ "isStartStep").text,
      (stepXML \ "components" \ "component") map (c => toComponents(c))
    )
  }

  private def toComponents(componentXML: scala.xml.Node): Component = {
    new Component(
      (componentXML \ "id").text,
      (componentXML \ "nameToShow").text,
      (componentXML \ "nextStepId").text
    )
  }
}