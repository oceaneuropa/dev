package org.orbit.os.server.service.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.os.server.Constants;
import org.orbit.os.server.service.AppsManager;
import org.orbit.os.server.service.NodeOS;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class NodeOSImpl implements NodeOS {

	public static final String NODE_OS_NAME = "NodeOS";
	public static final String NODE_OS_VERSION = "1.0.0";

	protected BundleContext bundleContext;
	protected Properties configIniProps;
	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	protected AppsManagerImpl appsManager;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 * @param configIniProps
	 */
	public NodeOSImpl(BundleContext bundleContext, Properties configIniProps) {
		this.bundleContext = bundleContext;
		this.configIniProps = configIniProps;

		this.appsManager = new AppsManagerImpl();
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	@Override
	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");

		if (isStarted()) {
			System.out.println("NodeOS (" + getOSName() + ":" + getOSVersion() + ") '" + getName() + "' is already started.");
			return;
		}
		this.isStarted.set(true);

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_NAME);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_HOST_URL);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_CONTEXT_ROOT);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_JDBC_DRIVER);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_JDBC_URL);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_JDBC_USERNAME);
		PropertyUtil.loadProperty(this.bundleContext, configProps, Constants.COMPONENT_NODE_JDBC_PASSWORD);

		updateProperties(configProps);

		// Register NodeOS
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = this.bundleContext.registerService(NodeOS.class, this, props);
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Unregister NodeOS
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	protected String getProperty(Object key) {
		return getProperty(key, String.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(Object key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.configProps.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		// If config properties cannot be found, read from config.ini file
		if (String.class.equals(valueClass)) {
			String value = this.configIniProps.getProperty(key.toString());
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	@Override
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = getProperty(Constants.ORBIT_HOST_URL);
		String name = getProperty(Constants.COMPONENT_NODE_NAME);
		String hostURL = getProperty(Constants.COMPONENT_NODE_HOST_URL);
		String contextRoot = getProperty(Constants.COMPONENT_NODE_CONTEXT_ROOT);
		String jdbcDriver = getProperty(Constants.COMPONENT_NODE_JDBC_DRIVER);
		String jdbcURL = getProperty(Constants.COMPONENT_NODE_JDBC_URL);
		String jdbcUsername = getProperty(Constants.COMPONENT_NODE_JDBC_USERNAME);
		String jdbcPassword = getProperty(Constants.COMPONENT_NODE_JDBC_PASSWORD);

		System.out.println(Constants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(Constants.COMPONENT_NODE_NAME + " = " + name);
		System.out.println(Constants.COMPONENT_NODE_HOST_URL + " = " + hostURL);
		System.out.println(Constants.COMPONENT_NODE_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(Constants.COMPONENT_NODE_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(Constants.COMPONENT_NODE_JDBC_URL + " = " + jdbcURL);
		System.out.println(Constants.COMPONENT_NODE_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(Constants.COMPONENT_NODE_JDBC_PASSWORD + " = " + jdbcPassword);

		this.configProps = configProps;
		this.databaseProperties = getConnectionProperties();
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties() {
		String driver = getProperty(Constants.COMPONENT_NODE_JDBC_DRIVER);
		String url = getProperty(Constants.COMPONENT_NODE_JDBC_URL);
		String username = getProperty(Constants.COMPONENT_NODE_JDBC_USERNAME);
		String password = getProperty(Constants.COMPONENT_NODE_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getOSName() {
		return NODE_OS_NAME;
	}

	@Override
	public String getOSVersion() {
		return NODE_OS_VERSION;
	}

	@Override
	public String getName() {
		String name = getProperty(Constants.COMPONENT_NODE_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(Constants.COMPONENT_NODE_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = getProperty(Constants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(Constants.COMPONENT_NODE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public AppsManager getAppsManager() {
		return this.appsManager;
	}

}

// protected Framework framework;

// public Framework getFramework() {
// return this.framework;
// }

// Map<String, String> configuration = new HashMap<String, String>();
// this.framework = FrameworkUtil.createFramework(configuration);
// try {
// this.framework.start();
// } catch (BundleException e) {
// e.printStackTrace();
// }

// if (this.framework != null) {
// try {
// this.framework.stop();
// } catch (BundleException e) {
// e.printStackTrace();
// }
// this.framework = null;
// }