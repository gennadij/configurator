package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import orientdb.App

class TestOrientdb {
  
  val app = new App
  
  app.startServer()

   val uri: String = "remote:localhost/test"
//   val uri: String = "plocal:C:/Users/heimann/scala/sbt_projects/configurator/databases/test"
  
  val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
  val graph: OrientGraph = factory.getTx()
  
//  val person: OrientVertexType = graph.createVertexType("Person2")
//      person.createProperty("firstName", OType.STRING)
//      person.createProperty("lastName", OType.STRING)
  
  println("Graph" + graph)
}