package org.orbit.infra.api.util;

import org.orbit.infra.api.InfraConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class InfraServicesPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static InfraServicesPropertiesHandler INSTANCE = new InfraServicesPropertiesHandler();

	public static InfraServicesPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //
		};
		return propNames;
	}

	public String getIndexServiceURL() {
		String serviceUrl = getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		return serviceUrl;
	}

	public String getExtensionRegistryURL() {
		String serviceUrl = getProperty(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		return serviceUrl;
	}

	public String getConfigRegistryURL() {
		String serviceUrl = getProperty(InfraConstants.ORBIT_CONFIG_REGISTRY_URL);
		return serviceUrl;
	}

}
