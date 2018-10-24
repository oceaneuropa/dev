package org.orbit.infra.runtime.util;

import org.orbit.infra.runtime.InfraConstants;
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
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				InfraConstants.CONFIG_REGISTRY__AUTOSTART, //
				InfraConstants.CONFIG_REGISTRY__NAME, //
				InfraConstants.CONFIG_REGISTRY__HOST_URL, //
				InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT, //
				InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER, //
				InfraConstants.CONFIG_REGISTRY__JDBC_URL, //
				InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME, //
				InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD, //
		};
		return propNames;
	}

}
