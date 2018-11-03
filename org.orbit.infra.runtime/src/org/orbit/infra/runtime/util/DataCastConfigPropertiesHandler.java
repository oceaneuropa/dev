package org.orbit.infra.runtime.util;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.service.AbstractConfigPropertiesHandler;

public class DataCastConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static DataCastConfigPropertiesHandler INSTANCE = new DataCastConfigPropertiesHandler();

	public static DataCastConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_HOST_URL, //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //

				// For service
				InfraConstants.DATACAST__AUTOSTART, //
				InfraConstants.DATACAST__ID, //
				InfraConstants.DATACAST__NAME, //
				InfraConstants.DATACAST__HOST_URL, //
				InfraConstants.DATACAST__CONTEXT_ROOT, //
				InfraConstants.DATACAST__JDBC_DRIVER, //
				InfraConstants.DATACAST__JDBC_URL, //
				InfraConstants.DATACAST__JDBC_USERNAME, //
				InfraConstants.DATACAST__JDBC_PASSWORD, //

				// For relay of services
				InfraConstants.DATACAST__RELAY_AUTOSTART, //
				InfraConstants.DATACAST__RELAY_NAME, //
				InfraConstants.DATACAST__RELAY_CONTEXT_ROOT, //
				InfraConstants.DATACAST__RELAY_HOSTS, //
				InfraConstants.DATACAST__RELAY_URLS, //
		};
		return propNames;
	}

}
