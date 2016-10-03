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
    
//    val uri: String = "remote:localhost/" + user
    val uri: String = "remote:generic-config.dnshome.de/" + user
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
    val graph: OrientGraph = factory.getTx()
    
    val vStep = new VertexStep(
        step.id, step.nameToShow, step.fatherStep, step.nextStep,
        step.components, step.dependencies)
    
    /**
     * 1. Erstelle die Schema 
     * 		Vertex -> Step, Component 
     * 		Edge -> hasComponent, nextStep
     * 
     * 
     * 2. befühle die Classes mit objects
     * 
     * 4. update objects
     * 
     * 5. Step besteht aus der 
     * 		1. HauptStep und deren Components
     * 		2. Components mit deren NextSteps
     * 
     * 6. NextStep werden bei der erzeugzng von der HauptStep erzeugt
     * 
     * 7. Zuerst wird Config erstellt. Der Config bildet nur das Ablauf der Konfiguration ab.
     * 
     * 8. Wenn config erstellt wurde, nachher werden die Regeln hinzugefuegt in der config Graph
     * 
     * 9. Die Rules bestehen aus:
     * 		Edge -> dependency
     */
    
    
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