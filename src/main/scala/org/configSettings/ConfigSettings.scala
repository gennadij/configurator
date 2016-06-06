package org.configSettings

import org.configTree.step._
import org.configTree._
import org.container.Container
import org.configTree.component.Component
import org.configTree.component.ImmutableComponent
import org.configTree.component.MutableComponent

/**
 *
 */

object ConfigSettings {
  val configSettings: Container = {
    val configSet = new ConfigSettings
    new Container(configSet.getXMLV01 \ "step" map(s => configSet.toStep(s)))
  }
}

class ConfigSettings {
  private def getXMLV01 = scala.xml.XML.loadFile("config/config_v0.1.xml")

  private def toNextStep(ns: scala.xml.NodeSeq): NextStep = {
    new NextStep(
        "1",
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
  private def toComponent(c: scala.xml.Node): Component = {
    (c \ "kind").text match {
      case "immutable" => immutableComponent(c)
      case "mutable" => mutableComponent(c)
    }
  }
  
  private def mutableComponent(c: scala.xml.Node) = {
    new MutableComponent(
      (c \ "id").text,
      (c \ "nameToShow").text,
      (c \ "minValue").text.toInt,
			(c \ "maxValue").text.toInt,
			(c \ "defaultValue").text.toInt,
			(c \ "interval").text.toInt,
      List.empty
    )
  }
  
  private def immutableComponent(c: scala.xml.Node) = {
    new ImmutableComponent(
      (c \ "id").text,
      (c \ "nameToShow").text
    )
  }

  /**
   	* 
    * @param step
    * @return
    */
  private def toStep(step: scala.xml.Node) = {
    val id = (step \ "id").text
    val nameToShow = (step \ "nameToShow").text
    val nextSteps = (step \ "nextSteps" \ "nextStep") map (ns => toNextStep(ns))
    val kind = (step \ "kind").text
    val selectionCriterium = toSelectionCriterium  (step \ "selectionCriterium")
    val from = toSource(step \"from")
    val components = (step \ "components" \ "component") map (c => toComponent(c))

    kind match {
      case "first" => new FirstStep(id, nameToShow, nextSteps,
                                    selectionCriterium, from, components)
      case "default" => new DefaultStep(id, nameToShow, nextSteps,
                                        selectionCriterium, from, components)
      case "last" => new LastStep(id, nameToShow, nextSteps, 
                                  selectionCriterium, from, components)
    }
  }
}