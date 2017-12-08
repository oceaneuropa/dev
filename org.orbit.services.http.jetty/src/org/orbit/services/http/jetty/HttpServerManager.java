package org.orbit.services.http.jetty;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.websocket.server.ServerContainer;

import org.eclipse.equinox.http.servlet.HttpServiceServlet;
// import org.eclipse.equinox.http.servlet.HttpServiceServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
// import org.eclipse.jetty.server.bio.SocketConnector;
// import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
// import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration Admin Service and ManagedService/ManagedServiceFactory
 * 
 * @see org.eclipse.equinox.http.jetty.internal.HttpServiceManager
 * 
 * @see http://felix.apache.org/documentation/subprojects/apache-felix-config-admin.html
 * 
 * @see https://blog.openshift.com/how-to-build-java-websocket-applications-using-the-jsr-356-api/
 * 
 * @see ConfigAdminSupport for ConfigAdmin example
 * 
 * @see ConfigInstaller for Configuration example
 * 
 * @see https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/master/javax.websocket-example/src/main/java/org/eclipse/jetty/demo/EventServer.
 *      java
 * 
 *      Example code to start Jetty Server
 * 
 */
public class HttpServerManager implements ManagedServiceFactory {

	protected static Logger LOG = LoggerFactory.getLogger(HttpServerManager.class);

	// HttpServerManager.update()
	// -> Create new Jetty Server instance for each PID using the Dictionary as its configurations.
	//
	// Jetty Server
	// -> Jetty ServerConnector (extends Jetty Connector)
	//
	// -> Jetty ServletContextHandler // e.g. ServletContextHandler.addServlet(servletHolder, "/*")
	// ---- Jetty SessionHandler
	// -------- Jetty HashSessionManager
	//
	// ---> Jetty ServletHolder
	// ------> init parameters // e.g. service.vendor, service.description, http.port, https.port, other.info
	//
	// ------> InternalHttpServiceServlet (implements javax.servlet.Servlet) // Wrapper servlet of the real HttpServiceServlet
	// ---------> HttpServiceServlet (extends equinox ProxyServlet) // ProxyServlet extends javax.servlet.http.HttpServlet
	//
	// ------------> InternalHttpServiceServlet.init(ServletConfig)
	// -----------------// in newer version, new instance of HttpServiceServlet is registered as javax.servlet.http.HttpServlet service.
	// ---------------> ProxyServlet.init(ServletConfig)
	// -----------------// in newer version, the Activator has service tracker to track the javax.servlet.http.HttpServlet service. Converts/creates a
	// -----------------// HttpServiceRuntimeImpl using the HttpServiceServlet and initialize a HttpServiceFactory with the HttpServiceRuntimeImpl.
	// ------------------> Activator.addProxyServlet(ProxyServlet)
	// ---------------------> Activator.registerHttpService(ProxyServlet)
	// ------------------------> new HttpServiceFactory(ProxyServlet)
	// ---------------------------> new HttpServiceImpl(ProxyServlet) // for each client Bundle getting the HttpService

