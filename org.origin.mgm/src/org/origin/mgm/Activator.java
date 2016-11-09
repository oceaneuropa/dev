package org.origin.mgm;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.origin.common.env.SetupUtil;
import org.origin.common.util.Printer;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl.IndexServiceDatabaseConfiguration;
import org.origin.mgm.service.impl.IndexServiceDatabaseImpl;
import org.origin.mgm.ws.IndexServiceApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static IndexServiceDatabaseImpl indexService;

	public static BundleContext getContext() {
		return context;
	}

	public static IndexService getIndexService() {
		return Activator.indexService;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected IndexServiceApplication indexServiceApplication;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// Common properties
		// -----------------------------------------------------------------------------
		// Get ORIGIN_HOME system property and load {ORIGIN_HOME}/config.ini file into Properties.
		Path originHome = SetupUtil.getOriginHome(bundleContext);
		Path configIniPath = originHome.resolve("config.ini");
		Properties configIniProps = SetupUtil.getProperties(originHome, "config.ini");
		System.out.println("originHome = " + originHome);
		System.out.println(configIniPath + " properties:");
		Printer.pl(configIniProps);

		Map<Object, Object> commonProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.OSGI_HTTP_PORT_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_HOST_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_PORT_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_CONTEXT_ROOT_PROP);

		// get http host
		String host = (String) commonProps.get(OriginConstants.ORBIT_HTTP_HOST_PROP);
		if (host == null || host.isEmpty()) {
			try {
				InetAddress address = InetAddress.getLocalHost();
				host = address.getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		// get http port
		String port = (String) commonProps.get(OriginConstants.OSGI_HTTP_PORT_PROP);
		if (port == null || port.isEmpty()) {
			port = (String) commonProps.get(OriginConstants.ORBIT_HTTP_PORT_PROP);
		}
		if (port == null || port.isEmpty()) {
			port = "80";
		}

		// get http context root
		String contextRoot = (String) commonProps.get(OriginConstants.ORBIT_HTTP_CONTEXT_ROOT_PROP);
		if (contextRoot == null || contextRoot.isEmpty()) {
			contextRoot = OriginConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
		}
		if (!contextRoot.startsWith("/")) {
			contextRoot = "/" + contextRoot;
		}

		// current server URL
		final String serverURL = "http://" + host + ":" + port;

		// -----------------------------------------------------------------------------
		// Start services
		// -----------------------------------------------------------------------------
		// 1. IndexService
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_NAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD_PROP);

		if (!configIniProps.containsKey(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT_PROP)) {
			configIniProps.put(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT_PROP, contextRoot);
		}
		if (!configIniProps.containsKey(OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP)) {
			configIniProps.put(OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP, serverURL);
		}

		IndexServiceDatabaseConfiguration config = new IndexServiceDatabaseConfiguration(configIniProps);
		IndexServiceDatabaseImpl indexService = new IndexServiceDatabaseImpl(bundleContext, config);
		indexService.initializeTables();
		indexService.start();
		Activator.indexService = indexService;

		// -----------------------------------------------------------------------------
		// Start web services
		// -----------------------------------------------------------------------------
		// 1. IndexService
		this.indexServiceApplication = new IndexServiceApplication(bundleContext, contextRoot);
		this.indexServiceApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// -----------------------------------------------------------------------------
		// Stop web services
		// -----------------------------------------------------------------------------
		// 1. IndexService
		if (this.indexServiceApplication != null) {
			this.indexServiceApplication.stop();
			this.indexServiceApplication = null;
		}

		// -----------------------------------------------------------------------------
		// Stop services
		// -----------------------------------------------------------------------------
		// 1. IndexService
		if (Activator.indexService != null) {
			Activator.indexService.stop();
			Activator.indexService = null;
		}

		Activator.context = null;
	}

}

// String driver = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.driver", String.class, "org.postgresql.Driver");
// String url = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.url", String.class,
// "jdbc:postgresql://127.0.0.1:5432/origin");
// String username = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.username", String.class, "postgres");
// String password = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.password", String.class, "admin");
// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
// Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres",
// "admin");
// Properties properties = DatabaseUtil.getProperties(driver, url, username, password);
// PropertyUtil.loadProperty(bundleContext, properties, IndexServiceConstants.CONFIG_PROP_HEARTBEAT_EXPIRE_TIME, Integer.class);
// PropertyUtil.loadProperty(bundleContext, properties, IndexServiceConstants.CONFIG_PROP_NODE_NAME, String.class);
// properties.putAll(configIniProperties);
// System.out.println("DatabaseIndexServiceConfiguration properties:");
// Printer.pl(properties);
