package org.persistence


import org.configSettings.ConfigSetting
import org.status.SuccessfulStatus
import org.configTree.step.Step
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable

import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL
import org.persistence.db.orientdb.VertexStep

object Persistence {
  
  def config() = ???
  
  def rules() = ???
  
  def setStep(user: String, isConnected: Boolean, step: Step) = {
    
    val uri: String = "remote:localhost/" + user
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
    val graph: OrientGraph = factory.getTx()
    
    val vStep = new VertexStep(
        step.id, step.nameToShow, step.fatherStep, step.nextStep,
        step.components, step.dependencies)
    
    vStep.persistStep(graph)
    
    new SuccessfulStatus("")
  }
  
  
  def getPersisitence = {
    val configSetting = scala.xml.XML.loadFile("persistence/config_settings.xml")
    
    val kindOfpersisitenceForConfigTree = configSetting \ "persistence" \ "persistenceKind" \ "forConfigTree"
    val kindOfpersisitenceForRule = configSetting \ "persistence" \ "persistenceKind" \ "forRule"
    
    val configTree = configSetting \ "persistence" \ "xml" \ "config"
    val rule = configSetting \ "persistence" \ "xml" \ "rule"
    
    new ConfigSetting("", configTree.text.toString(), rule.text.toString(), "presentation")
  }
}


class Persistence {
  
}