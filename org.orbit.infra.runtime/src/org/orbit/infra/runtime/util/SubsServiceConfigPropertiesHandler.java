package org.orbit.infra.runtime.util;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsServiceConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static SubsServiceConfigPropertiesHandler INSTANCE = new SubsServiceConfigPropertiesHandler();

	public static SubsServiceConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_HOST_URL, //

				// For service
				InfraConstants.SUBS_SERVER__NAME, //
				InfraConstants.SUBS_SERVER__HOST_URL, //
				InfraConstants.SUBS_SERVER__CONTEXT_ROOT, //
				InfraConstants.SUBS_SERVER__JDBC_DRIVER, //
				InfraConstants.SUBS_SERVER__JDBC_URL, //
				InfraConstants.SUBS_SERVER__JDBC_USERNAME, //
				InfraConstants.SUBS_SERVER__JDBC_PASSWORD, //

				// For relay of services
				InfraConstants.SUBS_SERVER__RELAY_AUTOSTART, //
				InfraConstants.SUBS_SERVER__RELAY_NAME, //
				InfraConstants.SUBS_SERVER__RELAY_CONTEXT_ROOT, //
				InfraConstants.SUBS_SERVER__RELAY_HOSTS, //
				InfraConstants.SUBS_SERVER__RELAY_URLS, //
		};
		return propNames;
	}

}
