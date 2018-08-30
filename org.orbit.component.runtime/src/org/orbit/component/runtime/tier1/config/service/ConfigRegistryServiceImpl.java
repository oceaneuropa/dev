package org.orbit.component.runtime.tier1.config.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.orbit.component.runtime.ComponentConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryServiceImpl implements ConfigRegistryService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<String, ConfigRegistry> accountIdToRegistryMap = new HashMap<String, ConfigRegistry>();

	/**
	 * 
	 * @param initProperties
	 */
	public ConfigRegistryServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	/**
	 * Start ConfigRegistryService
	 * 
	 */
	@Override
	public void start(BundleContext bundleContext) {
		// System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);

		updateProperties(properties);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(ConfigRegistryService.class, this, props);
	}

	/**
	 * Stop ConfigRegistryService
	 * 
	 */
	@Override
	public void stop(BundleContext bundleContext) {
		// System.out.println(getClass().getSimpleName() + ".stop()");

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
		// System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(ComponentConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		String hostURL = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		String contextRoot = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		String jdbcUsername = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(ComponentConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_NAME + " = " + name);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL + " = " + hostURL);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL + " = " + jdbcURL);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = configProps;
		this.databaseProperties = getConnectionProperties(this.properties);

		for (Iterator<Entry<String, ConfigRegistry>> itor = this.accountIdToRegistryMap.entrySet().iterator(); itor.hasNext();) {
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
		String driver = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER);
		String url = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_URL);
		String username = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME);
		String password = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 * 
	 * @throws SQLException
	 */
	public void initialize() {
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);
			DatabaseUtil.initialize(conn, ConfigRegistryPathTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, ConfigRegistryPropertyTableHandler.INSTANCE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(ComponentConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(ComponentConstants.COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public synchronized ConfigRegistry getRegistry(String accountId) {
		ConfigRegistry configRegistry = this.accountIdToRegistryMap.get(accountId);
		if (configRegistry == null) {
			configRegistry = new ConfigRegistryDatabaseImpl(accountId, this.databaseProperties);
			this.accountIdToRegistryMap.put(accountId, configRegistry);
		}
		return configRegistry;
	}

}
