package orientdb;

import java.io.File;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class App {

	public static void main(String[] args) throws Exception {
		App app = new App();
		app.startServer();
	}

	public void startServer() throws Exception {
		System.out.println("Create Server");
		OServer server = OServerMain.create();
//		server.startup(new File("/home/gennadi/development/projects/testOrientdbEmbedded/src/main/resources/db.config"));
//		server.activate();
		server = server.startup(getClass().getClassLoader().getResourceAsStream("db.config"));
		server.activate();
		
		
		System.out.println("Server started");
		
		
		
		
//		System.out.println("Server shutdown");
//		server.shutdown();
	}
}
