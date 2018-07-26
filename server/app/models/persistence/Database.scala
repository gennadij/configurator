package models.persistence

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 25.10.2017
 */

object Database {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @return
   */
  
  def getFactory: OrientGraphFactory = {
    
    new Database("OrientDB").getFactory
  }
}

class Database(name: String) {
  
  private def getFactory: OrientGraphFactory = {
    
    val db = TestDB()
    
    db match {
      case testDB: TestDB => 
        val uri = "remote:localhost/" + testDB.dbName
        val factory = new OrientGraphFactory(uri, testDB.username, testDB.password)
        factory
    }
  }
}

case class TestDB() {
    def dbName: String = "testDB"
    def username: String = "root"
    def password: String = "root"
}