	// Note:
	// ProxyServlet is initialized when Jetty Server is started.
	//
	// Stack trace when new HttpServiceFactory instance is created:
	// --------------------------------------------------------------------------------------------
	// HttpServiceFactory.<init>(HttpServiceRuntimeImpl) line: 26
	// Activator.addingService(ServiceReference<HttpServlet>) line: 157
	// Activator.addingService(ServiceReference) line: 1 // org.eclipse.equinox.http.servlet_1.2.0.v20150509
	//
	// ServiceTracker$Tracked.customizerAdding(ServiceReference<S>, ServiceEvent) line: 941
	// ServiceTracker$Tracked.customizerAdding(Object, Object) line: 1
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).trackAdding(S, R) line: 256
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).track(S, R) line: 229
	// ServiceTracker$Tracked.serviceChanged(ServiceEvent) line: 901
	// FilteredServiceListener.serviceChanged(ServiceEvent) line: 109
	// BundleContextImpl.dispatchEvent(Object, Object, int, Object) line: 914
	// EventManager.dispatchEvent(Set<Entry<K,V>>, EventDispatcher<K,V,E>, int, E) line: 230
	// ListenerQueue<K,V,E>.dispatchEventSynchronous(int, E) line: 148
	// ServiceRegistry.publishServiceEventPrivileged(ServiceEvent) line: 862
	// ServiceRegistry.publishServiceEvent(ServiceEvent) line: 801
	// ServiceRegistrationImpl<S>.register(Dictionary<String,?>) line: 127
	// ServiceRegistry.registerService(BundleContextImpl, String[], Object, Dictionary<String,?>) line: 225
	// BundleContextImpl.registerService(String[], Object, Dictionary<String,?>) line: 464
	// BundleContextImpl.registerService(String, Object, Dictionary<String,?>) line: 482
	// BundleContextImpl.registerService(Class<S>, S, Dictionary<String,?>) line: 998
	// ------ clazz Class<T> (javax.servlet.http.HttpServlet) (id=55)
	// ------ service HttpServiceServlet (id=54)
	// ------ properties Hashtable<K,V> (id=275)
	//
	// Activator.addProxyServlet(ProxyServlet) line: 58 // org.eclipse.equinox.http.servlet_1.2.0.v20150509
	// HttpServiceServlet(ProxyServlet).init(ServletConfig) line: 36 // org.eclipse.equinox.http.servlet_1.2.0.v20150509
	//
	// HttpServerManager$InternalHttpServiceServlet.init(ServletConfig) line: 333 // org.eclipse.equinox.http.jetty
	// ServletHolder.initServlet() line: 643
	// ServletHolder.initialize() line: 422
	// ServletHandler.initialize() line: 892
	// ServletContextHandler.startContext() line: 349
	// ServletContextHandler(ContextHandler).doStart() line: 778
	// ServletContextHandler.doStart() line: 262
	// ServletContextHandler(AbstractLifeCycle).start() line: 68
	// Server(ContainerLifeCycle).start(LifeCycle) line: 131
	// Server.start(LifeCycle) line: 422
	// Server(ContainerLifeCycle).doStart() line: 105
	// Server(AbstractHandler).doStart() line: 61
	// Server.doStart() line: 389
	// Server(AbstractLifeCycle).start() line: 68
	// HttpServerManager.updated(String, Dictionary) line: 143 // org.eclipse.equinox.http.jetty
	// Activator.start(BundleContext) line: 62 // org.eclipse.equinox.http.jetty
	// --------------------------------------------------------------------------------------------
	//
	// Note:
	// HttpServiceImpl is created when code from a client bundle (e.g. org.origin.common) tries to get the HttpService.
	//
	// Stack trace when new HttpServiceImpl instance is created:
	// --------------------------------------------------------------------------------------------
	// HttpServiceImpl.<init>(Bundle, HttpServiceRuntimeImpl) line: 63
	// ------ bundle EquinoxBundle (id=126) org.origin.common_1.0.0.qualifier [27]
	// ------ httpServiceRuntime HttpServiceRuntimeImpl (id=109)
	//
	// HttpServiceFactory.getService(Bundle, ServiceRegistration<HttpService>) line: 33
	// HttpServiceFactory.getService(Bundle, ServiceRegistration) line: 1
	// ServiceFactoryUse$1.run() line: 212
	// AccessController.doPrivileged(PrivilegedAction<T>) line: not available [native method]
	// ServiceFactoryUse<S>.factoryGetService() line: 210
	// ServiceFactoryUse<S>.getService() line: 111
	// ServiceConsumer$2.getService(ServiceUse<S>) line: 45
	// ServiceRegistrationImpl<S>.getService(BundleContextImpl, ServiceConsumer) line: 496
	// ServiceRegistry.getService(BundleContextImpl, ServiceReferenceImpl<S>) line: 461
	// BundleContextImpl.getService(ServiceReference<S>) line: 619
	// HttpServiceAdapter$1.addingService(ServiceReference<HttpService>) line: 39
	// HttpServiceAdapter$1.addingService(ServiceReference) line: 1
	// ServiceTracker$Tracked.customizerAdding(ServiceReference<S>, ServiceEvent) line: 941
	// ServiceTracker$Tracked.customizerAdding(Object, Object) line: 1
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).trackAdding(S, R) line: 256
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).track(S, R) line: 229
	// ServiceTracker$Tracked.serviceChanged(ServiceEvent) line: 901
	// FilteredServiceListener.serviceChanged(ServiceEvent) line: 109
	// BundleContextImpl.dispatchEvent(Object, Object, int, Object) line: 914
	// EventManager.dispatchEvent(Set<Entry<K,V>>, EventDispatcher<K,V,E>, int, E) line: 230
	// ListenerQueue<K,V,E>.dispatchEventSynchronous(int, E) line: 148
	// ServiceRegistry.publishServiceEventPrivileged(ServiceEvent) line: 862
	// ServiceRegistry.publishServiceEvent(ServiceEvent) line: 801
	// ServiceRegistrationImpl<S>.register(Dictionary<String,?>) line: 127
	// ServiceRegistry.registerService(BundleContextImpl, String[], Object, Dictionary<String,?>) line: 225
	// BundleContextImpl.registerService(String[], Object, Dictionary<String,?>) line: 464
	// Activator.addingService(ServiceReference<HttpServlet>) line: 159
	// Activator.addingService(ServiceReference) line: 1
	// ServiceTracker$Tracked.customizerAdding(ServiceReference<S>, ServiceEvent) line: 941
	// ServiceTracker$Tracked.customizerAdding(Object, Object) line: 1
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).trackAdding(S, R) line: 256
	// ServiceTracker$Tracked(AbstractTracked<S,T,R>).track(S, R) line: 229
	// ServiceTracker$Tracked.serviceChanged(ServiceEvent) line: 901
	// FilteredServiceListener.serviceChanged(ServiceEvent) line: 109
	// BundleContextImpl.dispatchEvent(Object, Object, int, Object) line: 914
	// EventManager.dispatchEvent(Set<Entry<K,V>>, EventDispatcher<K,V,E>, int, E) line: 230
	// ListenerQueue<K,V,E>.dispatchEventSynchronous(int, E) line: 148
	// ServiceRegistry.publishServiceEventPrivileged(ServiceEvent) line: 862
	// ServiceRegistry.publishServiceEvent(ServiceEvent) line: 801
	// ServiceRegistrationImpl<S>.register(Dictionary<String,?>) line: 127
	// ServiceRegistry.registerService(BundleContextImpl, String[], Object, Dictionary<String,?>) line: 225
	// BundleContextImpl.registerService(String[], Object, Dictionary<String,?>) line: 464
	// BundleContextImpl.registerService(String, Object, Dictionary<String,?>) line: 482
	// BundleContextImpl.registerService(Class<S>, S, Dictionary<String,?>) line: 998
	// Activator.addProxyServlet(ProxyServlet) line: 58
	// HttpServiceServlet(ProxyServlet).init(ServletConfig) line: 36
	// HttpServerManager$InternalHttpServiceServlet.init(ServletConfig) line: 333
	// ServletHolder.initServlet() line: 643
	// ServletHolder.initialize() line: 422
	// ServletHandler.initialize() line: 892
	// ServletContextHandler.startContext() line: 349
	// ServletContextHandler(ContextHandler).doStart() line: 778
	// ServletContextHandler.doStart() line: 262
	// ServletContextHandler(AbstractLifeCycle).start() line: 68
	// Server(ContainerLifeCycle).start(LifeCycle) line: 131
	// Server.start(LifeCycle) line: 422
	// Server(ContainerLifeCycle).doStart() line: 105
	// Server(AbstractHandler).doStart() line: 61
	// Server.doStart() line: 389
	// Server(AbstractLifeCycle).start() line: 68
	// HttpServerManager.updated(String, Dictionary) line: 143
	// Activator.start(BundleContext) line: 62
	// --------------------------------------------------------------------------------------------

	protected static final String CONTEXT_TEMPDIR = "javax.servlet.context.tempdir"; //$NON-NLS-1$
	protected static final String DIR_PREFIX = "pid_"; //$NON-NLS-1$
	protected static final String INTERNAL_CONTEXT_CLASSLOADER = "org.eclipse.equinox.http.jetty.internal.ContextClassLoader"; //$NON-NLS-1$

	protected BundleContext bundleContext;
	protected File workDir;
	protected Map<String, Server> servers = new HashMap<String, Server>();
	protected Map<Server, ServiceRegistration<ServerContainer>> serverToWebSocketContainerMap = new HashMap<Server, ServiceRegistration<ServerContainer>>();

	/**
	 * 
	 * @param bundleContext
	 * @param workDir
	 */
	public HttpServerManager(BundleContext bundleContext, File workDir) {
		this.bundleContext = bundleContext;
		this.workDir = workDir;
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * The PID is just a string, which must be globally unique. Usually PID is factoryID + OSGi-generated-random-ID.
	 * 
	 */
	@Override
	public synchronized void updated(String pid, Dictionary<String, ?> dictionary) throws ConfigurationException {
		LOG.debug("updated()");
		LOG.debug("pid = " + pid);
		LOG.debug("dictionary is:");
		for (Enumeration<String> itor = dictionary.keys(); itor.hasMoreElements();) {
			String propName = itor.nextElement();
			Object propValue = dictionary.get(propName);
			LOG.debug("\t" + propName + " = " + propValue);
		}

		deleted(pid);
		Server server = new Server();

		ServerConnector httpConnector = createHttpConnector(server, dictionary);
		if (httpConnector != null) {
			server.addConnector(httpConnector);
		}
		// ServerConnector httpsConnector = createHttpsConnector(dictionary);
		// if (httpsConnector != null) {
		// server.addConnector(httpsConnector);
		// }

		// ServletHolder servletHolder = new ServletHolder(new InternalHttpServiceServlet()); // implements javax.servlet.Servlet
		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder servletHolder = new ServletHolder(defaultServlet);

		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter(Constants.SERVICE_VENDOR, "Orbit"); //$NON-NLS-1$
		servletHolder.setInitParameter(Constants.SERVICE_DESCRIPTION, "Orbit Jetty Http Service"); //$NON-NLS-1$

		if (httpConnector != null) {
			int port = httpConnector.getLocalPort();
			if (port == -1) {
				port = httpConnector.getPort();
			}
			servletHolder.setInitParameter(JettyConstants.HTTP_PORT, Integer.toString(port));
		}
		// if (httpsConnector != null) {
		// int port = httpsConnector.getLocalPort();
		// if (port == -1) {
		// port = httpsConnector.getPort();
		// }
		// servletHolder.setInitParameter(JettyConstants.HTTPS_PORT, Integer.toString(port));
		// }

		String otherInfo = (String) dictionary.get(JettyConstants.OTHER_INFO);
		if (otherInfo != null) {
			servletHolder.setInitParameter(JettyConstants.OTHER_INFO, otherInfo);
		}

		ServletContextHandler httpContext = createHttpContext(dictionary);
		httpContext.addServlet(servletHolder, "/*"); //$NON-NLS-1$
		server.setHandler(httpContext);

		ServerContainer webSocketContainer = null;
		try {
			webSocketContainer = WebSocketServerContainerInitializer.configureContext(httpContext);

		} catch (ServletException e) {
			e.printStackTrace();
		}

		try {
			server.start();

			LOG.debug("Jetty Server is started.");
			LOG.debug("Init parameters:");
			Map<String, String> initParams = servletHolder.getInitParameters();
			for (Iterator<String> paramItor = initParams.keySet().iterator(); paramItor.hasNext();) {
				String paramName = paramItor.next();
				String paramValue = initParams.get(paramName);
				LOG.debug("\t" + paramName + " = " + paramValue);
			}

		} catch (Exception e) {
			throw new ConfigurationException(pid, e.getMessage(), e);
		}
		this.servers.put(pid, server);

		registerWebSocketContainer(this.bundleContext, server, servletHolder, webSocketContainer);
	}

	@Override
	public synchronized void deleted(String pid) {
		LOG.debug("deleted()");
		LOG.debug("pid = " + pid);

		Server server = this.servers.remove(pid);
		if (server != null) {
			unregisterWebSocketContainer(this.bundleContext, server);

			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			File contextWorkDir = new File(workDir, DIR_PREFIX + pid.hashCode());
			deleteDirectory(contextWorkDir);
		}
	}

	/**
	 * deleteDirectory is a convenience method to recursively delete a directory
	 * 
	 * @param directory
	 * @return
	 */
	private static boolean deleteDirectory(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return directory.delete();
	}

	/**
	 * Called by Activator.stop()
	 * 
	 * @throws Exception
	 */
	public synchronized void shutdown() throws Exception {
		LOG.debug("Jetty Servers are shut down.");

		for (Iterator<Server> it = this.servers.values().iterator(); it.hasNext();) {
			Server server = it.next();

			unregisterWebSocketContainer(this.bundleContext, server);

			server.stop();
		}
		this.servers.clear();
	}

	/**
	 * Called by this.update(String pid, Dictionary dictionary)
	 * 
	 * @param dictionary
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ServerConnector createHttpConnector(Server server, Dictionary dictionary) {
		Boolean httpEnabled = null;

		Object httpEnabledObj = dictionary.get(JettyConstants.HTTP_ENABLED);
		if (httpEnabledObj instanceof Boolean) {
			httpEnabled = (Boolean) httpEnabledObj;
		} else if (httpEnabledObj instanceof String) {
			httpEnabled = Boolean.parseBoolean(httpEnabledObj.toString());
		}
		if (httpEnabled != null && !httpEnabled.booleanValue()) {
			return null;
		}

		Integer httpPort = null;
		Object httpPortObj = dictionary.get(JettyConstants.HTTP_PORT);
		if (httpPortObj instanceof Integer) {
			httpPort = (Integer) httpPortObj;
		} else if (httpPortObj instanceof String) {
			httpPort = Integer.valueOf(httpPortObj.toString());
		}
		if (httpPort == null) {
			return null;
		}

		Boolean nioEnabled = null;
		Object nioEnabledObj = dictionary.get(JettyConstants.HTTP_NIO);
		if (nioEnabledObj instanceof Boolean) {
			nioEnabled = (Boolean) nioEnabledObj;
		} else if (nioEnabledObj instanceof String) {
			nioEnabled = Boolean.parseBoolean(nioEnabledObj.toString());
		}
		if (nioEnabled == null) {
			nioEnabled = getDefaultNIOEnablement();
		}

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(httpPort.intValue());

		String httpHost = (String) dictionary.get(JettyConstants.HTTP_HOST);
		if (httpHost != null) {
			connector.setHost(httpHost);
		}

		return connector;
	}

	private Boolean getDefaultNIOEnablement() {
		Properties systemProperties = System.getProperties();
		String javaVendor = systemProperties.getProperty("java.vendor", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (javaVendor.equals("IBM Corporation")) { //$NON-NLS-1$
			String javaVersion = systemProperties.getProperty("java.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if (javaVersion.startsWith("1.4")) //$NON-NLS-1$
				return Boolean.FALSE;
			// Note: no problems currently logged with 1.5
			if (javaVersion.equals("1.6.0")) { //$NON-NLS-1$
				String jclVersion = systemProperties.getProperty("java.jcl.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
				if (jclVersion.startsWith("2007")) //$NON-NLS-1$
					return Boolean.FALSE;
				if (jclVersion.startsWith("2008") && !jclVersion.startsWith("200811") && !jclVersion.startsWith("200812")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	@SuppressWarnings("rawtypes")
	private ServletContextHandler createHttpContext(Dictionary dictionary) {
		ServletContextHandler httpContext = new ServletContextHandler();
		httpContext.setAttribute(INTERNAL_CONTEXT_CLASSLOADER, Thread.currentThread().getContextClassLoader());
		httpContext.setClassLoader(this.getClass().getClassLoader());

		String contextPathProperty = (String) dictionary.get(JettyConstants.CONTEXT_PATH);
		if (contextPathProperty == null) {
			contextPathProperty = "/"; //$NON-NLS-1$
		}
		httpContext.setContextPath(contextPathProperty);

		File contextWorkDir = new File(workDir, DIR_PREFIX + dictionary.get(Constants.SERVICE_PID).hashCode());
		contextWorkDir.mkdir();
		httpContext.setAttribute(CONTEXT_TEMPDIR, contextWorkDir);

		HashSessionManager sessionManager = new HashSessionManager();
		Integer sessionInactiveInterval = null;

		Object sessionInactiveIntervalObj = dictionary.get(JettyConstants.CONTEXT_SESSIONINACTIVEINTERVAL);
		if (sessionInactiveIntervalObj instanceof Integer) {
			sessionInactiveInterval = (Integer) sessionInactiveIntervalObj;
		} else if (sessionInactiveIntervalObj instanceof String) {
			sessionInactiveInterval = Integer.valueOf(sessionInactiveIntervalObj.toString());
		}

		if (sessionInactiveInterval != null) {
			sessionManager.setMaxInactiveInterval(sessionInactiveInterval.intValue());
		}
		httpContext.setSessionHandler(new SessionHandler(sessionManager));

		return httpContext;
	}

	public static class InternalHttpServiceServlet implements Servlet {
		// HttpServiceServlet extends ProxyServlet, which extends javax.servlet.http.HttpServlet.
		// private Servlet httpServiceServlet = new HttpServiceServlet();
		private Servlet httpServiceServlet = new HttpServiceServlet();
		private ClassLoader contextLoader;

		@Override
		public void init(ServletConfig config) throws ServletException {
			ServletContext context = config.getServletContext();
			this.contextLoader = (ClassLoader) context.getAttribute(INTERNAL_CONTEXT_CLASSLOADER);

			Thread thread = Thread.currentThread();
			ClassLoader current = thread.getContextClassLoader();
			thread.setContextClassLoader(this.contextLoader);
			try {
				this.httpServiceServlet.init(config);
			} finally {
				thread.setContextClassLoader(current);
			}
		}

		@Override
		public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
			Thread thread = Thread.currentThread();
			ClassLoader current = thread.getContextClassLoader();
			thread.setContextClassLoader(contextLoader);
			try {
				this.httpServiceServlet.service(req, res);
			} finally {
				thread.setContextClassLoader(current);
			}
		}

		@Override
		public ServletConfig getServletConfig() {
			return this.httpServiceServlet.getServletConfig();
		}

		@Override
		public String getServletInfo() {
			return this.httpServiceServlet.getServletInfo();
		}

		@Override
		public void destroy() {
			Thread thread = Thread.currentThread();
			ClassLoader current = thread.getContextClassLoader();
			thread.setContextClassLoader(this.contextLoader);
			try {
				this.httpServiceServlet.destroy();
			} finally {
				thread.setContextClassLoader(current);
			}
			this.contextLoader = null;
		}
	}

	/**
	 * Register WebSocket ServerContainer service.
	 * 
	 * @param bundleContext
	 * @param server
	 * @param servletHolder
	 * @param container
	 */
	protected void registerWebSocketContainer(BundleContext bundleContext, Server server, ServletHolder servletHolder, ServerContainer container) {
		LOG.debug("registerWebSocketContainer()");

		try {
			if (bundleContext != null && server != null && servletHolder != null && container != null) {
				Map<String, String> initParams = servletHolder.getInitParameters();

				Hashtable<String, Object> props = new Hashtable<String, Object>();
				props.putAll(initParams);

				LOG.debug("Register ServerContainer " + container);

				ServiceRegistration<ServerContainer> serviceRegistration = bundleContext.registerService(ServerContainer.class, container, props);
				if (serviceRegistration != null) {
					this.serverToWebSocketContainerMap.put(server, serviceRegistration);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unregister WebSocket ServerContainer service.
	 * 
	 * @param bundleContext
	 * @param server
	 */
	protected void unregisterWebSocketContainer(BundleContext bundleContext, Server server) {
		try {
			if (bundleContext != null && server != null) {
				ServiceRegistration<ServerContainer> serviceRegistration = this.serverToWebSocketContainerMap.remove(server);
				if (serviceRegistration != null) {
					LOG.debug("Unregister ServerContainer.");

					serviceRegistration.unregister();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// @SuppressWarnings({ "rawtypes" })
// private ServerConnector createHttpsConnector(Dictionary dictionary) {
// Boolean httpsEnabled = null;
//
// Object httpsEnabledObj = dictionary.get(JettyConstants.HTTPS_ENABLED);
// if (httpsEnabledObj instanceof Boolean) {
// httpsEnabled = (Boolean) httpsEnabledObj;
// } else if (httpsEnabledObj instanceof String) {
// httpsEnabled = Boolean.parseBoolean(httpsEnabledObj.toString());
// }
//
// if (httpsEnabled == null || !httpsEnabled.booleanValue()) {
// return null;
// }
//
// Integer httpsPort = null;
// Object httpsPortObj = dictionary.get(JettyConstants.HTTPS_PORT);
// if (httpsPortObj instanceof Integer) {
// httpsPort = (Integer) httpsPortObj;
// } else if (httpsPortObj instanceof String) {
// httpsPort = Integer.valueOf(httpsPortObj.toString());
// }
// if (httpsPort == null) {
// return null;
// }

// SslSocketConnector sslConnector = new SslSocketConnector();
// sslConnector.setPort(httpsPort.intValue());
//
// String httpsHost = (String) dictionary.get(JettyConstants.HTTPS_HOST);
// if (httpsHost != null) {
// sslConnector.setHost(httpsHost);
// }
//
// String keyStore = (String) dictionary.get(JettyConstants.SSL_KEYSTORE);
// if (keyStore != null) {
// sslConnector.setKeystore(keyStore);
// }
//
// String password = (String) dictionary.get(JettyConstants.SSL_PASSWORD);
// if (password != null) {
// sslConnector.setPassword(password);
// }
//
// String keyPassword = (String) dictionary.get(JettyConstants.SSL_KEYPASSWORD);
// if (keyPassword != null) {
// sslConnector.setKeyPassword(keyPassword);
// }
//
// Object needClientAuth = dictionary.get(JettyConstants.SSL_NEEDCLIENTAUTH);
// if (needClientAuth != null) {
// if (needClientAuth instanceof String) {
// needClientAuth = Boolean.valueOf((String) needClientAuth);
// }
// sslConnector.setNeedClientAuth(((Boolean) needClientAuth).booleanValue());
// }
//
// Object wantClientAuth = dictionary.get(JettyConstants.SSL_WANTCLIENTAUTH);
// if (wantClientAuth != null) {
// if (wantClientAuth instanceof String) {
// wantClientAuth = Boolean.valueOf((String) wantClientAuth);
// }
// sslConnector.setWantClientAuth(((Boolean) wantClientAuth).booleanValue());
// }
//
// String protocol = (String) dictionary.get(JettyConstants.SSL_PROTOCOL);
// if (protocol != null) {
// sslConnector.setProtocol(protocol);
// }
//
// String keystoreType = (String) dictionary.get(JettyConstants.SSL_KEYSTORETYPE);
// if (keystoreType != null) {
// sslConnector.setKeystoreType(keystoreType);
// }
//
// if (sslConnector.getPort() == 0) {
// try {
// sslConnector.open();
// } catch (IOException e) {
// // this would be unexpected since we're opening the next available port
// e.printStackTrace();
// }
// }
// return sslConnector;
// return null;
// }
