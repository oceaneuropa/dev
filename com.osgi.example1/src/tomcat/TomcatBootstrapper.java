package tomcat;

import java.io.File;

import javax.servlet.ServletException;

//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.WebResourceRoot;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.startup.Tomcat;
//import org.apache.catalina.webresources.DirResourceSet;
//import org.apache.catalina.webresources.StandardRoot;

public class TomcatBootstrapper {

	/**
	 * 
	 * @param webPort
	 * @param webappDirLocation
	 * @return
	 * @throws ServletException
	 * @throws LifecycleException
	 */
//	public Tomcat startTomcat(Integer webPort, String webappDirLocation) throws ServletException, LifecycleException {
//		Tomcat tomcat = new Tomcat();
//		tomcat.setPort(webPort);
//
//		File webappDir = new File(webappDirLocation);
//		StandardContext ctx = (StandardContext) tomcat.addWebapp("/", webappDir.getAbsolutePath());
//		System.out.println("configuring app with basedir: " + webappDirLocation);
//
//		// Declare an alternative location for your "WEB-INF/classes" dir
//		// Servlet 3.0 annotation will work
//		File additionWebInfClasses = new File("target/classes");
//
//		WebResourceRoot resources = new StandardRoot(ctx);
//		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
//		ctx.setResources(resources);
//
//		tomcat.getConnector().setURIEncoding("UTF-8"); // Tomcat 8 does this by default
//
//		tomcat.start();
//
//		return tomcat;
//	}

}
