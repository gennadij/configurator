package org.persistence

import org.configSettings.ConfigSetting

object Persistence {
  
  def config() = ???
  
  def rules() = ???
  
  def getPersisitence = {
    val configSetting = scala.xml.XML.loadFile("persistence/config_settings.xml")
    
    val kindOfpersisitenceForConfigTree = configSetting \ "persistence" \ "persistenceKind" \ "forConfigTree"
    val kindOfpersisitenceForRule = configSetting \ "persistence" \ "persistenceKind" \ "forRule"
    
    val configTree = configSetting \ "persistence" \ "xml" \ "config"
    val rule = configSetting \ "persistence" \ "xml" \ "rule"
    
    new ConfigSetting("", configTree.text.toString(), rule.text.toString(), "presentation")
  }
  
}