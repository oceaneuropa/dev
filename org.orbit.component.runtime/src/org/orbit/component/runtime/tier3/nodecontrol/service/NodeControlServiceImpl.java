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

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.nodecontrol.util.NodeHelper;
import org.orbit.component.runtime.tier3.nodecontrol.util.PlatformSetupUtil;
import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.launcher.ScriptLauncher;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.ResourcesFactory;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.resources.util.WorkspaceHelper;
import org.origin.common.resources.util.WorkspaceUtil;
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
	public String getHome() {
		String home = (String) this.properties.get(OrbitConstants.PLATFORM_HOME);
		return home;
	}

	@Override
	public String getNodespaceLocation() {
		String nodespaceLocation = (String) this.properties.get(OrbitConstants.NODESPACE_LOCATION);
		if (nodespaceLocation == null || nodespaceLocation.isEmpty()) {
			String home = getHome();
			if (!home.endsWith("/")) {
				home += "/";
			}
			nodespaceLocation = home + "nodespace";
		}
		return nodespaceLocation;
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

	@Override
	public boolean setNodeAttribute(String id, String attrName, String attrValue) throws IOException {
		IWorkspace workspace = getWorkspace();
		return WorkspaceUtil.INSTANCE.setNodeAttribute(workspace, id, attrName, attrValue);
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

	protected Map<String, String> nodeIdToLaunchInstasnceIdMap = new HashMap<String, String>();

	@Override
	public synchronized boolean startNode(String id) throws IOException {
		LaunchService launchService = NodeHelper.INSTANCE.getLaunchService();
		if (launchService == null) {
			LOG.error("LaunchService is null.");
			return false;
		}
		INode node = getNode(id);
		if (node == null) {
			LOG.error("Node with id '" + id + "' is not found.");
			return false;
		}

		// 1. Create launch configuration
		String launchTypeId = NodeHelper.INSTANCE.getLaunchTypeId();
		String launchConfigName = NodeHelper.INSTANCE.getLaunchConfigName(id);

		String launcherId = ScriptLauncher.ID;
		String nodespaceLocation = getNodespaceLocation();
		String nodeLocation = getNodeLocation(nodespaceLocation, node);
		String startNodeScriptLocation = nodeLocation + "/bin/start_node.sh";

		LaunchConfig launchConfig = launchService.getLaunchConfiguration(launchTypeId, launchConfigName);
		if (launchConfig == null) {
			launchConfig = launchService.createLaunchConfiguration(launchTypeId, launchConfigName);
		}
		launchConfig.setAttribute(LaunchConfig.LAUNCHER_ID, launcherId);
		launchConfig.setAttribute(ScriptLauncher.WORKING_DIRECTORY_LOCATION, nodeLocation);
		launchConfig.setAttribute(ScriptLauncher.START_SCRIPT_LOCATION, startNodeScriptLocation);
		launchConfig.save();

		// 2. Launch the node with launch configuration
		LaunchInstance launchInstance = launchConfig.launch();
		if (launchInstance != null) {
			this.nodeIdToLaunchInstasnceIdMap.put(id, launchInstance.getId());
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean stopNode(String id) throws IOException {
		INode node = getNode(id);
		if (node == null) {
			return false;
		}
		return false;
	}

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