package org.configSettings

import org.configTree.step._
import org.configTree._
import org.container.Container
import org.configTree.component._
import org.admin.Admin
import org.errorHandling.ErrorStrings
import org.client.ConfigClient

/**
 *
 */

object ConfigSettings {
  
  def firstStep(client: org.client.ConfigClient) = {
    new ConfigSettings().firstStep(client)
  }
  
  def stepOfComponents(client: org.client.ConfigClient, 
      selectedComponents: Set[SelectedComponent]): Step = {
    new ConfigSettings().stepOfComponents(client, selectedComponents)
  }
  
//  def configSettings: Container = {
//    val configSet = new ConfigSettings
//    new Container(configSet.getXML \ "step" map(s => configSet.toStep(s)))
//  }
  
  def configSettings(client: org.client.ConfigClient): Seq[ConfigSettingsStep] = {
    new ConfigSettings().configSettings(client)
  }
}

class ConfigSettings {

  private def getXML = scala.xml.XML.loadFile("config/config_v0.1.xml")
  
  private def firstStep(client: org.client.ConfigClient) = {
    
    val xml = scala.xml.XML.loadFile("config/" + client.configFile)
    
    
    val config = xml \ "step" map(s => toStep(s))
    
    val firstStep = config filter(_.isInstanceOf[FirstStep])
    if(firstStep.size == 1) firstStep.head else new ErrorStep("7", ErrorStrings.existigOfMoreFirstStep, Nil)
  }
  
  private def stepOfComponents(client: org.client.ConfigClient, 
      selectedComponents: Set[SelectedComponent]) = {
    //lese komplette Konfiguration aus Config file
    val xml = scala.xml.XML.loadFile("config/" + client.configFile)
    
    val config = xml \ "step" map(s => toStep(s))
    
    val steps = for{
      step <- config
    }yield {
      //hole stepId aus allen Steps in der Config
      val ids = step.components map (_.id)
       //hole Id aus selectedComponents
      val selectedComponentIds = selectedComponents map (_.id)
      if(ids.exists { selectedComponentIds.contains _ }){
        step
      }else{
        null
      }
    }
    val filterdSteps = steps filter (_ != null)
    
    if(filterdSteps.size == 1){
      filterdSteps(0)
    }else{
      new ErrorStep("7", ErrorStrings.notFoundSteps, Nil)
    }
  }
  
  private def configSettings(client: org.client.ConfigClient): Seq[ConfigSettingsStep] = {
    val xml = scala.xml.XML.loadFile("config/" + client.configFile)
    xml \ "step" map(s => toStep(s))
  }
  
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
    val fatherStep = (step \ "fatherStep").text
    val dependencies = (step \ "dependencies").text
    val nextSteps = (step \ "nextSteps" \ "nextStep") map (ns => toNextStep(ns))
    val kind = (step \ "kind").text
    val selectionCriterium = toSelectionCriterium  (step \ "selectionCriterium")
    val from = toSource(step \"from")
    val components = (step \ "components" \ "component") map (c => toComponent(c))

    kind match {
      case "first" => new FirstStep(id, nameToShow, fatherStep, nextSteps,
                                    selectionCriterium, from, components)
      case "default" => new DefaultStep(id, nameToShow, fatherStep, nextSteps,
                                        selectionCriterium, from, components)
      case "last" => new LastStep(id, nameToShow, fatherStep, nextSteps, 
                                  selectionCriterium, from, components)
    }
  }
}