package orientdb;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

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
}
