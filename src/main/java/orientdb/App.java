package orientdb;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;


public class App {

	private OServer server = null;
	
	public void createServer() throws Exception{
		System.out.println("Create Server");
		
		server = OServerMain.create();
//		server.startup(new File("/home/gennadi/development/projects/testOrientdbEmbedded/src/main/resources/db.config"));
		server = server.startup(getClass().getClassLoader().getResourceAsStream("db.config"));
	}

	public void startServer() throws Exception {
		server.activate();
		System.out.println("Server started");
	}
	
	public void shutdownServer() throws Exception{
		System.out.println("Server shutdown");
		server.shutdown();
	}
	
	public OServer getServer(){
		return server;
	}
	
	public void readPerson(OrientGraph graph){
//		OrientVertexType  person = graph.createVertexType("Person2");
//		person.createProperty("firstName", OType.STRING);
//		person.createProperty("lastName", OType.STRING);
//		graph.addVertex("class:Person2");
//		
//		System.out.println("start");
//		for (Vertex v : (Iterable<Vertex>) graph.command(
//				new OCommandSQL("SELECT FROM Person2")).execute()){
//			System.out.println(v);
//		}
	}
	
	public OrientGraph connectDB(String uri) {
		
		OrientGraphFactory faktory = new OrientGraphFactory(uri);
		OrientGraph graph = faktory.getTx();
		return graph;
	}
}
