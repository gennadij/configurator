package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
import org.persistence.GlobalConfigForDB

object OrientDB {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param
   * 
   * @return
   */
  def getGraph(): OrientGraph = {
    
    val uri = "remote:localhost/" + GlobalConfigForDB.db.dbName
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, GlobalConfigForDB.db.username, GlobalConfigForDB.db.password)
    factory.getTx()
  }
}

case class DB (
    kind: String,
    dbName: String,
    username: String,
    password: String
)