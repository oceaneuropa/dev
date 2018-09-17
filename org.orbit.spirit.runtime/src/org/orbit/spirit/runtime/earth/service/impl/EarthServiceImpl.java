package org.orbit.spirit.runtime.earth.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.api.InfraConstants;
import org.orbit.spirit.runtime.SpiritConstants;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.service.World;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class EarthServiceImpl implements EarthService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param initProperties
	 */
	public EarthServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(EarthService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__GAIA_ID);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__ID);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_PASSWORD);

		updateProperties(properties);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(EarthService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String indexServiceUrl = (String) configProps.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String globalHostURL = (String) configProps.get(SpiritConstants.ORBIT_HOST_URL);
		String gaiaId = (String) configProps.get(SpiritConstants.EARTH__GAIA_ID);
		String id = (String) configProps.get(SpiritConstants.EARTH__ID);
		String name = (String) configProps.get(SpiritConstants.EARTH__NAME);
		String hostURL = (String) configProps.get(SpiritConstants.EARTH__HOST_URL);
		String contextRoot = (String) configProps.get(SpiritConstants.EARTH__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(SpiritConstants.EARTH__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(SpiritConstants.EARTH__JDBC_URL);
		String jdbcUsername = (String) configProps.get(SpiritConstants.EARTH__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(SpiritConstants.EARTH__JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(SpiritConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(SpiritConstants.EARTH__GAIA_ID + " = " + gaiaId);
			System.out.println(SpiritConstants.EARTH__ID + " = " + id);
			System.out.println(SpiritConstants.EARTH__NAME + " = " + name);
			System.out.println(SpiritConstants.EARTH__HOST_URL + " = " + hostURL);
			System.out.println(SpiritConstants.EARTH__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(SpiritConstants.EARTH__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(SpiritConstants.EARTH__JDBC_URL + " = " + jdbcURL);
			System.out.println(SpiritConstants.EARTH__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(SpiritConstants.EARTH__JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = configProps;
		this.databaseProperties = getConnectionProperties(this.properties);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.properties.get(SpiritConstants.EARTH__JDBC_DRIVER);
		String url = (String) this.properties.get(SpiritConstants.EARTH__JDBC_URL);
		String username = (String) this.properties.get(SpiritConstants.EARTH__JDBC_USERNAME);
		String password = (String) this.properties.get(SpiritConstants.EARTH__JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);

			// if (this.categoryTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.categoryTableHandler);
			// }
			// if (this.appTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.appTableHandler);
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getGaiaId() {
		String gaiaId = (String) this.properties.get(SpiritConstants.EARTH__GAIA_ID);
		return gaiaId;
	}

	@Override
	public String getEarthId() {
		String earthId = (String) this.properties.get(SpiritConstants.EARTH__ID);
		return earthId;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(SpiritConstants.EARTH__NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(SpiritConstants.EARTH__HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(SpiritConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(SpiritConstants.EARTH__CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public World[] getWorlds() throws ServerException {
		return null;
	}

	@Override
	public boolean worldExists(String name) throws ServerException {
		return false;
	}

	@Override
	public World getWorld(String name) throws ServerException {
		return null;
	}

	@Override
	public World createWorld(String name) throws ServerException {
		return null;
	}

	@Override
	public boolean deleteWorld(String name) throws ServerException {
		return false;
	}

	@Override
	public boolean join(World world, String accountId) throws ServerException {
		return false;
	}

	@Override
	public boolean leave(World world, String accountId) throws ServerException {
		return false;
	}

}

// this.accountIdToFileSystemMap.clear();
