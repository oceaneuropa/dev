package org.orbit.component.api.util;

import org.orbit.component.api.ComponentConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class ComponentServicesPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static ComponentServicesPropertiesHandler INSTANCE = new ComponentServicesPropertiesHandler();

	public static ComponentServicesPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				ComponentConstants.ORBIT_IDENTITY_SERVICE_URL, //
				ComponentConstants.ORBIT_USER_ACCOUNTS_URL, //
				ComponentConstants.ORBIT_AUTH_URL, //
				ComponentConstants.ORBIT_APP_STORE_URL, //
				ComponentConstants.ORBIT_DOMAIN_SERVICE_URL, //
				ComponentConstants.ORBIT_NODE_CONTROL_URL, //
				ComponentConstants.ORBIT_MISSION_CONTROL_URL, //
		};
		return propNames;
	}

	public String getIdentityServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		return serviceUrl;
	}

	public String getUserAcountServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		return serviceUrl;
	}

	public String getAuthServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_AUTH_URL);
		return serviceUrl;
	}

	public String getAppStoreURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_APP_STORE_URL);
		return serviceUrl;
	}

	public String getDomainServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		return serviceUrl;
	}

	public String getNodeControlURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_NODE_CONTROL_URL);
		return serviceUrl;
	}

	public String getMissionControlURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_MISSION_CONTROL_URL);
		return serviceUrl;
	}

}
