package org.persistence


import org.configSettings.ConfigSetting
import org.status.SuccessfulStatus
import org.configTree.step.Step
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable

import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL

object Persistence {
  
  def config() = ???
  
  def rules() = ???
  
  def setStep(user: String, isConnected: Boolean, step: Step) = {
    
    val uri: String = "remote:localhost/" + user
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
    val graph: OrientGraph = factory.getTx()
    
    println(graph.getVertexType("Person"))
    val res: OrientDynaElementIterable = graph.command(new OCommandSQL(s"SELECT FROM Person WHERE firstName='Gennadi'")).execute()
    
    res.foreach(v => {println(v)})
    
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