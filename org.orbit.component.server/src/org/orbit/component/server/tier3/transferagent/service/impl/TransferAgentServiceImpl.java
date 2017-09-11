package org.orbit.component.server.tier3.transferagent.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.server.tier3.transferagent.util.TASetupUtil;
import org.origin.common.command.IEditingDomain;
import org.origin.common.util.PropertyUtil;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.ResourceFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @see HomeAgentServiceImpl for IEditingDomain
 *
 */
public class TransferAgentServiceImpl implements TransferAgentService {

	protected BundleContext bundleContext;
	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected IEditingDomain editingDomain;
	protected IWorkspace nodespaceRoot;

	/**
	 * 
	 * @param bundleContext
	 */
	public TransferAgentServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	@Override
	public String getName() {
		String name = (String) this.configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.configProps.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public String getHome() {
		String home = (String) this.configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOME);
		return home;
	}

	public void start() {
		this.editingDomain = IEditingDomain.getEditingDomain(TransferAgentService.class.getName());

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		TASetupUtil.loadConfigIniProperties(bundleContext, configProps);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_HOME);

		updateProperties(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(TransferAgentService.class, this, props);

		Path taHome = Paths.get(getHome()).toAbsolutePath();
		Path nodespacesPath = TASetupUtil.getNodespacesPath(taHome, true);

		this.nodespaceRoot = ResourceFactory.getInstance().createFsRoot(nodespacesPath.toFile());
	}

	public void stop() {
		if (this.nodespaceRoot != null) {
			this.nodespaceRoot.dispose();
			this.nodespaceRoot = null;
		}

		if (this.editingDomain != null) {
			IEditingDomain.disposeEditingDomain(TransferAgentService.class.getName());
			this.editingDomain = null;
		}
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);
		String hostURL = (String) configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOST_URL);
		String contextRoot = (String) configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_CONTEXT_ROOT);
		String home = (String) configProps.get(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOME);

		System.out.println();
		System.out.println("Config properties:");
		System.out.println("-----------------------------------------------------");
		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_TRANSFER_AGENT_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_TRANSFER_AGENT_HOME + " = " + home);
		System.out.println("-----------------------------------------------------");
		System.out.println();

		this.configProps = configProps;
	}

	@Override
	public IWorkspace getNodespaceRoot() {
		return this.nodespaceRoot;
	}

}
