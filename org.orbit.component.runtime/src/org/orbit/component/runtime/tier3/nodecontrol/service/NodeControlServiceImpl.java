package org.orbit.component.runtime.tier3.nodecontrol.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.PlatformConstants;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.util.LaunchServiceHelper;
import org.orbit.component.runtime.util.PlatformSetupUtil;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.api.Clients;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.sdk.Activator;
import org.orbit.platform.sdk.IPlatform;
import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.launcher.JavaLauncher;
import org.origin.common.launch.launcher.ScriptLauncher;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.ResourcesFactory;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.resources.util.WorkspaceHelper;
import org.origin.common.resources.util.WorkspaceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see HomeAgentServiceImpl for IEditingDomain
 *
 */
public class NodeControlServiceImpl implements NodeControlService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected IWorkspace workspace;
	protected WSEditPolicies wsEditPolicies;

	protected boolean useScriptLaunch = false;

	/**
	 * 
	 * @param initProperties
	 */
	public NodeControlServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(NodeControlService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();

		PlatformSetupUtil.loadPlatformConfigProperties(bundleContext, properties);
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.PLATFORM_HOME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.NODESPACE_LOCATION);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(NodeControlService.class, this, props);

		// Path homePath = Paths.get(getHome()).toAbsolutePath();
		// Path nodespacePath = TASetupUtil.getNodespacesPath(homePath, true);
		Path nodespacePath = Paths.get(getNodespaceLocation()).toAbsolutePath();

		this.workspace = ResourcesFactory.getInstance().createWorkspace(nodespacePath.toFile());
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		if (this.workspace != null) {
			this.workspace.dispose();
			this.workspace = null;
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

		String indexServiceUrl = (String) properties.get(OrbitConstants.ORBIT_INDEX_SERVICE_URL);
		String platformHome = (String) properties.get(OrbitConstants.PLATFORM_HOME);
		String nodespaceHome = (String) properties.get(OrbitConstants.NODESPACE_LOCATION);
		String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_NAME);
		String hostURL = (String) properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(OrbitConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(OrbitConstants.PLATFORM_HOME + " = " + platformHome);
			System.out.println(OrbitConstants.NODESPACE_LOCATION + " = " + nodespaceHome);
			System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_NAME + " = " + name);
			System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL + " = " + hostURL);
			System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT + " = " + contextRoot);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = properties;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL);
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
		String contextRoot = (String) this.properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public String getPlatformHome() {
		String platformHome = (String) this.properties.get(OrbitConstants.PLATFORM_HOME);
		return platformHome;
	}

	@Override
	public String getNodespaceLocation() {
		String nodespaceLocation = (String) this.properties.get(OrbitConstants.NODESPACE_LOCATION);
		if (nodespaceLocation == null || nodespaceLocation.isEmpty()) {
			String home = getPlatformHome();
			if (!home.endsWith("/")) {
				home += "/";
			}
			nodespaceLocation = home + "nodespace";
		}
		return nodespaceLocation;
	}

	protected String getIndexServiceURL() {
		String indexServiceUrl = (String) this.properties.get(OrbitConstants.ORBIT_INDEX_SERVICE_URL);
		return indexServiceUrl;
	}

	@Override
	public IWorkspace getWorkspace() {
		return this.workspace;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public List<INode> getNodes() {
		IWorkspace workspace = getWorkspace();
		List<INode> resultNodes = WorkspaceHelper.INSTANCE.getRootNodes(workspace);
		if (resultNodes == null) {
			resultNodes = Collections.emptyList();
		}
		return resultNodes;
	}

	@Override
	public List<INode> getNodes(String typeId) {
		List<INode> resultNodes = new ArrayList<INode>();
		if (typeId != null) {
			IWorkspace workspace = getWorkspace();
			List<INode> nodes = WorkspaceHelper.INSTANCE.getRootNodes(workspace);
			if (nodes != null) {
				for (INode currNode : nodes) {
					String currTypeId = null;
					try {
						NodeDescription desc = currNode.getDescription();
						if (desc != null) {
							currTypeId = desc.getStringAttribute("typeId");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (typeId.equals(currTypeId)) {
						resultNodes.add(currNode);
					}
				}
			}
		}
		return resultNodes;
	}

	@Override
	public boolean nodeExists(String id) {
		IWorkspace workspace = getWorkspace();
		return WorkspaceHelper.INSTANCE.rootNodeExists(workspace, id);
	}

	@Override
	public INode getNode(String id) {
		IWorkspace workspace = getWorkspace();
		return WorkspaceHelper.INSTANCE.getRootNode(workspace, id);
	}

	@Override
	public INode createNode(String id, String typeId, String name) throws IOException {
		IWorkspace workspace = getWorkspace();
		INode node = WorkspaceUtil.INSTANCE.createNode(workspace, id, typeId, name);
		if (node != null) {
			Object context = this;
			WorkspaceUtil.INSTANCE.preConfigureFolder(context, workspace, node);
		}
		return node;
	}

	// boolean setNodeAttribute(String id, String attrName, String attrValue) throws IOException;

	// @Override
	// public boolean setNodeAttribute(String id, String attrName, String attrValue) throws IOException {
	// IWorkspace workspace = getWorkspace();
	// return WorkspaceUtil.INSTANCE.setNodeAttribute(workspace, id, attrName, attrValue);
	// }

	@Override
	public boolean addAttribute(String id, String name, Object value) throws IOException {
		INode node = getNode(id);
		if (node != null && name != null) {
			NodeDescription desc = node.getDescription();
			if (!desc.hasAttribute(name)) {
				desc.setAttirbute(name, value);
				node.setDescription(desc);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateAttribute(String id, String oldName, String name, Object value) throws IOException {
		INode node = getNode(id);
		if (node != null && name != null) {
			NodeDescription desc = node.getDescription();
			boolean isNameChanged = (oldName != null && !oldName.equals(name)) ? true : false;
			if (isNameChanged) {
				if (desc.hasAttribute(name)) {
					// attribute with new name already exists.
					return false;
				}
				desc.removeAttribute(oldName);
			}
			desc.setAttirbute(name, value);
			node.setDescription(desc);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAttribute(String id, String name) throws IOException {
		INode node = getNode(id);
		if (node != null && name != null) {
			NodeDescription desc = node.getDescription();
			if (desc.hasAttribute(name)) {
				desc.removeAttribute(name);
				node.setDescription(desc);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteNode(String id) throws IOException {
		IWorkspace workspace = getWorkspace();
		boolean succeed = WorkspaceUtil.INSTANCE.deleteNode(workspace, id);
		return succeed;
	}

	protected String getNodeLocation(String nodespaceLocation, INode node) {
		if (!nodespaceLocation.endsWith("/")) {
			nodespaceLocation += "/";
		}
		String nodeLocation = nodespaceLocation + node.getName();
		return nodeLocation;
	}

	protected Map<String, String> nodeIdToLaunchInstanceIdMap = new HashMap<String, String>();
	protected boolean isNodeStarting = false;
	protected boolean isNodeStopping = false;

	@Override
	public synchronized boolean startNode(String id) throws IOException {
		if (isNodeStarted(id)) {
			throw new IOException("Node with id '" + id + "' is already started.");
		}
		if (isNodeStarting(id)) {
			throw new IOException("Node with id '" + id + "' is being started.");
		}
		if (isNodeStopping(id)) {
			throw new IOException("Node with id '" + id + "' is being stopped.");
		}

		try {
			this.isNodeStarting = true;

			LaunchService launchService = LaunchServiceHelper.INSTANCE.getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return false;
			}
			INode node = getNode(id);
			if (node == null) {
				LOG.error("Node with id '" + id + "' is not found.");
				return false;
			}

			String homeLocation = getPlatformHome();
			String nodespaceLocation = getNodespaceLocation();
			String nodeLocation = getNodeLocation(nodespaceLocation, node);

			// 1. Create {node_path}/bin/start_node.sh file and {node_path}/configuration/config.ini file
			// (Ideally should use resource builder to generate both when .node file is changed. Then only need to trigger a build here.)
			LaunchServiceHelper.INSTANCE.generateStartNodeScript(node);
			LaunchServiceHelper.INSTANCE.generateConfigIni(node, this.properties);

			// 2. Create launch configuration
			String launchTypeId = LaunchServiceHelper.INSTANCE.getLaunchTypeId();
			String launchConfigName = LaunchServiceHelper.INSTANCE.getLaunchConfigName(id);
			LaunchConfig launchConfig = launchService.getLaunchConfiguration(launchTypeId, launchConfigName);
			if (launchConfig == null) {
				launchConfig = launchService.createLaunchConfiguration(launchTypeId, launchConfigName);
			}

			if (this.useScriptLaunch) {
				launchConfig.setAttribute(LaunchConfig.LAUNCHER_ID, ScriptLauncher.ID);

				String scriptLocation = nodeLocation + "/bin/start_node.sh";
				launchConfig.setAttribute(ScriptLauncher.START_SCRIPT_LOCATION, scriptLocation);

			} else {
				launchConfig.setAttribute(LaunchConfig.LAUNCHER_ID, JavaLauncher.ID);

				Map<String, String> vmArgumentsMap = new HashMap<String, String>();
				String jarFileLocation = homeLocation + "/plugins/org.eclipse.osgi_3.10.101.v20150820-1432.jar";
				vmArgumentsMap.put("-jar", jarFileLocation);
				launchConfig.setAttribute(LaunchConstants.VM_ARGUMENTS_MAP, vmArgumentsMap);

				Map<String, String> systemArgumentsMap = new HashMap<String, String>();
				String nodeConfigurationLocation = nodeLocation + "/configuration";
				systemArgumentsMap.put("-configuration", nodeConfigurationLocation);
				launchConfig.setAttribute(LaunchConstants.SYSTEM_ARGUMENTS_MAP, systemArgumentsMap);

				List<String> programArgs = new ArrayList<String>();
				programArgs.add("-console");
				launchConfig.setAttribute(LaunchConstants.PROGRAM_ARGUMENTS_LIST, programArgs);
			}
			launchConfig.setAttribute(ScriptLauncher.WORKING_DIRECTORY_LOCATION, nodeLocation);
			launchConfig.save();

			// 3. Launch the node with launch configuration
			LaunchInstance launchInstance = launchConfig.launch();
			if (launchInstance != null) {
				this.nodeIdToLaunchInstanceIdMap.put(id, launchInstance.getId());
				return true;
			}

			return false;

		} finally {
			this.isNodeStarting = false;
		}
	}

	@Override
	public synchronized boolean stopNode(String id) throws IOException {
		if (isNodeStopped(id)) {
			throw new IOException("Node with id '" + id + "' is already stopped.");
		}
		if (isNodeStopping(id)) {
			throw new IOException("Node with id '" + id + "' is being stopped.");
		}
		if (isNodeStarting(id)) {
			throw new IOException("Node with id '" + id + "' is being started.");
		}

		try {
			this.isNodeStopping = true;

			LaunchService launchService = LaunchServiceHelper.INSTANCE.getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return false;
			}
			INode node = getNode(id);
			if (node == null) {
				LOG.error("Node with id '" + id + "' is not found.");
				return false;
			}

			// 1. Direct shutdown node platform
			boolean isDirectShutdownSucceed = false;
			PlatformClient nodePlatformClient = null;
			IPlatform currPlatform = Activator.getInstance().getPlatform();
			String indexServiceUrl = getIndexServiceURL();
			IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
			if (currPlatform != null && indexService != null) {
				String platformId = currPlatform.getId();
				IndexItem nodeIndexItem = getNodeIndexItem(indexService, platformId, id);
				if (nodeIndexItem != null) {
					nodePlatformClient = getPlatformClient(nodeIndexItem);
				}
			}
			if (nodePlatformClient != null) {
				try {
					nodePlatformClient.shutdown(10 * 1000, false);
					isDirectShutdownSucceed = true;
				} catch (ClientException e) {
					e.printStackTrace();
				}
			}

			// 2. Terminate launch instance
			boolean isLaunchInstanceTerminated = false;
			String launchInstanceId = this.nodeIdToLaunchInstanceIdMap.remove(id);
			if (launchInstanceId != null) {
				LaunchInstance launchInstance = launchService.getLaunchInstance(launchInstanceId);
				if (launchInstance != null) {
					if (this.useScriptLaunch) {
						if (launchInstance.canTerminate()) {
							isLaunchInstanceTerminated = launchInstance.terminate();
							if (isLaunchInstanceTerminated) {
								LOG.info("LaunchInstance with id '" + launchInstanceId + "' is terminated.");
							} else {
								LOG.error("LaunchInstance with id '" + launchInstanceId + "' is not terminated.");
							}
						} else {
							LOG.error("LaunchInstance with id '" + launchInstanceId + "' cannot be terminated.");
						}
					} else {
						launchInstance.remove();
						isLaunchInstanceTerminated = true;
					}
				} else {
					LOG.error("LaunchInstance with id '" + launchInstanceId + "' is not found.");
				}
			}

			boolean succeed = isDirectShutdownSucceed;
			return succeed;

		} finally {
			this.isNodeStopping = false;
		}
	}

	@Override
	public boolean isNodeStarting(String id) throws IOException {
		return this.isNodeStarting;
	}

	@Override
	public boolean isNodeStarted(String id) throws IOException {
		IPlatform currPlatform = Activator.getInstance().getPlatform();
		IndexService indexService = InfraClients.getInstance().getIndexService(getIndexServiceURL());
		if (currPlatform != null && indexService != null) {
			String platformId = currPlatform.getId();
			IndexItem nodeIndexItem = getNodeIndexItem(indexService, platformId, id);
			if (nodeIndexItem != null) {
				boolean isOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				if (isOnline) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isNodeStopping(String id) throws IOException {
		return this.isNodeStopping;
	}

	@Override
	public boolean isNodeStopped(String id) throws IOException {
		IPlatform currPlatform = Activator.getInstance().getPlatform();
		IndexService indexService = InfraClients.getInstance().getIndexService(getIndexServiceURL());
		if (currPlatform != null && indexService != null) {
			String platformId = currPlatform.getId();
			IndexItem nodeIndexItem = getNodeIndexItem(indexService, platformId, id);
			if (nodeIndexItem != null) {
				boolean isOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				boolean isStopped = false;
				String runtimeState = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_RUNTIME_STATE);
				if ("stopped".equalsIgnoreCase(runtimeState)) {
					isStopped = true;
				}
				if (!isOnline || isStopped) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param indexService
	 * @param platformParentId
	 * @param nodePlatformid
	 * @return
	 * @throws IOException
	 */
	public IndexItem getNodeIndexItem(IndexService indexService, String platformParentId, String nodePlatformid) throws IOException {
		IndexItem nodeIndexItem = null;
		if (indexService != null && platformParentId != null && nodePlatformid != null) {
			List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					String currPlatformId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
					String currPlatformParentId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_PARENT_ID);
					String currPlatformType = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_TYPE);

					if (PlatformConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId) && nodePlatformid.equals(currPlatformId)) {
						nodeIndexItem = indexItem;
						break;
					}
				}
			}
		}
		return nodeIndexItem;
	}

	/**
	 * 
	 * @param nodeIdToIndexItem
	 * @param nodeId
	 * @return
	 */
	public PlatformClient getPlatformClient(IndexItem indexItem) {
		PlatformClient platformClient = null;
		if (indexItem != null) {
			String platformUrl = null;
			String platformHostUrl = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
			String platformContextRoot = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);

			if (platformHostUrl != null && platformContextRoot != null) {
				platformUrl = platformHostUrl;
				if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
					platformUrl += "/";
				}
				platformUrl += platformContextRoot;
			}

			if (platformUrl != null) {
				platformClient = Clients.getInstance().getPlatformClient(platformUrl);
			}
		}
		return platformClient;
	}

}

// protected IEditingDomain editingDomain;
// @Override
// public IEditingDomain getEditingDomain() {
// return this.editingDomain;
// }
// this.editingDomain = IEditingDomain.getEditingDomain(TransferAgentService.class.getName());
// if (this.editingDomain != null) {
// IEditingDomain.disposeEditingDomain(TransferAgentService.class.getName());
// this.editingDomain = null;
// }

// public class TransferAgentWSEditPolicies implements WSEditPolicies {
// WSEditPoliciesSupport editPoliciesSupport = new WSEditPoliciesSupport();
//
// @Override
// public List<WSEditPolicy> getEditPolicies() {
// return this.editPoliciesSupport.getEditPolicies();
// }
//
// @Override
// public WSEditPolicy getEditPolicy(String id) {
// return this.editPoliciesSupport.getEditPolicy(id);
// }
//
// @Override
// public boolean installEditPolicy(WSEditPolicy editPolicy) {
// // Initialize the WSEditPolicy with service
// editPolicy.setService(TransferAgentService.class, TransferAgentServiceImpl.this);
//
// return this.editPoliciesSupport.installEditPolicy(editPolicy);
// }
//
// @Override
// public boolean uninstallEditPolicy(WSEditPolicy editPolicy) {
// boolean succeed = this.editPoliciesSupport.uninstallEditPolicy(editPolicy);
// if (succeed) {
// editPolicy.setService(TransferAgentService.class, null);
// }
// return succeed;
// }
//
// @Override
// public WSEditPolicy uninstallEditPolicy(String id) {
// return this.editPoliciesSupport.uninstallEditPolicy(id);
// }
//
// @Override
// public WSCommand getCommand(Request request) {
// return this.editPoliciesSupport.getCommand(request);
// }
// }

// String configIniLocation = nodeLocation + "/configuration/config.ini";

// String oldConfigIniLocation = launchConfig.getAttribute(ScriptLauncher.CONFIG_INI_LOCATION, (String) null);
// if (!configIniLocation.equals(oldConfigIniLocation)) {
// launchConfig.setAttribute(ScriptLauncher.CONFIG_INI_LOCATION, configIniLocation);
// }

// String launchInstanceId = this.nodeIdToLaunchInstasnceIdMap.remove(id);
// if (launchInstanceId != null) {
// LOG.error("Node with id '" + id + "' is already started.");
// return false;
// }
