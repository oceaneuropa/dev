package org.orbit.infra.api.util;

import org.orbit.infra.api.InfraConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class InfraConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static InfraConfigPropertiesHandler INSTANCE = new InfraConfigPropertiesHandler();

	public static InfraConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //
				InfraConstants.ORBIT_SUBS_SERVER_URL, //
		};
		return propNames;
	}

	public String getIndexServiceURL() {
		String url = getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		return url;
	}

	public String getExtensionRegistryURL() {
		String url = getProperty(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		return url;
	}

	public String getConfigRegistryURL() {
		String url = getProperty(InfraConstants.ORBIT_CONFIG_REGISTRY_URL);
		return url;
	}

	public String getSubsServerURL() {
		String url = getProperty(InfraConstants.ORBIT_SUBS_SERVER_URL);
		return url;
	}

}
