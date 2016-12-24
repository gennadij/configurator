package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal

object OrientDB {
  
  
  def getGraph(): OrientGraph = {
    
    val xmlDBConfig = scala.xml.XML.loadFile("global_config/globalConfig.xml")
    
    val activeDB: String = (xmlDBConfig \"activeDB" \ "dbName").text
    val dbs : scala.xml.NodeSeq = (xmlDBConfig \ "db")
    
    val db: List[DB] = dbs.filter (k => (k \ "dbName").text == activeDB ).map(db =>{
      new DB((db \ "kind").text, (db \ "dbName").text, (db \ "username").text, (db \ "password").text)
    }).toList
    
    val uri = if(System.getProperty("os.name") == "Linux") 
                "remote:localhost/" + db(0).dbName
              else 
                "remote:localhost/" + db(0).dbName
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, db(0).username, db(0).password)
    factory.getTx()
  }
  
  def getGraph(db: String, username: String, password: String) = {
    val uri = if(System.getProperty("os.name") == "Linux") 
                "remote:localhost/" + db 
              else 
                "remote:localhost/" + db
    
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri, username, password)
    factory.getTx()
  }
}

case class DB (
    kind: String,
    dbName: String,
    username: String,
    password: String
)