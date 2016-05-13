package org.configSettings

import org.configTree.step._
import org.configTree._
import org.container.Container
import org.configTree.component.StaticComponent
import org.configTree.component.Component
import org.configTree.component.ImmutableComponent
import org.configTree.component.MutableComponent

object ConfigSettings {
  
//  val configSettings: Container  = {
//     val configSet = new ConfigSettings
//    new Container(configSet.getXML \ "step" map(s => configSet.toStep(s)))
//  }
  val configSettings: Container = {
    val configSet = new ConfigSettings
    new Container(configSet.getXMLV01 \ "step" map(s => configSet.toStepv01(s)))
  }
}


class ConfigSettings {
  private def getXML = scala.xml.XML.loadFile("src/main/scala/xml_json/config.xml")
  private def getXMLV01 = scala.xml.XML.loadFile("src/main/scala/xml_json/config_v0.1.xml")

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

  private def toNextStep(ns: scala.xml.NodeSeq): NextStep = {
    new NextStep(
      (ns \ "@component").text,
      (ns \ "@step").text)
  }
  private def toSelectionCriterium(sc: scala.xml.NodeSeq): SelectionCriterium = {
    new SelectionCriterium(
      (sc \ "min").text,
      (sc \ "max").text
    )
  }

  private def toSource(s: scala.xml.NodeSeq): Source = {
    new Source(
      (s \ "@source").text,
      (s \ "sqlStatement").text,
      (s \ "path").text
    )
  }
  private def toComponentv01(c: scala.xml.Node): StaticComponent= {
    new StaticComponent(
      (c \ "id").text,
      (c \ "kind").text,
      (c \ "nameToShow").text
    )
  }

  /**
    * kind kann man entfernen in der Klasse AbstractStep2
    * @param step
    * @return
    */
  private def toStepv01(step: scala.xml.Node) = {
    val id = (step \ "id").text
    val nameToShow = (step \ "nameToShow").text
    val nextSteps = (step \ "nextSteps" \ "nextStep") map (ns => toNextStep(ns))
    val kind = (step \ "kind").text
    val selectionCriterium = toSelectionCriterium  (step \ "selectionCriterium")
    val from = toSource(step \"from")
    val components = (step \ "components" \ "component") map (c => toComponentv01(c))

    if(kind == "first") {
      new FirstStep(id, nameToShow, nextSteps, kind, selectionCriterium, from, components)
    }else if(kind == "default"){
      new DefaultStep(id, nameToShow, nextSteps, kind, selectionCriterium, from, components)
    }else{
      new LastStep(id, nameToShow, nextSteps, kind, selectionCriterium, from, components)
    }
  }
}