package org.orbit.component.runtime.tier1.config.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryServiceDatabaseImpl implements ConfigRegistryService {

	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<String, ConfigRegistry> userIdToRegistryMap = new HashMap<String, ConfigRegistry>();

	/**
	 * 
	 * @param props
	 */
	public ConfigRegistryServiceDatabaseImpl() {
	}

	/**
	 * Start ConfigRegistryService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);

		updateProperties(configProps);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(ConfigRegistryService.class, this, props);
	}

	/**
	 * Stop ConfigRegistryService
	 * 
	 */
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");

		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		String hostURL = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		String contextRoot = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		String jdbcUsername = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);

		System.out.println();
		System.out.println("Config properties:");
		System.out.println("-----------------------------------------------------");
		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL + " = " + jdbcURL);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD + " = " + jdbcPassword);
		System.out.println("-----------------------------------------------------");
		System.out.println();

		this.configProps = configProps;
		this.databaseProperties = getConnectionProperties(this.configProps);

		for (Iterator<Entry<String, ConfigRegistry>> itor = this.userIdToRegistryMap.entrySet().iterator(); itor.hasNext();) {
			Entry<String, ConfigRegistry> entry = itor.next();
			ConfigRegistry configRegistry = entry.getValue();
			if (configRegistry instanceof ConfigRegistryDatabaseImpl) {
				((ConfigRegistryDatabaseImpl) configRegistry).update(this.databaseProperties);
			}
		}
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		String url = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		String username = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		String password = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		try {
			DatabaseUtil.initialize(conn, ConfigRegistryPathTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, ConfigRegistryPropertyTableHandler.INSTANCE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getName() {
		String name = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.configProps.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.configProps.get(OrbitConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public synchronized ConfigRegistry getRegistry(String userId) {
		ConfigRegistry configRegistry = this.userIdToRegistryMap.get(userId);
		if (configRegistry == null) {
			configRegistry = new ConfigRegistryDatabaseImpl(userId, this.databaseProperties);
			this.userIdToRegistryMap.put(userId, configRegistry);
		}
		return configRegistry;
	}

}
