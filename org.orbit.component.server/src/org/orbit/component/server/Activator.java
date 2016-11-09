package org.orbit.component.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.appstore.service.AppStoreService;
import org.orbit.component.server.appstore.service.AppStoreServiceTimer;
import org.orbit.component.server.appstore.service.impl.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.appstore.ws.AppStoreApplication;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;
import org.orbit.component.server.configregistry.service.ConfigRegistryServiceTimer;
import org.orbit.component.server.configregistry.service.impl.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.server.configregistry.ws.ConfigRegistryApplication;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;
	protected static AppStoreServiceDatabaseImpl appStoreService;
	protected static ConfigRegistryServiceDatabaseImpl configRegistryService;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static AppStoreService getAppStoreService() {
		return appStoreService;
	}

	public static ConfigRegistryService getConfigRegistryService() {
		return configRegistryService;
	}

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;

	protected AppStoreApplication appStoreApplication;
	protected AppStoreServiceTimer appStoreServiceTimer;

	protected ConfigRegistryApplication configRegistryApplication;
	protected ConfigRegistryServiceTimer configRegistryServiceTimer;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		// -----------------------------------------------------------------------------
		// Common properties
		// -----------------------------------------------------------------------------
		Map<Object, Object> commonProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.OSGI_HTTP_PORT_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_HOST_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_PORT_PROP);
		PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_CONTEXTROOT_PROP);

		// get http host
		String host = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_HOST_PROP);
		if (host == null || host.isEmpty()) {
			try {
				InetAddress address = InetAddress.getLocalHost();
				host = address.getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		// get http port
		String port = (String) commonProps.get(OrbitConstants.OSGI_HTTP_PORT_PROP);
		if (port == null || port.isEmpty()) {
			port = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_PORT_PROP);
		}
		if (port == null || port.isEmpty()) {
			port = "80";
		}

		// get http context root
		String contextRoot = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_CONTEXTROOT_PROP);
		if (contextRoot == null || contextRoot.isEmpty()) {
			contextRoot = OrbitConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
		}
		if (!contextRoot.startsWith("/")) {
			contextRoot = "/" + contextRoot;
		}

		// current server URL
		final String serverURL = "http://" + host + ":" + port;

		// -----------------------------------------------------------------------------
		// IndexProvider
		// -----------------------------------------------------------------------------
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		this.indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Start services
		// -----------------------------------------------------------------------------
		// 1. AppStore
		Map<Object, Object> appStoreProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_NAME_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD_PROP);

		String appStoreName = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_NAME_PROP);

		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl(appStoreProps);
		appStoreService.initializeTables();
		appStoreService.start(bundleContext);
		Activator.appStoreService = appStoreService;

		// 2. ConfigRegistry
		Map<Object, Object> configRegistryProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD_PROP);

		String configRegistryName = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME_PROP);

		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl(configRegistryProps);
		configRegistryService.initializeTables();
		configRegistryService.start(bundleContext);
		Activator.configRegistryService = configRegistryService;

		// -----------------------------------------------------------------------------
		// Start web services
		// -----------------------------------------------------------------------------
		// 1. AppStore
		this.appStoreApplication = new AppStoreApplication(bundleContext, contextRoot);
		this.appStoreApplication.start();

		// 2. ConfigRegistry
		this.configRegistryApplication = new ConfigRegistryApplication(bundleContext, contextRoot);
		this.configRegistryApplication.start();

		// -----------------------------------------------------------------------------
		// Start Timers
		// -----------------------------------------------------------------------------
		// 1. AppStore
		this.appStoreServiceTimer = new AppStoreServiceTimer(this.indexProviderLoadBalancer, serverURL, contextRoot, appStoreName);
		this.appStoreServiceTimer.start();

		// 2. ConfigRegistry
		this.configRegistryServiceTimer = new ConfigRegistryServiceTimer(this.indexProviderLoadBalancer, serverURL, contextRoot, configRegistryName);
		this.configRegistryServiceTimer.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = null;

		// -----------------------------------------------------------------------------
		// Stop Timers
		// -----------------------------------------------------------------------------
		// 1. AppStore
		if (this.appStoreServiceTimer != null) {
			this.appStoreServiceTimer.stop();
			this.appStoreServiceTimer = null;
		}

		// 2. ConfigRegistry
		if (this.configRegistryServiceTimer != null) {
			this.configRegistryServiceTimer.stop();
			this.configRegistryServiceTimer = null;
		}

		// -----------------------------------------------------------------------------
		// Stop web services
		// -----------------------------------------------------------------------------
		// 1. AppStore
		if (this.appStoreApplication != null) {
			this.appStoreApplication.stop();
			this.appStoreApplication = null;
		}

		// 2. ConfigRegistry
		if (this.configRegistryApplication != null) {
			this.configRegistryApplication.stop();
			this.configRegistryApplication = null;
		}

		// -----------------------------------------------------------------------------
		// Stop services
		// -----------------------------------------------------------------------------
		// 1. AppStore
		if (Activator.appStoreService != null) {
			Activator.appStoreService.stop();
			Activator.appStoreService = null;
		}

		// 2. ConfigRegistry
		if (Activator.configRegistryService != null) {
			Activator.configRegistryService.stop();
			Activator.configRegistryService = null;
		}
	}

	public static void main(String[] args) {
		String host = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			host = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("host = " + host);
	}

}

// String configRegistryContextRoot = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXTROOT_PROP);
// if (configRegistryContextRoot == null || configRegistryContextRoot.isEmpty()) {
// configRegistryContextRoot = OrbitConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
// }
// if (!configRegistryContextRoot.startsWith("/")) {
// configRegistryContextRoot = "/" + configRegistryContextRoot;
// }

// String appStoreContextRoot = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_CONTEXTROOT_PROP);
// if (appStoreContextRoot == null || appStoreContextRoot.isEmpty()) {
// appStoreContextRoot = OrbitConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
// }
// if (!appStoreContextRoot.startsWith("/")) {
// appStoreContextRoot = "/" + appStoreContextRoot;
// }
