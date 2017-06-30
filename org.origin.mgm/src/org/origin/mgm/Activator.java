package org.origin.mgm;

import java.nio.file.Path;
import java.util.Properties;

import org.origin.common.env.SetupUtil;
import org.origin.common.util.Printer;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl2.IndexServiceDatabaseSimpleImpl;
import org.origin.mgm.ws.IndexServiceWSApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static IndexService indexService;

	public static BundleContext getContext() {
		return context;
	}

	public static IndexService getIndexService() {
		return Activator.indexService;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected IndexServiceWSApplication indexServiceApp;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// config.ini properties
		// -----------------------------------------------------------------------------
		// Get ORIGIN_HOME system property and load {ORIGIN_HOME}/config.ini file into Properties.
		Path originHome = SetupUtil.getOriginHome(bundleContext);
		Path configIniPath = originHome.resolve("config.ini");
		Properties configIniProps = SetupUtil.getProperties(originHome, "config.ini");
		System.out.println("originHome = " + originHome);
		System.out.println(configIniPath + " properties:");
		Printer.pl(configIniProps);

		// -----------------------------------------------------------------------------
		// Start IndexService
		// -----------------------------------------------------------------------------
		// 1. Start IndexService
		IndexServiceDatabaseSimpleImpl indexService = new IndexServiceDatabaseSimpleImpl(configIniProps);
		indexService.start(bundleContext);
		Activator.indexService = indexService;

		// -----------------------------------------------------------------------------
		// Start web applications
		// -----------------------------------------------------------------------------
		// 1. Start IndexService web application
		this.indexServiceApp = new IndexServiceWSApplication();
		this.indexServiceApp.setBundleContext(bundleContext);
		this.indexServiceApp.setContextRoot(indexService.getContextRoot());
		this.indexServiceApp.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// -----------------------------------------------------------------------------
		// Stop web applications
		// -----------------------------------------------------------------------------
		// 1. Stop IndexService web application
		if (this.indexServiceApp != null) {
			this.indexServiceApp.stop();
			this.indexServiceApp = null;
		}

		// -----------------------------------------------------------------------------
		// Stop services
		// -----------------------------------------------------------------------------
		// 1. Stop IndexService
		if (Activator.indexService != null) {
			Activator.indexService.stop(bundleContext);
			Activator.indexService = null;
		}

		Activator.context = null;
	}

}

//// -----------------------------------------------------------------------------
//// Common properties
//// -----------------------------------------------------------------------------
// Map<Object, Object> commonProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.OSGI_HTTP_PORT_PROP);
// PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_HOST_PROP);
// PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_PORT_PROP);
// PropertyUtil.loadProperty(bundleContext, commonProps, OriginConstants.ORBIT_HTTP_CONTEXT_ROOT_PROP);
//
//// get http host
// String host = (String) commonProps.get(OriginConstants.ORBIT_HTTP_HOST_PROP);
// if (host == null || host.isEmpty()) {
// try {
// InetAddress address = InetAddress.getLocalHost();
// host = address.getHostAddress();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// }
//
//// get http port
// String port = (String) commonProps.get(OriginConstants.OSGI_HTTP_PORT_PROP);
// if (port == null || port.isEmpty()) {
// port = (String) commonProps.get(OriginConstants.ORBIT_HTTP_PORT_PROP);
// }
// if (port == null || port.isEmpty()) {
// port = "80";
// }
//
//// get http context root
// String contextRoot = (String) commonProps.get(OriginConstants.ORBIT_HTTP_CONTEXT_ROOT_PROP);
// if (contextRoot == null || contextRoot.isEmpty()) {
// contextRoot = OriginConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
// }
// if (!contextRoot.startsWith("/")) {
// contextRoot = "/" + contextRoot;
// }
//
//// current host URL
// String hostURL = "http://" + host + ":" + port;
// System.out.println("hostURL = " + hostURL);

// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_URL);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_NAME);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
// PropertyUtil.loadProperty(bundleContext, configIniProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);
//
// if (!configIniProps.containsKey(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT)) {
// configIniProps.put(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
// }
// if (!configIniProps.containsKey(OriginConstants.COMPONENT_INDEX_SERVICE_URL)) {
// configIniProps.put(OriginConstants.COMPONENT_INDEX_SERVICE_URL, hostURL);
// }
//
// IndexServiceDatabaseConfiguration config = new IndexServiceDatabaseConfiguration(configIniProps);
// IndexServiceDatabaseComplexImpl indexService = new IndexServiceDatabaseComplexImpl(bundleContext, config);
// indexService.start();
// Activator.indexService = indexService;