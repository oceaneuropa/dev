package org.orbit.component.runtime.tier3.nodecontrol.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.nodecontrol.util.TASetupUtil;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.ResourcesFactory;
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
	protected IWorkspace nodespaceRoot;
	protected WSEditPolicies wsEditPolicies;

	public NodeControlServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(NodeControlService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		TASetupUtil.loadConfigIniProperties(bundleContext, properties);
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_CONTROL_HOME);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(NodeControlService.class, this, props);

		Path taHome = Paths.get(getHome()).toAbsolutePath();
		Path nodespacesPath = TASetupUtil.getNodespacesPath(taHome, true);

		this.nodespaceRoot = ResourcesFactory.getInstance().createWorkspace(nodespacesPath.toFile());
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		if (this.nodespaceRoot != null) {
			this.nodespaceRoot.dispose();
			this.nodespaceRoot = null;
		}
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void update(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(OrbitConstants.COMPONENT_NODE_CONTROL_NAME);
		String hostURL = (String) configProps.get(OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL);
		String contextRoot = (String) configProps.get(OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT);
		String home = (String) configProps.get(OrbitConstants.COMPONENT_NODE_CONTROL_HOME);

		System.out.println();
		System.out.println("Config properties:");
		System.out.println("-----------------------------------------------------");
		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_NODE_CONTROL_HOME + " = " + home);
		System.out.println("-----------------------------------------------------");
		System.out.println();

		this.properties = configProps;
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
		String home = (String) this.properties.get(OrbitConstants.COMPONENT_NODE_CONTROL_HOME);
		return home;
	}

	@Override
	public IWorkspace getNodeWorkspace() {
		return this.nodespaceRoot;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
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
