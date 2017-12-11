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

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.orbit.component.runtime.OrbitConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.StringUtil;
import org.origin.core.resources.IWorkspaceService;
import org.origin.core.resources.WorkspaceServiceFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DomainManagementServiceDatabaseImpl implements DomainManagementService {

	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected Properties workspaceProperties;
	protected ServiceRegistration<?> serviceRegistry;

	protected MachineConfigTableHandler machineConfigTableHandler = MachineConfigTableHandler.INSTANCE;
	protected TransferAgentConfigTableHandler transferAgentConfigTableHandler = TransferAgentConfigTableHandler.INSTANCE;
	protected NodeConfigTableHandler nodeConfigTableHandler = NodeConfigTableHandler.INSTANCE;
	protected IWorkspaceService workspaceService;

	public DomainManagementServiceDatabaseImpl() {
	}

	/**
	 * Start service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAMESPACE);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD);

		update(configProps);

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

	/**
	 * Stop service
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

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
		System.out.println(getClass().getSimpleName() + ".updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
		String namespace = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAMESPACE);
		String name = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		String hostURL = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL);
		String jdbcUsername = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD);

		System.out.println();
		System.out.println("Config properties:");
		System.out.println("-----------------------------------------------------");
		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAMESPACE + " = " + namespace);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL + " = " + jdbcURL);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD + " = " + jdbcPassword);
		System.out.println("-----------------------------------------------------");
		System.out.println();

		this.databaseProperties = DatabaseUtil.getProperties(jdbcDriver, jdbcURL, jdbcUsername, jdbcPassword);
		// Initialize database tables.
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		try {
			DatabaseUtil.initialize(conn, MachineConfigTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, TransferAgentConfigTableHandler.INSTANCE);
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

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getNamespace() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAMESPACE);
	}

	@Override
	public String getName() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_HOST_URL);
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
		return (String) this.properties.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT);
	}

	/**
	 * 
	 * @param machineId
	 * @throws DomainMgmtException
	 */
	protected void checkMachineId(String machineId) throws DomainMgmtException {
		if (machineId == null || machineId.isEmpty()) {
			throw new DomainMgmtException("400", "machineId is empty.");
		}
	}

	/**
	 * 
	 * @param transferAgentId
	 * @throws DomainMgmtException
	 */
	protected void checkTransferAgentId(String transferAgentId) throws DomainMgmtException {
		if (transferAgentId == null || transferAgentId.isEmpty()) {
			throw new DomainMgmtException("400", "transferAgentId is empty.");
		}
	}

	/**
	 * 
	 * @param nodeId
	 * @throws DomainMgmtException
	 */
	protected void checkNodeId(String nodeId) throws DomainMgmtException {
		if (nodeId == null || nodeId.isEmpty()) {
			throw new DomainMgmtException("400", "nodeId is empty.");
		}
	}

	/**
	 * 
	 * @param e
	 * @throws DomainMgmtException
	 */
	protected void handleSQLException(SQLException e) throws DomainMgmtException {
		e.printStackTrace();
		throw new DomainMgmtException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	@Override
	public List<MachineConfigRTO> getMachineConfigs() throws DomainMgmtException {
		Connection conn = getConnection();
		try {
			return this.machineConfigTableHandler.getMachineConfigs(conn);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public MachineConfigRTO getMachineConfig(String machineId) throws DomainMgmtException {
		checkMachineId(machineId);

		Connection conn = getConnection();
		try {
			return this.machineConfigTableHandler.getMachineConfig(conn, machineId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean machineConfigExists(String machineId) throws DomainMgmtException {
		checkMachineId(machineId);

		Connection conn = getConnection();
		try {
			return this.machineConfigTableHandler.machineConfigExists(conn, machineId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws DomainMgmtException {
		String id = addMachineRequest.getId();
		String name = addMachineRequest.getName();
		String ipAddress = addMachineRequest.getIpAddress();

		checkMachineId(id);

		Connection conn = getConnection();
		try {
			MachineConfigRTO newMachineConfig = this.machineConfigTableHandler.addMachineConfig(conn, id, name, ipAddress);
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
	public boolean updateMachineConfig(MachineConfigRTO updateMachineRequest, List<String> fieldsToUpdate) throws DomainMgmtException {
		String id = updateMachineRequest.getId();
		String newName = updateMachineRequest.getName();
		String newIpAddress = updateMachineRequest.getIpAddress();
		if (fieldsToUpdate == null) {
			fieldsToUpdate = Collections.emptyList();
		}

		checkMachineId(id);

		boolean isUpdated = false;

		Connection conn = getConnection();
		try {
			MachineConfigRTO machineConfig = this.machineConfigTableHandler.getMachineConfig(conn, id);
			if (machineConfig == null) {
				throw new DomainMgmtException("404", "Machine with id '" + id + "' is not found.");
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
	public boolean deleteMachineConfig(String machineId) throws DomainMgmtException {
		checkMachineId(machineId);

		Connection conn = getConnection();
		try {
			return this.machineConfigTableHandler.deleteMachineConfig(conn, machineId);
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
	public List<TransferAgentConfigRTO> getTransferAgentConfigs(String machineId) throws DomainMgmtException {
		checkMachineId(machineId);

		Connection conn = getConnection();
		try {
			return this.transferAgentConfigTableHandler.getTransferAgentConfigs(conn, machineId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public TransferAgentConfigRTO getTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = getConnection();
		try {
			return this.transferAgentConfigTableHandler.getTransferAgentConfig(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean transferAgentConfigExists(String machineId, String transferAgentId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = getConnection();
		try {
			return this.transferAgentConfigTableHandler.transferAgentConfigExists(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addTransferAgentConfig(String machineId, TransferAgentConfigRTO addTransferAgentRequest) throws DomainMgmtException {
		String id = addTransferAgentRequest.getId();
		String name = addTransferAgentRequest.getName();
		String home = addTransferAgentRequest.getHome();
		String hostURL = addTransferAgentRequest.getHostURL();
		String contextRoot = addTransferAgentRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(id);

		Connection conn = getConnection();
		try {
			TransferAgentConfigRTO newTransferAgentConfig = this.transferAgentConfigTableHandler.addTransferAgentConfig(conn, machineId, id, name, home, hostURL, contextRoot);
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
	public boolean updateTransferAgentConfig(String machineId, TransferAgentConfigRTO updateTransferAgentRequest, List<String> fieldsToUpdate) throws DomainMgmtException {
		String id = updateTransferAgentRequest.getId();
		String newName = updateTransferAgentRequest.getName();
		String newHome = updateTransferAgentRequest.getHome();
		String newHostURL = updateTransferAgentRequest.getHostURL();
		String newContextRoot = updateTransferAgentRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(id);

		boolean isUpdated = false;

		Connection conn = getConnection();
		try {
			TransferAgentConfigRTO transferAgentConfig = this.transferAgentConfigTableHandler.getTransferAgentConfig(conn, machineId, id);
			if (transferAgentConfig == null) {
				throw new DomainMgmtException("404", "TransferAgent with id '" + id + "' is not found.");
			}

			String oldName = transferAgentConfig.getName();
			String oldHome = transferAgentConfig.getHome();
			String oldHostURL = transferAgentConfig.getHostURL();
			String oldContextRoot = transferAgentConfig.getContextRoot();

			if (fieldsToUpdate.contains("name")) {
				boolean needToUpdate = (!StringUtil.equals(newName, oldName)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.transferAgentConfigTableHandler.updateName(conn, machineId, id, newName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("home")) {
				boolean needToUpdate = (!StringUtil.equals(newHome, oldHome)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.transferAgentConfigTableHandler.updateHome(conn, machineId, id, newHome);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("hostURL")) {
				boolean needToUpdate = (!StringUtil.equals(newHostURL, oldHostURL)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.transferAgentConfigTableHandler.updateHostURL(conn, machineId, id, newHostURL);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (fieldsToUpdate.contains("contextRoot")) {
				boolean needToUpdate = (!StringUtil.equals(newContextRoot, oldContextRoot)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.transferAgentConfigTableHandler.updateContextRoot(conn, machineId, id, newContextRoot);
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
	public boolean deleteTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = getConnection();
		try {
			return this.transferAgentConfigTableHandler.deleteTransferAgentConfig(conn, machineId, transferAgentId);
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
	public List<NodeConfigRTO> getNodeConfigs(String machineId, String transferAgentId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		Connection conn = getConnection();
		try {
			return this.nodeConfigTableHandler.getNodeConfigs(conn, machineId, transferAgentId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public NodeConfigRTO getNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = getConnection();
		try {
			return this.nodeConfigTableHandler.getNodeConfig(conn, machineId, transferAgentId, nodeId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean nodeConfigExists(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = getConnection();
		try {
			return this.nodeConfigTableHandler.nodeConfigExists(conn, machineId, transferAgentId, nodeId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean addNodeConfig(String machineId, String transferAgentId, NodeConfigRTO addNodeRequest) throws DomainMgmtException {
		String id = addNodeRequest.getId();
		String name = addNodeRequest.getName();
		String home = addNodeRequest.getHome();
		String hostURL = addNodeRequest.getHostURL();
		String contextRoot = addNodeRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(id);

		Connection conn = getConnection();
		try {
			NodeConfigRTO newNodeConfig = this.nodeConfigTableHandler.addNodeConfig(conn, machineId, transferAgentId, id, name, home, hostURL, contextRoot);
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
	public boolean updateNodeConfig(String machineId, String transferAgentId, NodeConfigRTO updateNodeRequest, List<String> fieldsToUpdate) throws DomainMgmtException {
		String id = updateNodeRequest.getId();
		String newName = updateNodeRequest.getName();
		String newHome = updateNodeRequest.getHome();
		String newHostURL = updateNodeRequest.getHostURL();
		String newContextRoot = updateNodeRequest.getContextRoot();

		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(id);

		boolean isUpdated = false;

		Connection conn = getConnection();
		try {
			NodeConfigRTO nodeConfig = this.nodeConfigTableHandler.getNodeConfig(conn, machineId, transferAgentId, id);
			if (nodeConfig == null) {
				throw new DomainMgmtException("404", "NodeAgent with id '" + id + "' is not found.");
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
	public boolean deleteNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		Connection conn = getConnection();
		try {
			return this.nodeConfigTableHandler.deleteNodeConfig(conn, machineId, transferAgentId, nodeId);
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
