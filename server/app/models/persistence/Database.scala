package models.persistence

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.orientechnologies.orient.core.sql.OCommandSQL
import play.api.Logger

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
  
  def getFactory(): OrientGraphFactory = {
    
    new Database("OrientDB").getFactory()
  }
}

class Database(name: String) {
  
  private def getFactory(): OrientGraphFactory = {
    
    val db = TestDB()
    
    db match {
      case testDB: TestDB => 
        val uri = "remote:localhost/" + testDB.dbName
        new OrientGraphFactory(uri, testDB.username, testDB.password)
    }
  }
}

case class TestDB() {
    def dbName: String = "testDB"
    def username: String = "root"
    def password: String = "root"
}
