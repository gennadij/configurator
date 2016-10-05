package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph

object OrientDB {
  
  
  def setGraph(uri: String){
    
  }
  
  def getGraph(): OrientGraph = {
    val uri: String = "remote:generic-config.dnshome.de/config2"
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
    factory.getTx()
  }
}