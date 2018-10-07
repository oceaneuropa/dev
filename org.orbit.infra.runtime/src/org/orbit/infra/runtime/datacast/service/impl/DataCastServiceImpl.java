package org.orbit.infra.runtime.datacast.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DataCastServiceImpl implements DataCastService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param initProperties
	 */
	public DataCastServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DataCastService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__ID);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_PASSWORD);

		updateProperties(properties);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DataCastService.class, this, props);
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
		String globalHostURL = (String) configProps.get(InfraConstants.ORBIT_HOST_URL);
		String id = (String) configProps.get(InfraConstants.DATACAST__ID);
		String name = (String) configProps.get(InfraConstants.DATACAST__NAME);
		String hostURL = (String) configProps.get(InfraConstants.DATACAST__HOST_URL);
		String contextRoot = (String) configProps.get(InfraConstants.DATACAST__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(InfraConstants.DATACAST__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(InfraConstants.DATACAST__JDBC_URL);
		String jdbcUsername = (String) configProps.get(InfraConstants.DATACAST__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(InfraConstants.DATACAST__JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(InfraConstants.DATACAST__ID + " = " + id);
			System.out.println(InfraConstants.DATACAST__NAME + " = " + name);
			System.out.println(InfraConstants.DATACAST__HOST_URL + " = " + hostURL);
			System.out.println(InfraConstants.DATACAST__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(InfraConstants.DATACAST__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(InfraConstants.DATACAST__JDBC_URL + " = " + jdbcURL);
			System.out.println(InfraConstants.DATACAST__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(InfraConstants.DATACAST__JDBC_PASSWORD + " = " + jdbcPassword);
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
		String driver = (String) this.properties.get(InfraConstants.DATACAST__JDBC_DRIVER);
		String url = (String) this.properties.get(InfraConstants.DATACAST__JDBC_URL);
		String username = (String) this.properties.get(InfraConstants.DATACAST__JDBC_USERNAME);
		String password = (String) this.properties.get(InfraConstants.DATACAST__JDBC_PASSWORD);
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
	public String getDataCastId() {
		String dataCastId = (String) this.properties.get(InfraConstants.DATACAST__ID);
		return dataCastId;
	}

	@Override
	public String getName() {
		return (String) this.properties.get(InfraConstants.DATACAST__NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(InfraConstants.DATACAST__HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return (String) this.properties.get(InfraConstants.DATACAST__CONTEXT_ROOT);
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

}
