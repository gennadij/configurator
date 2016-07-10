package org.admin

class ConfigClient (id: String, configPath: String, configXML: String) {
  
  
  def toXML = {
    <client>
      <id>{id}</id>
      <configPath>{configPath}</configPath>
      <configFile>{configXML}</configFile>
		</client>
  }
}