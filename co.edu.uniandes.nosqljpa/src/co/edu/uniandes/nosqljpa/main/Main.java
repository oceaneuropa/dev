package co.edu.uniandes.nosqljpa.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class.getName());

	public static void main(String agrs[]) {
		try {
			String webappDirLocation = "src/main/webapp/";
			String webPort = System.getenv("PORT");
			if (webPort == null || webPort.isEmpty()) {
				webPort = "8080";
			}

			Server server = new Server(Integer.valueOf(webPort));
			WebAppContext root = new WebAppContext();
			root.setContextPath("/");
			root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
			root.setResourceBase(webappDirLocation);
			root.setParentLoaderPriority(true);
			server.setHandler(root);
			server.start();
			server.join();

		} catch (InterruptedException ex) {
			LOG.log(Level.WARNING, ex.getMessage());
		} catch (Exception ex) {
			LOG.log(Level.WARNING, ex.getMessage());
		}
	}

}
