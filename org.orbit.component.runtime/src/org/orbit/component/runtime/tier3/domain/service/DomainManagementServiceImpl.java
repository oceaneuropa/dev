package org.orbit.component.runtime.tier3.domain.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.model.domain.PlatformConfig;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.resources.IWorkspaceService;
import org.origin.common.resources.WorkspaceServiceFactory;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.StringUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DomainManagementServiceImpl implements DomainManagementService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected Properties workspaceProperties;
	protected ServiceRegistration<?> serviceRegistry;

	protected MachineConfigTableHandler machineConfigTableHandler = MachineConfigTableHandler.INSTANCE;
	protected PlatformConfigTableHandler platformConfigTableHandler = PlatformConfigTableHandler.INSTANCE;
	protected NodeConfigTableHandler nodeConfigTableHandler = NodeConfigTableHandler.INSTANCE;
	protected IWorkspaceService workspaceService;

	public DomainManagementServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	@Override
	public void start(BundleContext bundleContext) {
		// System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD);

		update(properties);

		// Start service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DomainManagementService.class, this, props);

		// Start workspace service
		// DatabaseFileSystemConfig config = new DatabaseFileSystemConfig(this.databaseProperties);
		// FileSystem fileSystem = new DatabaseFileSystem(config);
		// this.workspaceService = WorkspaceServiceFactory.getInstance().createWorkspaceService(fileSystem);
		this.workspaceService = WorkspaceServiceFactory.getInstance().createWorkspaceService(new File("/path-to-workspace-folder"));

		try {
			this.workspaceService.start(bundleContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext bundleContext) {
		// System.out.println(getClass().getSimpleName() + ".stop()");

		// Stop service
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		// Stop workspace service
		if (this.workspaceService != null) {
			try {
				this.workspaceService.stop(bundleContext);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.workspaceService = null;
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

		String globalHostURL = (String) properties.get(ComponentConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		String hostURL = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
		String contextRoot = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL);
		String jdbcUsername = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(ComponentConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME + " = " + name);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL + " = " + hostURL);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL + " = " + jdbcURL);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.databaseProperties = DatabaseUtil.getProperties(jdbcDriver, jdbcURL, jdbcUsername, jdbcPassword);
		// Initialize database tables.
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);
			DatabaseUtil.initialize(conn, MachineConfigTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, PlatformConfigTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, NodeConfigTableHandler.INSTANCE);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		// String tableNamePrefix = namespace.replaceAll("\\.", "_");
		this.workspaceProperties = new Properties();
		this.workspaceProperties.putAll(this.databaseProperties);
		// this.workspaceProperties.put(Constants.METADATA_TABLE_NAME, tableNamePrefix + "_" + Constants.METADATA_TABLE_NAME_DEFAULT_VALUE);
		// this.workspaceProperties.put(Constants.CONTENT_TABLE_NAME, tableNamePrefix + "_" + Constants.CONTENT_TABLE_NAME_DEFAULT_VALUE);
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		return (String) this.properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
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
		return (String) this.properties.get(ComponentConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
	}

	/**
	 * 
	 * @param machineId
	 * @throws ServerException
	 */
	protected void checkMachineId(String machineId) throws ServerException {
		if (machineId == null || machineId.isEmpty()) {
			throw new ServerException("400", "machineId is empty.");
		}
	}

	/**
	 * 
	 * @param transferAgentId
	 * @throws ServerException
	 */
	protected void checkTransferAgentId(String transferAgentId) throws ServerException {
		if (transferAgentId == null || transferAgentId.isEmpty()) {
			throw new ServerException("400", "transferAgentId is empty.");
		}
	}

	/**
	 * 
	 * @param nodeId
	 * @throws ServerException
	 */
	protected void checkNodeId(String nodeId) throws ServerException {
		if (nodeId == null || nodeId.isEmpty()) {
			throw new ServerException("400", "nodeId is empty.");
		}
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws ServerException {
		e.printStackTrace();
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	@Override
	public List<MachineConfig> getMachineConfigs() throws ServerException {
		Connection conn = null;
		try {
			conn = getConnection();
			return this.machineConfigTableHandler.getMachineConfigs(conn);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public MachineConfig getMachineConfig(String machineId) throws ServerException {
		checkMachineId(machineId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.machineConfigTableHandler.getMachineConfig(conn, machineId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean machineConfigExists(String machineId) throws ServerException {
		checkMachineId(machineId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.machineConfigTableHandler.exists(conn, machineId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addMachineConfig(MachineConfig addMachineRequest) throws ServerException {
		String id = addMachineRequest.getId();
		String name = addMachineRequest.getName();
		String ipAddress = addMachineRequest.getIpAddress();

		checkMachineId(id);

		Connection conn = null;
		try {
			conn = getConnection();
			MachineConfig newMachineConfig = this.machineConfigTableHandler.addMachineConfig(conn, id, name, ipAddress);
			if (newMachineConfig != null) {
				return true;
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean updateMachineConfig(MachineConfig updateMachineRequest, List<String> fieldsToUpdate) throws ServerException {
		String id = updateMachineRequest.getId();
		String newName = updateMachineRequest.getName();
		String newIpAddress = updateMachineRequest.getIpAddress();
		if (fieldsToUpdate == null) {
			fieldsToUpdate = Collections.emptyList();
		}

		checkMachineId(id);

		boolean isUpdated = false;

		Connection conn = null;
		try {
			conn = getConnection();
			MachineConfig machineConfig = this.machineConfigTableHandler.getMachineConfig(conn, id);
			if (machineConfig == null) {
				throw new ServerException("404", "Machine with id '" + id + "' is not found.");
			}

			String oldName = machineConfig.getName();
			String oldIpAddress = machineConfig.getIpAddress();

			if (fieldsToUpdate.contains("name")) {
				boolean needToUpdate = (!StringUtil.equals(newName, oldName)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.machineConfigTableHandler.updateName(conn, id, newName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("ipAddress")) {
				boolean needToUpdate = (!StringUtil.equals(newIpAddress, oldIpAddress)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.machineConfigTableHandler.updateIpAddress(conn, id, newIpAddress);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return isUpdated;
	}

	@Override
	public boolean deleteMachineConfig(String machineId) throws ServerException {
		checkMachineId(machineId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.machineConfigTableHandler.delete(conn, machineId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	@Override
	public List<PlatformConfig> getPlatformConfigs(String machineId) throws ServerException {
		checkMachineId(machineId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.platformConfigTableHandler.getPlatformConfigs(conn, machineId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public PlatformConfig getPlatformConfig(String machineId, String transferAgentId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.platformConfigTableHandler.getPlatformConfig(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean platformConfigExists(String machineId, String transferAgentId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.platformConfigTableHandler.exists(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addPlatformConfig(String machineId, PlatformConfig addTransferAgentRequest) throws ServerException {
		String id = addTransferAgentRequest.getId();
		String name = addTransferAgentRequest.getName();
		String home = addTransferAgentRequest.getHome();
		String hostURL = addTransferAgentRequest.getHostURL();
		String contextRoot = addTransferAgentRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(id);

		Connection conn = null;
		try {
			conn = getConnection();
			PlatformConfig newTransferAgentConfig = this.platformConfigTableHandler.add(conn, machineId, id, name, home, hostURL, contextRoot);
			if (newTransferAgentConfig != null) {
				return true;
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean updatePlatformConfig(String machineId, PlatformConfig updateTransferAgentRequest, List<String> fieldsToUpdate) throws ServerException {
		String id = updateTransferAgentRequest.getId();
		String newName = updateTransferAgentRequest.getName();
		String newHome = updateTransferAgentRequest.getHome();
		String newHostURL = updateTransferAgentRequest.getHostURL();
		String newContextRoot = updateTransferAgentRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(id);

		boolean isUpdated = false;

		Connection conn = null;
		try {
			conn = getConnection();
			PlatformConfig transferAgentConfig = this.platformConfigTableHandler.getPlatformConfig(conn, machineId, id);
			if (transferAgentConfig == null) {
				throw new ServerException("404", "TransferAgent with id '" + id + "' is not found.");
			}

			String oldName = transferAgentConfig.getName();
			String oldHome = transferAgentConfig.getHome();
			String oldHostURL = transferAgentConfig.getHostURL();
			String oldContextRoot = transferAgentConfig.getContextRoot();

			if (fieldsToUpdate.contains("name")) {
				boolean needToUpdate = (!StringUtil.equals(newName, oldName)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.platformConfigTableHandler.updateName(conn, machineId, id, newName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("home")) {
				boolean needToUpdate = (!StringUtil.equals(newHome, oldHome)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.platformConfigTableHandler.updateHome(conn, machineId, id, newHome);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("hostUrl")) {
				boolean needToUpdate = (!StringUtil.equals(newHostURL, oldHostURL)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.platformConfigTableHandler.updateHostURL(conn, machineId, id, newHostURL);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("contextRoot")) {
				boolean needToUpdate = (!StringUtil.equals(newContextRoot, oldContextRoot)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.platformConfigTableHandler.updateContextRoot(conn, machineId, id, newContextRoot);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return isUpdated;
	}

	@Override
	public boolean deletePlatformConfig(String machineId, String transferAgentId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.platformConfigTableHandler.delete(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	@Override
	public List<NodeConfig> getNodeConfigs(String machineId, String transferAgentId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.nodeConfigTableHandler.getNodeConfigs(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public NodeConfig getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.nodeConfigTableHandler.getNodeConfig(conn, machineId, transferAgentId, nodeId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean nodeConfigExists(String machineId, String transferAgentId, String nodeId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.nodeConfigTableHandler.exists(conn, machineId, transferAgentId, nodeId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addNodeConfig(String machineId, String transferAgentId, NodeConfig addNodeRequest) throws ServerException {
		String id = addNodeRequest.getId();
		String name = addNodeRequest.getName();
		String home = addNodeRequest.getHome();
		String hostURL = addNodeRequest.getHostURL();
		String contextRoot = addNodeRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(id);

		Connection conn = null;
		try {
			conn = getConnection();
			NodeConfig newNodeConfig = this.nodeConfigTableHandler.add(conn, machineId, transferAgentId, id, name, home, hostURL, contextRoot);
			if (newNodeConfig != null) {
				return true;
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean updateNodeConfig(String machineId, String transferAgentId, NodeConfig updateNodeRequest, List<String> fieldsToUpdate) throws ServerException {
		String id = updateNodeRequest.getId();
		String newName = updateNodeRequest.getName();
		String newHome = updateNodeRequest.getHome();
		String newHostURL = updateNodeRequest.getHostURL();
		String newContextRoot = updateNodeRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(id);

		boolean isUpdated = false;

		Connection conn = null;
		try {
			conn = getConnection();
			NodeConfig nodeConfig = this.nodeConfigTableHandler.getNodeConfig(conn, machineId, transferAgentId, id);
			if (nodeConfig == null) {
				throw new ServerException("404", "NodeAgent with id '" + id + "' is not found.");
			}

			String oldName = nodeConfig.getName();
			String oldHome = nodeConfig.getHome();
			String oldHostURL = nodeConfig.getHostURL();
			String oldContextRoot = nodeConfig.getContextRoot();

			if (fieldsToUpdate.contains("name")) {
				boolean needToUpdate = (!StringUtil.equals(newName, oldName)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.nodeConfigTableHandler.updateName(conn, machineId, transferAgentId, id, newName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("home")) {
				boolean needToUpdate = (!StringUtil.equals(newHome, oldHome)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.nodeConfigTableHandler.updateHome(conn, machineId, transferAgentId, id, newHome);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("hostURL")) {
				boolean needToUpdate = (!StringUtil.equals(newHostURL, oldHostURL)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.nodeConfigTableHandler.updateHostURL(conn, machineId, transferAgentId, id, newHostURL);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("contextRoot")) {
				boolean needToUpdate = (!StringUtil.equals(newContextRoot, oldContextRoot)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.nodeConfigTableHandler.updateContextRoot(conn, machineId, transferAgentId, id, newContextRoot);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return isUpdated;
	}

	@Override
	public boolean deleteNodeConfig(String machineId, String transferAgentId, String nodeId) throws ServerException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = null;
		try {
			conn = getConnection();
			return this.nodeConfigTableHandler.delete(conn, machineId, transferAgentId, nodeId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	// ------------------------------------------------------
	// Workspaces
	// ------------------------------------------------------
	@Override
	public IWorkspaceService getWorkspaceService() {
		return this.workspaceService;
	}

}
