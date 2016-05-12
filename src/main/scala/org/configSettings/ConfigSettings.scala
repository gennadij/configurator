package org.configSettings

import org.configTree.step.Step
import org.configTree._
import org.container.Container
import org.configTree.component.StaticComponent
import org.configTree.component.Component
import org.configTree.component.ImmutableComponent
import org.configTree.component.MutableComponent
import org.configTree.step.Source
import org.configTree.step.SelectionCriterium
import org.configTree.step.DefaultStep
import org.configTree.step.NextStep

object ConfigSettings {
  
  val configSettings: Container  = {
     val configSet = new ConfigSettings
    new Container(configSet.getXML \ "step" map(s => configSet.toStep(s)))
  }
  val configSettingsV01: Container = {
    val configSet = new ConfigSettings
    val container = new Container(null)
    container.configSettingsForStatic = configSet.getXMLV01 \ "step" map(s => configSet.toStepv01(s))
    container
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

  private def toStepv01(step: scala.xml.Node) = {
    new DefaultStep(
      (step \ "id").text,
      (step \ "nameToShow").text,
      (step \ "nextSteps" \ "nextStep") map (ns => toNextStep(ns)),
      (step \ "kind").text,
      toSelectionCriterium  (step \ "selectionCriterium"),
      toSource(step \"from"),
      (step \ "components" \ "component") map (c => toComponentv01(c))
    )
  }
}