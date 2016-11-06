package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal

object OrientDB {
  
  
  def setGraph(uri: String){
    
  }
  
  def getGraph(): OrientGraph = {
    val uri = if(System.getProperty("os.name") == "Linux") 
                "remote:localhost/config1" 
              else 
                "remote:localhost/config_1"
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
    factory.getTx()
  }
}