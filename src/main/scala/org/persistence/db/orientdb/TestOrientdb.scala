package org.persistence.db.orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import sun.security.provider.certpath.Vertex
import com.orientechnologies.orient.server.OServerMain
import com.orientechnologies.orient.server.OServer
import com.tinkerpop.blueprints.impls.orient.OrientVertex

import scala.collection.JavaConversions._

class TestOrientdb {
  
//  val uri: String = "plocal:/home/gennadi/development/projects/configurator/databases/test1"
//  val uri: String = "plocal:/C:/Users/heimann/scala/sbt_projects/configurator/databases/test1"
  val uri: String = "remote:localhost/test"
//  val server = createServer()
//  startServer(server)
//  val graph: OrientGraph = connectDB(uri)
  val factory:  OrientGraphFactory = new OrientGraphFactory(uri, "root", "root")
  val graph: OrientGraph = factory.getTx()
  try{
    if(graph.getVertex("Person") != null){
      val person: OrientVertexType = graph.createVertexType("Person")
      person.createProperty("firstName", OType.STRING)
      person.createProperty("lastName", OType.STRING)
    }
//    graph.addVertex("class:Person", "firstName", "Gennadi", "lastName", "Heimann")
//    graph.commit()
    println(graph.getVertexType("Person"))
    val res: OrientDynaElementIterable = graph.command(new OCommandSQL(s"SELECT FROM Person WHERE firstName='Gennadi'")).execute()
    
    res.foreach(v => {println(v)})
    
  } finally {
//    shutdownServer(server)
  }
  
  def startServer(server: OServer) = {
		server.activate();
		System.out.println("Server started");
	}
  
  def createServer(): OServer = {
    val server: OServer = OServerMain.create()
    server.startup(getClass().getClassLoader().getResourceAsStream("db.config"))
    server
  }
  
  def shutdownServer(server: OServer) = {
		System.out.println("Server shutdown");
		server.shutdown();
	}
  
  def connectDB(uri: String): OrientGraph = {
    val factory:  OrientGraphFactory = new OrientGraphFactory(uri)
    factory.getTx()
  }
}