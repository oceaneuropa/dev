package org.orbit.component.server.tier3.transferagent.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.IEditingDomain;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @see HomeAgentServiceImpl for IEditingDomain
 *
 */
public class TransferAgentServiceImpl implements TransferAgentService {

	protected BundleContext bundleContext;
	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected IEditingDomain editingDomain;

	public TransferAgentServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public String getHostURL() {
		return null;
	}

	@Override
	public String getContextRoot() {
		return null;
	}

	@Override
	public String getName() {
		String name = (String) this.configProps.get(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		return name;
	}

	@Override
	public String getHome() {
		return null;
	}

	public void start() {
		this.editingDomain = IEditingDomain.getEditingDomain(TransferAgentService.class.getName());

	}

	public void stop() {
		if (this.editingDomain != null) {
			IEditingDomain.disposeEditingDomain(TransferAgentService.class.getName());
			this.editingDomain = null;
		}
	}

	@Override
	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

}
