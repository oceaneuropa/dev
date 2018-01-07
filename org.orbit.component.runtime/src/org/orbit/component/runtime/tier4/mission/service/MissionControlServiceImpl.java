package org.orbit.component.runtime.tier4.mission.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.transferagent.util.TASetupUtil;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissionControlServiceImpl implements MissionControlService {

	protected static Logger LOG = LoggerFactory.getLogger(MissionControlServiceImpl.class);

	protected WSEditPolicies wsEditPolicies;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;

	public MissionControlServiceImpl() {
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(MissionControlService.class, this);
	}

	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		TASetupUtil.loadConfigIniProperties(bundleContext, configProps);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD);

		update(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(MissionControlService.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<Object, Object> properties) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
		String hostURL = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL);
		String jdbcUsername = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD);

		System.out.println();
		System.out.println("Config properties:");
		System.out.println("-----------------------------------------------------");
		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL + " = " + jdbcURL);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD + " = " + jdbcPassword);
		System.out.println("-----------------------------------------------------");
		System.out.println();

		this.databaseProperties = DatabaseUtil.getProperties(jdbcDriver, jdbcURL, jdbcUsername, jdbcPassword);

		// Initialize database tables.
		Connection conn = getConnection();
		try {
			// DatabaseUtil.initialize(conn, MachineConfigTableHandler.INSTANCE);
			// DatabaseUtil.initialize(conn, TransferAgentConfigTableHandler.INSTANCE);
			// DatabaseUtil.initialize(conn, NodeConfigTableHandler.INSTANCE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

}
