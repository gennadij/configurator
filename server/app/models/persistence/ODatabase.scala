package models.persistence

import com.orientechnologies.orient.core.exception.ODatabaseException
import com.tinkerpop.blueprints.impls.orient.{OrientGraph, OrientGraphFactory}
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 25.10.2017
 */

object ODatabase {

  /**
   * @author Gennadi Heimann
   *
   * @version 0.0.1
   *
   * @return
   */

  def getFactory: (Option[OrientGraph], String) = {

    new ODatabase().getFactory
  }
}

class ODatabase() {
  
  private def getFactory: (Option[OrientGraph], String) = {
    
    val db = TestDB()
    
    db match {
      case testDB: TestDB =>
        val uri = "remote:localhost/" + testDB.dbName
        val factory = new OrientGraphFactory(uri, testDB.username, testDB.password)
        try {
          (Some(factory.getTx), "Datebase is open")
        }catch {
          case e : ODatabaseException =>
            val errorMesage = e.getMessage
            Logger.error(e.printStackTrace().toString)
            (None, errorMesage)
        }
    }
  }
}

case class TestDB() {
    def dbName: String = "testDB"
    def username: String = "root"
    def password: String = "root"
}
