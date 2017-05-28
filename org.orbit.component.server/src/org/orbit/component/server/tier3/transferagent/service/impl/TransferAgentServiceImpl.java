package org.orbit.component.server.tier3.transferagent.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.osgi.framework.ServiceRegistration;

public class TransferAgentServiceImpl implements TransferAgentService {

	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	

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

}
