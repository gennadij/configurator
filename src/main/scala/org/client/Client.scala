package org.client

object Client {

  def setClient(client: String) = {
    
    val xmlClientFiles = (new java.io.File("xmlClients")).listFiles()
    
    val clientSeparated = "client_" + client.split("/").last + ".xml"
    
    val xmlClientFile = xmlClientFiles filter { _.getName.equals(clientSeparated) }
    
    if(xmlClientFile.size == 1){
      val xmlClient = scala.xml.XML.loadFile(xmlClientFile.head)
      
      new org.client.ConfigClient((xmlClient \ "id").text, (xmlClient \ "configFile").text)
    }else{
      //TODO error 
      //es exestiert mehr als ein Client mit dieser fileName
      null
    }
  }
}