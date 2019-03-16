package org.orbit.infra.runtime.util;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class DataTubeConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static DataTubeConfigPropertiesHandler INSTANCE = new DataTubeConfigPropertiesHandler();

	public static DataTubeConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_HOST_URL, //
				// org.orbit.infra.api.InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				// org.orbit.infra.api.InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //

				// For service
				InfraConstants.DATATUBE__AUTOSTART, //
				InfraConstants.DATATUBE__DATACAST_ID, //
				InfraConstants.DATATUBE__ID, //
				InfraConstants.DATATUBE__NAME, //
				InfraConstants.DATATUBE__HOST_URL, //
				InfraConstants.DATATUBE__CONTEXT_ROOT, //
				InfraConstants.DATATUBE__HTTP_PORT, //

				// For relay of services
				InfraConstants.DATATUBE__RELAY_AUTOSTART, //
				InfraConstants.DATATUBE__RELAY_NAME, //
				InfraConstants.DATATUBE__RELAY_CONTEXT_ROOT, //
				InfraConstants.DATATUBE__RELAY_HOSTS, //
				InfraConstants.DATATUBE__RELAY_URLS, //
		};
		return propNames;
	}

}
