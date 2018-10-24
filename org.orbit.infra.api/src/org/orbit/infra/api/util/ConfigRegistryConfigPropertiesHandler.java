package org.orbit.infra.api.util;

import org.orbit.infra.api.InfraConstants;
import org.origin.common.service.AbstractConfigPropertiesHandler;

public class ConfigRegistryConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static ConfigRegistryConfigPropertiesHandler INSTANCE = new ConfigRegistryConfigPropertiesHandler();

	public static ConfigRegistryConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //
		};
		return propNames;
	}

}
