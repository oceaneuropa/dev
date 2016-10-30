package org.orbit.component.server.configregistry.service.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.orbit.component.server.configregistry.handler.ConfigRegistryPathTableHandler;
import org.orbit.component.server.configregistry.handler.ConfigRegistryPropertyTableHandler;
import org.orbit.component.server.configregistry.service.ConfigRegistry;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;
import org.origin.common.jdbc.DatabaseUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryServiceDatabaseImpl implements ConfigRegistryService {

	protected Map<Object, Object> props;
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceReg;
	protected Map<String, ConfigRegistry> userIdToRegistryMap = new HashMap<String, ConfigRegistry>();

	/**
	 * 
	 * @param props
	 */
	public ConfigRegistryServiceDatabaseImpl(Map<Object, Object> props) {
		assert (props != null) : "props is null";
		this.props = props;
		this.databaseProperties = getConnectionProperties(this.props);
	}

	/**
	 * Initialize database tables.
	 */
	public void initializeTables() {
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

	/**
	 * 
	 * @param props
	 */
	public synchronized void update(Map<Object, Object> props) {
		assert (props != null) : "props is null";
		this.props = props;
		this.databaseProperties = getConnectionProperties(this.props);

		for (Iterator<Entry<String, ConfigRegistry>> itor = userIdToRegistryMap.entrySet().iterator(); itor.hasNext();) {
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
		String driver = (String) this.props.get("configregistry.jdbc.driver");
		String url = (String) this.props.get("configregistry.jdbc.url");
		String username = (String) this.props.get("configregistry.jdbc.username");
		String password = (String) this.props.get("configregistry.jdbc.password");
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Start ConfigRegistryService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = bundleContext.registerService(ConfigRegistryService.class, this, props);
	}

	/**
	 * Stop ConfigRegistryService
	 * 
	 */
	public void stop() {
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
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
