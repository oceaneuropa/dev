package org.orbit.component.server;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.appstore.service.AppStoreService;
import org.orbit.component.server.appstore.service.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.appstore.ws.AppStoreApplication;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;
import org.orbit.component.server.configregistry.service.impl.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.server.configregistry.ws.ConfigRegistryApplication;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	protected static ConfigRegistryServiceDatabaseImpl configRegistryServiceImpl;
	protected static AppStoreServiceDatabaseImpl appStoreServiceImpl;

	public static BundleContext getContext() {
		return context;
	}

	public static ConfigRegistryService getConfigRegistryService() {
		return configRegistryServiceImpl;
	}

	public static AppStoreService getAppStoreService() {
		return appStoreServiceImpl;
	}

	protected ConfigRegistryApplication configRegistryApplication;
	protected AppStoreApplication appStoreApplication;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// IndexProvider
		// -----------------------------------------------------------------------------
		Map<Object, Object> indexServiceProps = new Hashtable<Object, Object>();
		// load properties from accessing index service
		PropertyUtil.loadProperty(bundleContext, indexServiceProps, "indexservice.url");

		// -----------------------------------------------------------------------------
		// App Store
		// -----------------------------------------------------------------------------
		// 1. Start the app store service
		Map<Object, Object> appStoreProps = new Hashtable<Object, Object>();
		// load JDBC connection properties from bundle context
		PropertyUtil.loadProperty(bundleContext, appStoreProps, "appstore.jdbc.driver");
		PropertyUtil.loadProperty(bundleContext, appStoreProps, "appstore.jdbc.url");
		PropertyUtil.loadProperty(bundleContext, appStoreProps, "appstore.jdbc.username");
		PropertyUtil.loadProperty(bundleContext, appStoreProps, "appstore.jdbc.password");

		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl(appStoreProps);
		appStoreService.initializeTables();
		appStoreService.start(bundleContext);
		Activator.appStoreServiceImpl = appStoreService;

		// 2. Start the web application for the app store
		this.appStoreApplication = new AppStoreApplication(bundleContext, "/orbit/v1");
		this.appStoreApplication.start();

		// -----------------------------------------------------------------------------
		// Config Registry
		// -----------------------------------------------------------------------------
		// 1. Start the config registry service
		Map<Object, Object> configRegistryProps = new Hashtable<Object, Object>();
		// load JDBC connection properties from bundle context
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, "configregistry.jdbc.driver");
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, "configregistry.jdbc.url");
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, "configregistry.jdbc.username");
		PropertyUtil.loadProperty(bundleContext, configRegistryProps, "configregistry.jdbc.password");

		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl(configRegistryProps);
		configRegistryService.initializeTables();
		configRegistryService.start(bundleContext);
		Activator.configRegistryServiceImpl = configRegistryService;

		// 2. Start the web service application for the config registry
		this.configRegistryApplication = new ConfigRegistryApplication(bundleContext, "/orbit/v1");
		this.configRegistryApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// -----------------------------------------------------------------------------
		// Config Registry
		// -----------------------------------------------------------------------------
		// 1. Stop the web service application for the config registry
		if (this.configRegistryApplication != null) {
			this.configRegistryApplication.stop();
			this.configRegistryApplication = null;
		}

		// 2. Stop the config registry service
		if (Activator.configRegistryServiceImpl != null) {
			Activator.configRegistryServiceImpl.stop();
			Activator.configRegistryServiceImpl = null;
		}

		// -----------------------------------------------------------------------------
		// App Store
		// -----------------------------------------------------------------------------
		// 1. Stop the web application for the app store
		if (this.appStoreApplication != null) {
			this.appStoreApplication.stop();
			this.appStoreApplication = null;
		}

		// 2. Stop the app store service
		if (Activator.appStoreServiceImpl != null) {
			Activator.appStoreServiceImpl.stop();
			Activator.appStoreServiceImpl = null;
		}
	}

}
