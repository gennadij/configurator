package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import orientdb.App
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import sun.security.provider.certpath.Vertex

class TestOrientdb {
  
  val app = new App
  
  app.createServer()
  
  app.startServer()

//   val uri: String = "remote:localhost/test"
//   val uri: String = "plocal:C:/Users/heimann/scala/sbt_projects/configurator/databases/test"
    
  val uri: String = "plocal:/home/gennadi/development/projects/configurator/databases/test1"
//  val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
//  val factory:  OrientGraphFactory = new OrientGraphFactory(uri)
//  val graph: OrientGraph = factory.getTx()
  val graph: OrientGraph = app.connectDB(uri)
  try{
    app.readPerson(graph)
//    val res: OrientDynaElementIterable = graph.command(new OCommandSQL(s"SELECT FROM Person2")).execute()
//    val person: OrientVertexType = graph.createVertexType("Person2")
//    println(person)
//    person.createProperty("firstName", OType.STRING)
//    person.createProperty("lastName", OType.STRING)
  } finally {
//    app.shutdownServer()
  }
}