package org.orbit.component.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.appstore.service.AppStoreService;
import org.orbit.component.server.appstore.service.impl.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.appstore.ws.AppStoreApplication;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;
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
	protected AppStoreApplication appStoreApp;
	protected ConfigRegistryApplication configRegistryApp;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		System.out.println("org.orbit.component.server.Activator.start()");

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

		// current host URL
		String hostURL = "http://" + host + ":" + port;
		System.out.println("hostURL = " + hostURL);

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
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME_PROP);
		PropertyUtil.loadProperty(bundleContext, appStoreProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD_PROP);

		String appStoreName = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_NAME_PROP);
		String appStoreContextRoot = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT_PROP);
		String appStoreJdbcDriver = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER_PROP);
		String appStoreJdbcURL = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_URL_PROP);
		String appStoreJdbcUsername = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME_PROP);
		String appStoreJdbcPassword = (String) appStoreProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD_PROP);
		System.out.println("appStoreName = " + appStoreName);
		System.out.println("appStoreContextRoot = " + appStoreContextRoot);
		System.out.println("appStoreJdbcDriver = " + appStoreJdbcDriver);
		System.out.println("appStoreJdbcURL = " + appStoreJdbcURL);
		System.out.println("appStoreJdbcUsername = " + appStoreJdbcUsername);
		System.out.println("appStoreJdbcPassword = " + appStoreJdbcPassword);

		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl(appStoreProps);
		appStoreService.initializeTables();
		appStoreService.start(bundleContext);
		Activator.appStoreService = appStoreService;

		// 2. ConfigRegistry
		Map<Object, Object> configRegistryProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME_PROP);
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD_PROP);

		String configRegistryName = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME_PROP);
		String configRegistryContextRoot = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT_PROP);
		String configRegistryJdbcDriver = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER_PROP);
		String configRegistryJdbcURL = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL_PROP);
		String configRegistryJdbcUsername = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME_PROP);
		String configRegistryJdbcPassword = (String) configRegistryProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD_PROP);
		System.out.println("configRegistryName = " + configRegistryName);
		System.out.println("configRegistryContextRoot = " + configRegistryContextRoot);
		System.out.println("configRegistryJdbcDriver = " + configRegistryJdbcDriver);
		System.out.println("configRegistryJdbcURL = " + configRegistryJdbcURL);
		System.out.println("configRegistryJdbcUsername = " + configRegistryJdbcUsername);
		System.out.println("configRegistryJdbcPassword = " + configRegistryJdbcPassword);

		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl(configRegistryProps);
		configRegistryService.initializeTables();
		configRegistryService.start(bundleContext);
		Activator.configRegistryService = configRegistryService;

		// -----------------------------------------------------------------------------
		// Start web applications
		// -----------------------------------------------------------------------------
		// 1. AppStore
		this.appStoreApp = new AppStoreApplication();
		this.appStoreApp.setBundleContext(bundleContext);
		this.appStoreApp.setHostURL(hostURL);
		this.appStoreApp.setContextRoot(appStoreContextRoot);
		this.appStoreApp.setComponentName(appStoreName);
		this.appStoreApp.setIndexProviderLoadBalancer(this.indexProviderLoadBalancer);
		this.appStoreApp.start();

		// 2. ConfigRegistry
		this.configRegistryApp = new ConfigRegistryApplication();
		this.configRegistryApp.setBundleContext(bundleContext);
		this.configRegistryApp.setHostURL(hostURL);
		this.configRegistryApp.setContextRoot(configRegistryContextRoot);
		this.configRegistryApp.setComponentName(configRegistryName);
		this.configRegistryApp.setIndexProviderLoadBalancer(this.indexProviderLoadBalancer);
		this.configRegistryApp.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = null;

		// -----------------------------------------------------------------------------
		// Stop web applications
		// -----------------------------------------------------------------------------
		// 1. AppStore
		if (this.appStoreApp != null) {
			this.appStoreApp.stop();
			this.appStoreApp = null;
		}

		// 2. ConfigRegistry
		if (this.configRegistryApp != null) {
			this.configRegistryApp.stop();
			this.configRegistryApp = null;
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

// get http context root
// String contextRoot = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_CONTEXTROOT_PROP);
// if (contextRoot == null || contextRoot.isEmpty()) {
// contextRoot = OrbitConstants.ORBIT_DEFAULT_CONTEXT_ROOT;
// }
// if (!contextRoot.startsWith("/")) {
// contextRoot = "/" + contextRoot;
// }
// if (appStoreContextRoot == null || appStoreContextRoot.isEmpty()) {
// appStoreContextRoot = "/orbit/v1/appstore";
// }
// if (configContextRoot == null || configContextRoot.isEmpty()) {
// configContextRoot = "/orbit/v1/configregistry";
// }
