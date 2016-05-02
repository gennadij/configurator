package org.configSettings

import org.configTree.{ImmutableComponent, MutableComponent, Step, Component}
import org.container.Container

object ConfigSettings {
  
  val configSettings: Container  = {
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
  //TODO spec for correct separation between Innutable and mutable Componnents
  private def toComponents(componentXML: scala.xml.Node): Component = {
    if((componentXML \ "mutable").text.toBoolean == false){
      new ImmutableComponent(
        (componentXML \ "id").text,
        (componentXML \ "nameToShow").text,
        (componentXML \ "nextStepId").text
      )
    }else{
      new MutableComponent("0", "0", "0", "0")
    }
  }
}