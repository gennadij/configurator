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
   * @param
   * 
   * @return
   */
  
  def getFactory(): OrientGraphFactory = {
    
    new Database("OrientDB").getFactory()
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param SQL-Query -> SQL-Command that start with select
   * 
   * @return OrientDynaElementIterable
   */
  
//  def executeSQLQuery(sqlQuery: String): Any = {
//    new Database().executeSQLQuery(sqlQuery)
//  }
//  
//  def getGraph: OrientGraph = {
//    new Database().getGraph
//  }


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
  
//  private def getGraph = {
//    val activeDb = TestDB()
//    val uri = "remote:localhost/" + activeDb.dbName
//    val graphFactory = new OrientGraphFactory(uri, activeDb.username, activeDb.password)
//    graphFactory.getTx
//  }
//  
//  def executeSQLQuery(sql: String): Any = {
//    
//    val graph: OrientGraph = getGraph
//    
//    val result: Any = try {
//		  val result: OrientDynaElementIterable = graph.command(new OCommandSQL(sql)).execute()
//		  graph.commit()
//		  result
//		}catch{
//		  case e: Exception => 
//		    graph.rollback()
//		    e
//		}finally {
//		  graph.shutdown()
//		}
//		
//		result
//  }
  
}



case class TestDB() {
    def dbName: String = "testDB"
    def username: String = "root"
    def password: String = "root"
}
