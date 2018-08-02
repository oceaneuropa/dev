package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.model.missioncontrol.Mission;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissionControlServiceImpl implements MissionControlService, ConnectionAware, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(MissionControlServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected WSEditPolicies wsEditPolicies;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected MissionPersistenceHandler persistenceHandler;

	public MissionControlServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(MissionControlService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}
		// Service properties
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(MissionControlService.class, this, props);
	}

	@Override
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
		// System.out.println(getClass().getSimpleName() + ".updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String jdbcDriver = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL);
		String jdbcUsername = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD);

		this.databaseProperties = DatabaseUtil.getProperties(jdbcDriver, jdbcURL, jdbcUsername, jdbcPassword);

		this.persistenceHandler = new MissionPersistenceHandlerDatabaseImpl(this);
	}

	@Override
	public Connection getConnection() {
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

	/**
	 * 
	 * @param e
	 * @throws IndexServiceException
	 */
	protected void handleException(Exception e) throws ServerException {
		e.printStackTrace();
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	protected MissionPersistenceHandler getPersistenceHandler() {
		return this.persistenceHandler;
	}

	@Override
	public List<Mission> getMissions(String typeId) throws ServerException {
		List<Mission> missions = null;
		try {
			missions = getPersistenceHandler().getMissions(typeId);
		} catch (Exception e) {
			handleException(e);
		}
		if (missions == null) {
			missions = new ArrayList<Mission>();
		}
		return missions;
	}

	@Override
	public Mission getMission(String typeId, String name) throws ServerException {
		Mission mission = null;
		try {
			mission = getPersistenceHandler().getMission(typeId, name);

		} catch (Exception e) {
			handleException(e);
		}
		return mission;
	}

	@Override
	public Mission createMission(String typeId, String name) throws ServerException {
		Mission mission = null;
		try {
			// 1. Check and create mission record
			boolean nameExists = getPersistenceHandler().nameExists(typeId, name);
			if (nameExists) {
				throw new ServerException("404", "Mission with name '" + name + "' already exists.");
			}

			mission = getPersistenceHandler().insert(typeId, name);
			if (mission == null) {
				throw new ServerException("404", "Mission cannot be created.");
			}

			// 2. TA - Node -> OS -> create mission instance.
			// - Determine which Node (and its OS) to use
			// - OS should be able to support the type and create mission instance in there.
			// - If OS failed, the mission control should throw exception as well.
			// - If OS succeed, need more information
			// ---- (1) URL for accessing the task from the OS
			// ---- (2) URL for broadcasting the OS data.

		} catch (Exception e) {
			handleException(e);
		}
		return mission;
	}

	@Override
	public boolean deleteMission(String typeId, String name) throws ServerException {
		boolean succeed = false;
		try {
			succeed = getPersistenceHandler().delete(typeId, name);

		} catch (Exception e) {
			handleException(e);
		}
		return succeed;
	}

	@Override
	public boolean startMission(String typeId, String name) throws ServerException {
		return false;
	}

	@Override
	public boolean stopMission(String typeId, String name) throws ServerException {
		return false;
	}

}

// String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
// String name = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
// String hostURL = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT);

// System.out.println();
// System.out.println("Config properties:");
// System.out.println("-----------------------------------------------------");
// System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME + " = " + name);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_HOST_URL + " = " + hostURL);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_CONTEXT_ROOT + " = " + contextRoot);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_DRIVER + " = " + jdbcDriver);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_URL + " = " + jdbcURL);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_USERNAME + " = " + jdbcUsername);
// System.out.println(OrbitConstants.COMPONENT_MISSION_CONTROL_JDBC_PASSWORD + " = " + jdbcPassword);
// System.out.println("-----------------------------------------------------");
// System.out.println();

//// Initialize database tables.
// Connection conn = getConnection();
// try {
// // DatabaseUtil.initialize(conn, MachineConfigTableHandler.INSTANCE);
// // DatabaseUtil.initialize(conn, TransferAgentConfigTableHandler.INSTANCE);
// // DatabaseUtil.initialize(conn, NodeConfigTableHandler.INSTANCE);
// } catch (Exception e) {
// e.printStackTrace();
// } finally {
// DatabaseUtil.closeQuietly(conn, true);
// }
